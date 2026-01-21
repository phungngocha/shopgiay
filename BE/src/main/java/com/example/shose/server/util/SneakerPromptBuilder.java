package com.example.shose.server.util;

import com.example.shose.server.dto.SneakerAiDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SneakerPromptBuilder {

    public String buildPrompt(String userMessage, List<SneakerAiDto> sneakers) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                You are a friendly and professional sneaker store assistant.
                
                Your behavior rules:
                - If the customer greets (e.g. "hi", "hello", "chào shop"):
                  → Greet back politely and ask what kind of sneakers they are looking for.
                - If the customer's need is unclear:
                  → Ask follow-up questions (gender, purpose, budget, size).
                - If the customer has a clear need:
                  → Recommend up to 3 suitable sneakers from the list.
                - Only talk about sneakers in this store.
                - Never invent price, size, brand, or availability.
                - If no sneaker matches, say so honestly.
                
                Response rules:
                - ALWAYS return JSON
                - Use simple, friendly Vietnamese
                - Keep responses concise and natural
                
                JSON response format:
                {
                  "message": "string",
                  "products": [
                    {
                      "productId": "string",
                      "reason": "string"
                    }
                  ]
                }
                
                Customer message:
                "%s"
                
                Sneaker list:
                """.formatted(userMessage));

        int i = 1;
        for (SneakerAiDto s : sneakers) {
            sb.append("""
                    %d. ID: %s
                       Name: %s
                       Price: %s
                       Gender: %s
                       Brand: %s
                       Category: %s
                       Material: %s
                       Sole: %s
                       Description: %s
                    """.formatted(
                    i++,
                    s.getProductId(),
                    s.getProductName(),
                    s.getPrice(),
                    s.getGender(),
                    s.getBrand(),
                    s.getCategory(),
                    s.getMaterial(),
                    s.getSole(),
                    s.getDescription()
            ));
        }

        sb.append("""
                Important:
                - If the customer is only greeting, return an empty products array.
                - If you ask questions, return an empty products array.
                - Only include products that clearly match the customer's need.
                """);

        return sb.toString();
    }
}
