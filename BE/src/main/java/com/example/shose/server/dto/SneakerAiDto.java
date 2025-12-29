package com.example.shose.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SneakerAiDto {
    private String productId;
    private String productName;
    private BigDecimal price;
    private String gender;
    private String description;
    private String brand;
    private String category;
    private String material;
    private String sole;
}
