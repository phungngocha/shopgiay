package com.example.shose.server.service.impl;

import com.example.shose.server.service.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiServiceImpl implements GeminiService {

    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String callAI(String prompt) {

        String url = "https://api.openai.com/v1/chat/completions";

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        String aiText = extractText(response.getBody());

        return cleanJson(aiText);
    }


    private String extractText(Map<?, ?> body) {
        try {
            List<?> choices = (List<?>) body.get("choices");
            Map<?, ?> message =
                    (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");

            Object content = message.get("content");

            // content dạng String
            if (content instanceof String) {
                return (String) content;
            }

            // content dạng List (API mới)
            if (content instanceof List) {
                StringBuilder sb = new StringBuilder();
                for (Object part : (List<?>) content) {
                    if (part instanceof Map) {
                        Object text = ((Map<?, ?>) part).get("text");
                        if (text != null) sb.append(text);
                    }
                }
                return sb.toString();
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String cleanJson(String aiResult) {
        if (aiResult == null) return null;

        return aiResult
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }


}