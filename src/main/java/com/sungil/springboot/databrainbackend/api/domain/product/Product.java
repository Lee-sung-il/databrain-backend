package com.sungil.springboot.databrainbackend.api.domain.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // 제목 (영어 원문)
    private String description; // 설명 (영어 원문)
    private Integer price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category; // 카테고리 Enum (AI_PROMPT, DATASET 등)

    private String sellerName; // 판매자 닉네임

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Product(String title, String description, Integer price, ProductCategory category, String sellerName) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerName = sellerName;
    }
}