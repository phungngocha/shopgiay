package com.example.shose.server.controller.client;

import com.example.shose.server.dto.SneakerAiDto;
import com.example.shose.server.dto.request.ConsultRequest;
import com.example.shose.server.dto.response.AiChatResponse;
import com.example.shose.server.dto.response.SneakerChatResponse;
import com.example.shose.server.dto.response.SneakerConsultResponse;
import com.example.shose.server.service.OpentAiService;
import com.example.shose.server.service.ProductService;
import com.example.shose.server.util.AiResponseParser;
import com.example.shose.server.util.ResponseObject;
import com.example.shose.server.util.SneakerPromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
    @RequestMapping("/client/ai")
public class ConsultSneakerRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SneakerPromptBuilder promptBuilder;

    @Autowired
    private OpentAiService opentAiService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/consult-sneaker")
    public ResponseObject consultSneaker(@RequestBody ConsultRequest request) {

        List<SneakerAiDto> sneakers = productService.getSneakers();

        String prompt = promptBuilder.buildPrompt(
                request.getNeed(),
                sneakers
        );

        String aiResult = opentAiService.callAI(prompt);

        SneakerChatResponse aiResponse =
                AiResponseParser.parse(aiResult, SneakerChatResponse.class);

        if (aiResponse.getProducts() == null || aiResponse.getProducts().isEmpty()) {
            return new ResponseObject(
                    Map.of(
                            "message", aiResponse.getMessage(),
                            "products", List.of()
                    )
            );
        }

        List<String> ids = aiResponse.getProducts().stream()
                .map(SneakerConsultResponse::getIdProductDetail)
                .toList();

        List<SneakerAiDto> details =
                productService.getDetailsByIds(ids);


        return new ResponseObject(
                Map.of(
                        "message", aiResponse.getMessage(),
                        "products", details
                )
        );

    }


}
