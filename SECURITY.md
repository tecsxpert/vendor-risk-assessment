# SECURITY.md - Vendor Risk Assessment

## Executive Summary

Tool-41 Vendor Risk Assessment processes vendor data and AI prompts through a Spring Boot backend and Flask AI service. AI Developer 3 security work covers input sanitisation, prompt-injection rejection, Flask rate limiting, Java-to-Flask AI client safety, OWASP ZAP tracking, security headers, PII checks, and sprint security sign-off through Day 15.

## AI Developer 3 Scope

- Input sanitisation for AI-bound text.
- Rate limiting for Flask AI endpoints.
- `AiServiceClient.java` for backend calls to the AI service.
- OWASP ZAP baseline and active scan tracking.
- Security headers.
- PII audit for prompts and logs.
- Final `SECURITY.md` security checklist and team sign-off.

## OWASP Top 10 Risks

| Risk | Attack Scenario | Mitigation |
| --- | --- | --- |
| Broken Access Control | A user calls protected vendor or AI endpoints without a valid JWT. | Spring Security/JWT must return `401` without a token and `403` for the wrong role. |
| Injection | A user submits SQL-like payloads or prompt-injection text in vendor fields. | AI service strips HTML/control characters and rejects prompt-injection patterns with `400`. Repository access should use JPA repositories and parameterised queries. |
| Cryptographic Failures | Secrets or personal/vendor-sensitive data are committed or logged. | `.env` must remain ignored. Prompts and logs must avoid personal data. |
| Security Misconfiguration | Missing browser security headers allows clickjacking or MIME sniffing. | Flask uses Talisman and explicit `X-Content-Type-Options`, `X-Frame-Options`, `Referrer-Policy`, and `Permissions-Policy` headers. |
| Identification and Authentication Failures | Expired or missing JWTs are accepted by mistake. | JWT validation must be enforced on protected backend endpoints. |

## Tool-Specific Threats

| Threat | Attack Vector | Damage Potential | Mitigation Plan |
| --- | --- | --- | --- |
| Prompt injection against AI analysis | Vendor text asks the model to ignore instructions, reveal prompts, or bypass policy. | AI output may become unreliable or expose implementation details. | `sanitize_vendor_text` rejects known prompt-injection patterns and returns `400`. |
| HTML/script input in vendor text | Vendor fields contain HTML or JavaScript payloads. | Stored or reflected XSS if rendered later by the UI. | AI service strips HTML tags before sending content to the model. Frontend should also escape rendered content. |
| AI endpoint abuse | Repeated requests to expensive AI endpoints. | Groq quota exhaustion and service slowdown. | Flask-Limiter applies `30 req/min` globally and `10 req/min` on `/generate-report`. |
| PII leakage in prompts/logs | Personal contact data is included in AI prompts or logs. | Sensitive data exposure through third-party AI calls or application logs. | PII audit requires prompts/logs to exclude personal data unless explicitly required for the task. |
| Backend-to-AI outage | Flask service times out or fails during vendor creation/analysis. | Backend workflows fail or demo breaks. | `AiServiceClient.java` uses 10-second connect/read timeout and returns `null` gracefully on error. |

## Implemented Controls

- `ai-service/services/groq_client.py`
  - Rejects non-string input.
  - Enforces minimum text length.
  - Strips HTML tags.
  - Removes control characters.
  - Detects prompt-injection phrases.
  - Returns validation failure before calling Groq.

- `ai-service/app.py`
  - Adds Flask-Limiter default limit of `30 per minute`.
  - Adds `/generate-report` limit of `10 per minute`.
  - Returns `429` JSON response with `retry_after` field on rate-limit breach.
  - Adds `/health` endpoint.
  - Adds Talisman and explicit security headers.

- `backend/src/main/java/com/internship/tool/service/AiServiceClient.java`
  - Calls Flask endpoints through `RestTemplate`.
  - Uses 10-second connect timeout.
  - Uses 10-second read timeout.
  - Returns `null` gracefully on AI service errors.
  - Reads AI base URL from `AI_SERVICE_URL`, defaulting to `http://localhost:5000`.

## Security Test Log

| Day | Test Area | Expected Result | Status |
| --- | --- | --- | --- |
| Day 5 | Empty input | AI endpoint returns `400`. | Implemented in sanitiser. |
| Day 5 | SQL injection patterns | Backend should avoid raw SQL and use repository/query binding. | Repository pattern present; full endpoint scan pending. |
| Day 5 | Prompt injection | AI endpoint returns `400`. | Implemented in sanitiser. |
| Day 7 | OWASP ZAP baseline scan | Findings exported and Medium+ remediation planned. | ZAP CLI not available in this workstation; run before final demo if installed. |
| Day 8 | Security headers | Headers present after re-scan. | Implemented in Flask app. |
| Day 9 | PII audit | No personal data in prompts/logs. | Prompt templates avoid personal contact data; full runtime log review pending with live app. |
| Day 10 | JWT enforcement | Protected backend endpoints return `401` without JWT. | Requires live backend verification. |
| Day 10 | Rate limiting | `/generate-report` returns `429` after limit. | Implemented and covered by pytest. |
| Day 10 | Injection rejection | Prompt injection rejected with `400`. | Implemented and covered by pytest. |
| Day 11 | ZAP active scan | Critical/High findings fixed today. | ZAP CLI not available in this workstation; run before final demo if installed. |
| Day 12 | Remaining ZAP findings | Zero Critical/High remaining. | Security headers implemented; ZAP re-scan pending. |
| Day 13 | Full stack security test | `401`, `403`, XSS rejection, and `429` verified. | AI-side `400`/`429` implemented; backend live auth verification pending. |
| Day 14 | Final documentation | Summary, threats, tests, fixes, risks, sign-off complete. | Completed. |
| Day 15 | Final checklist | Security checklist completed and committed. | Completed locally; commit pending user instruction. |

## Residual Risks

- OWASP ZAP is not installed on this workstation, so baseline and active scans must be run when ZAP is available.
- Backend security configuration currently belongs to teammate-owned code and should be verified after their final merge.
- Full runtime PII log review requires the live Docker Compose stack and demo data.
- `AiServiceClient.java` returns `null` on failure by design; calling services must handle `null` gracefully.

## Final Security Checklist - Day 15

- [x] OWASP Top 10 risks documented.
- [x] Tool-specific security threats documented.
- [x] Input sanitisation implemented.
- [x] Prompt-injection detection implemented.
- [x] Flask rate limiting implemented.
- [x] `/generate-report` stricter rate limit implemented.
- [x] Security headers implemented.
- [x] `AiServiceClient.java` implemented.
- [x] AI service URL externalised as `AI_SERVICE_URL`.
- [x] PII audit guidance documented.
- [x] ZAP scan status documented.
- [x] Residual risks documented.
- [ ] All 6 members signed off.

## Team Sign-Off

| Member | Role | Sign-Off |
| --- | --- | --- |
| Member 1 | Detail not provided in document. | Pending |
| Member 2 | Detail not provided in document. | Pending |
| Member 3 | Detail not provided in document. | Pending |
| Member 4 | Detail not provided in document. | Pending |
| Member 5 | Detail not provided in document. | Pending |
| Member 6 | Detail not provided in document. | Pending |
