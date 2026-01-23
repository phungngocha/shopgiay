package com.example.shose.server.util;

import com.example.shose.server.dto.SneakerAiDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SneakerPromptBuilder {

    public String buildPrompt(String userMessage, List<SneakerAiDto> sneakers) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                Bạn là trợ lý bán giày sneaker, nói tiếng Việt, thân thiện và ngắn gọn.
                
                Quy tắc:
                - Nếu khách chỉ chào → chào lại, hỏi nhu cầu, products = []
                - Nếu chưa rõ nhu cầu → hỏi thêm (giới tính, mục đích, size, ngân sách), products = []
                - Nếu nhu cầu rõ → gợi ý tối đa 3 sản phẩm phù hợp
                - Chỉ nói về sản phẩm có trong danh sách
                - Không tự bịa thông tin
                
                Luôn trả về JSON theo format:
                {
                  "message": "string",
                  "products": [
                    { "idProductDetail": "string", "reason": "string" }
                  ]
                }
                
                Tin nhắn khách:
                "%s"
                
                Danh sách sneaker:
                """.formatted(userMessage));
        int i = 1;
        for (SneakerAiDto s : sneakers) {
            sb.append("""
            - %s | %s | %s | %s
            """.formatted(
                    s.getIdProductDetail(),
                    s.getNameProduct(),
                    s.getPrice(),
                    s.getGender()
            ));
        }

        return sb.toString();
    }
}
