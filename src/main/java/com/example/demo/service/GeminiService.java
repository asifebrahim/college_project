package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    public String generateText(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Guard against null/empty prompt to avoid Map.of() NullPointerException
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Error: prompt is empty or null";
        }

        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                        GEMINI_URL + apiKey, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});

                // Parse response text defensively
                try {
                        if (response == null || response.getBody() == null) {
                                return "Error: empty response from Gemini";
                        }

                        Object candidatesObj = response.getBody().get("candidates");
                        if (!(candidatesObj instanceof java.util.List)) {
                                // Try other common fields
                                Object output = response.getBody().get("output");
                                if (output != null) return output.toString();
                                return "Error: unexpected response shape: " + response.getBody().toString();
                        }

                        java.util.List<?> candidates = (java.util.List<?>) candidatesObj;
                        if (candidates.isEmpty()) return "Error: no candidates returned";

                        Object first = candidates.get(0);
                        if (!(first instanceof Map)) return first.toString();

                        Map<?, ?> firstMap = (Map<?, ?>) first;
                        Object content = firstMap.get("content");
                        if (content == null) content = firstMap.get("output");
                        if (content == null) content = firstMap.get("text");
                        if (content == null) return "Error: candidate had no content: " + firstMap.toString();

                        return content.toString();
                } catch (Exception e) {
                        return "Error: " + e.getMessage();
                }
    }
}
