import time
from flask import Flask, jsonify, request
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from flask_talisman import Talisman

from services.groq_client import (
    GroqRiskClient,
    InputValidationError,
    sanitize_vendor_text,
)

DEFAULT_MODEL = "llama-3.3-70b-versatile"


def create_app():
    app = Flask(__name__)
    Talisman(
        app,
        force_https=False,
        frame_options="DENY",
        content_security_policy={
            "default-src": "'self'",
            "img-src": "'self' data:",
            "style-src": "'self' 'unsafe-inline'",
        },
    )
    limiter = Limiter(
        get_remote_address,
        app=app,
        default_limits=["30 per minute"],
        headers_enabled=True,
    )
    client = GroqRiskClient(model=DEFAULT_MODEL)

    @app.errorhandler(429)
    def rate_limit_handler(error):
        retry_after = getattr(error, "retry_after", None)
        return jsonify({
            "status": "error",
            "code": "RATE_LIMIT_EXCEEDED",
            "message": "Too many requests. Please retry later.",
            "retry_after": retry_after,
        }), 429

    @app.after_request
    def add_security_headers(response):
        response.headers["X-Content-Type-Options"] = "nosniff"
        response.headers["X-Frame-Options"] = "DENY"
        response.headers["Referrer-Policy"] = "no-referrer"
        response.headers["Permissions-Policy"] = "geolocation=(), microphone=(), camera=()"
        return response

    @app.route("/health", methods=["GET"])
    def health():
        return jsonify({
            "status": "UP",
            "service": "vendor-risk-ai-service",
            "model": DEFAULT_MODEL,
        })

    # =========================
    # 1. CATEGORISE ENDPOINT
    # =========================
    @app.route("/categorise", methods=["POST"])
    def categorise():
        start = time.time()

        try:
            payload = request.get_json()

            if not payload or "text" not in payload:
                raise InputValidationError("Text field is required")

            text = payload["text"]

            cleaned_text, flagged = sanitize_vendor_text(text)

            result = client.classify_vendor_risk(
                vendor_name=payload.get("vendor_name", "Unknown"),
                text=cleaned_text,
                industry=payload.get("context", {}).get("industry", ""),
                region=payload.get("context", {}).get("region", ""),
                contract_value=payload.get("context", {}).get("contract_value", 0),
            )

            data = result.data

            # fallback logic for low confidence
            if data.get("confidence", 0) < 0.40:
                data["category"] = "Medium Risk"
                data["reasoning"] += " (Low confidence - default applied)"

            response_time = int((time.time() - start) * 1000)

            return jsonify({
                "status": "success",
                "data": data,
                "meta": {
                    "model": result.model,
                    "tokens_used": result.tokens_used,
                    "response_time_ms": response_time,
                    "input_length": len(cleaned_text),
                }
            })

        except InputValidationError as e:
            return jsonify({
                "status": "error",
                "code": "VALIDATION_ERROR",
                "message": str(e)
            }), 400

        except Exception as e:
            return jsonify({
                "status": "error",
                "code": "AI_FAILURE",
                "message": str(e),
                "fallback_category": "Medium Risk"
            }), 500

    # =========================
    # 2. GENERATE REPORT ENDPOINT
    # =========================
    @app.route("/generate-report", methods=["POST"])
    @limiter.limit("10 per minute")
    def generate_report():
        try:
            payload = request.get_json()

            if not payload or "text" not in payload:
                raise InputValidationError("Text field is required")

            text = payload["text"]

            cleaned_text, _ = sanitize_vendor_text(text)

            result = client.classify_vendor_risk(
                vendor_name=payload.get("vendor_name", "Unknown"),
                text=cleaned_text,
                industry=payload.get("context", {}).get("industry", ""),
                region=payload.get("context", {}).get("region", ""),
                contract_value=payload.get("context", {}).get("contract_value", 0),
            )

            data = result.data

            report = {
                "summary": data["reasoning"],
                "risk_level": data["category"],
                "confidence": data["confidence"],
                "key_issues": data["signals"]["negative"],
                "recommendation": ", ".join(data["suggested_actions"])
            }

            return jsonify({
                "status": "success",
                "report": report
            })

        except InputValidationError as e:
            return jsonify({
                "status": "error",
                "code": "VALIDATION_ERROR",
                "message": str(e)
            }), 400

        except Exception as e:
            return jsonify({
                "status": "error",
                "message": str(e)
            }), 500

    return app


if __name__ == "__main__":
    app = create_app()
    app.run(debug=True)
