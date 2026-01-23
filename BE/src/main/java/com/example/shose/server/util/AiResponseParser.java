package com.example.shose.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiResponseParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FALLBACK_JSON = """
        {
          "message": "Mình chưa hiểu rõ nhu cầu, bạn mô tả chi tiết hơn nhé 😊",
          "products": []
        }
        """;

    public static <T> T parse(String raw, Class<T> clazz) {

        String json = extractJson(raw);

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            System.err.println("===== AI PARSE FAILED =====");
            System.err.println(raw);
            try {
                return objectMapper.readValue(FALLBACK_JSON, clazz);
            } catch (Exception ex) {
                throw new IllegalStateException("Fallback JSON sai cấu trúc", ex);
            }
        }
    }

    public static String extractJson(String raw) {

        if (raw == null || raw.isBlank()) {
            return FALLBACK_JSON;
        }

        String text = raw.trim();

        // 🔥 Remove ALL markdown code fences
        text = text.replaceAll("(?s)```json\\s*", "")
                .replaceAll("(?s)```\\s*", "")
                .trim();

        // 🔥 Remove BOM if exists
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }

        if (text.startsWith("{") && text.endsWith("}")) {
            return text;
        }

        Pattern pattern = Pattern.compile("\\{[\\s\\S]*}");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return FALLBACK_JSON;
    }

}
