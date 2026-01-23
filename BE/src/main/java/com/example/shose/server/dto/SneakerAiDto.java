package com.example.shose.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SneakerAiDto {
    private String idProductDetail;
    private String nameProduct;
    private BigDecimal price;
    private String gender;
}
