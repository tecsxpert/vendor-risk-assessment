package com.internship.tool.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AiServiceClient {

    private final RestTemplate restTemplate;
    private final String aiServiceUrl;

    public AiServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${ai.service.url:http://localhost:5000}") String aiServiceUrl) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
        this.aiServiceUrl = aiServiceUrl;
    }

    public Map<String, Object> describe(Map<String, Object> request) {
        return post("/describe", request);
    }

    public Map<String, Object> recommend(Map<String, Object> request) {
        return post("/recommend", request);
    }

    public Map<String, Object> categorise(Map<String, Object> request) {
        return post("/categorise", request);
    }

    public Map<String, Object> generateReport(Map<String, Object> request) {
        return post("/generate-report", request);
    }

    public Map<String, Object> query(Map<String, Object> request) {
        return post("/query", request);
    }

    public Map<String, Object> analyseDocument(Map<String, Object> request) {
        return post("/analyse-document", request);
    }

    public Map<String, Object> health() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(aiServiceUrl + "/health", Map.class);
            return normalize(response);
        } catch (RestClientException ex) {
            return null;
        }
    }

    private Map<String, Object> post(String path, Map<String, Object> request) {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiServiceUrl + path,
                    request == null ? Collections.emptyMap() : request,
                    Map.class);
            return normalize(response);
        } catch (RestClientException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> normalize(ResponseEntity<Map> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return null;
        }
        return (Map<String, Object>) response.getBody();
    }
}
