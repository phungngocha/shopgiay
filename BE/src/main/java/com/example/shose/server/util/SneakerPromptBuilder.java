package com.example.shose.server.util;

import com.example.shose.server.dto.SneakerAiDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SneakerPromptBuilder {

    public String buildPrompt(String need, List<SneakerAiDto> sneakers) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
        You are a professional sneaker store consultant.

        Customer need:
        "%s"

        Sneaker list:
        """.formatted(need));

        int i = 1;
        for (SneakerAiDto s : sneakers) {
            sb.append("""
            %d. ID: %s | Name: %s | Price: %s | Gender: %s
               Brand: %s | Category: %s | Material: %s | Sole: %s
               Description: %s
            """.formatted(
                    i++, s.getProductId(), s.getProductName(),
                    s.getPrice(), s.getGender(),
                    s.getBrand(), s.getCategory(),
                    s.getMaterial(), s.getSole(),
                    s.getDescription()
            ));
        }

        sb.append("""
        Rules:
        - Recommend maximum 3 sneakers
        - Focus on comfort, usage purpose, and budget
        - Ignore products not matching the need

        Output JSON only:
        [
          {
            "productId": "string",
            "reason": "string"
          }
        ]
        """);

        return sb.toString();
    }
}
