package com.sungil.springboot.databrainbackend.api.dto;

import com.sungil.springboot.databrainbackend.api.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long id;
    private final String title;       // 번역된 제목
    private final String description; // 번역된 설명
    private final Integer price;
    private final String sellerName;
    private final String category;

    public ProductResponse(Product product, String translatedTitle, String translatedDesc) {
        this.id = product.getId();
        this.title = translatedTitle;
        this.description = translatedDesc;
        this.price = product.getPrice();
        this.sellerName = product.getSellerName();
        this.category = product.getCategory().name();
    }
}