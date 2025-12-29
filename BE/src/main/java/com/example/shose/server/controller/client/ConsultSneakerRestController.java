package com.example.shose.server.controller.client;

import com.example.shose.server.dto.SneakerAiDto;
import com.example.shose.server.dto.request.ConsultRequest;
import com.example.shose.server.dto.response.SneakerConsultResponse;
import com.example.shose.server.service.GeminiService;
import com.example.shose.server.service.ProductService;
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

@RestController
@RequestMapping("/client/ai")
public class ConsultSneakerRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SneakerPromptBuilder promptBuilder;

    @Autowired
    private GeminiService geminiService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/consult-sneaker")
    public ResponseObject consultSneaker(
            @RequestBody ConsultRequest request) {

        List<SneakerAiDto> sneakers = productService.getSneakers();

        String prompt = promptBuilder.buildPrompt(
                request.getNeed(),
                sneakers
        );

        String aiResult = geminiService.callAI(prompt);

        try {
            List<SneakerConsultResponse> result =
                    objectMapper.readValue(
                            aiResult,
                            new TypeReference<List<SneakerConsultResponse>>() {}
                    );

            return new ResponseObject(result);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AI RAW RESULT:");
            System.out.println(aiResult);

            return new ResponseObject(Collections.emptyList());
        }
    }



}
