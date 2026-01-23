package com.example.shose.server.dto.response;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface SearchProductBasic {
    @Value("#{target.idProduct}")
    String getIdProduct();
    @Value("#{target.idProductDetail}")
    String getIdProductDetail();
    @Value("#{target.image}")
    String getImage();
    @Value("#{target.codeProduct}")
    String getCodeProduct();
    @Value("#{target.nameProduct}")
    String getNameProduct();
    @Value("#{target.price}")
    BigDecimal getPrice();
    @Value("#{target.brandName}")
    String getBrandName();
    @Value("#{target.valuePromotion}")
    String getValuePromotion();
//    @Value("#{target.createdDate}")
//    String getCreatedDate();
}
