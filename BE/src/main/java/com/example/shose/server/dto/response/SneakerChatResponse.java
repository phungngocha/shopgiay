package com.example.shose.server.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SneakerChatResponse {
    private String message;
    private List<SneakerConsultResponse> products;
}
