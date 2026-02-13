package com.sungil.springboot.databrainbackend.api.controller;

import com.sungil.springboot.databrainbackend.api.domain.product.Product;
import com.sungil.springboot.databrainbackend.api.domain.product.ProductCategory;
import com.sungil.springboot.databrainbackend.api.domain.user.User;
import com.sungil.springboot.databrainbackend.api.dto.ProductResponse;
import com.sungil.springboot.databrainbackend.api.repository.ProductRepository;
import com.sungil.springboot.databrainbackend.api.repository.UserRepository;
import com.sungil.springboot.databrainbackend.api.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TranslationService translationService; // 아까 만든 번역 서비스 주입

    @GetMapping
    public List<ProductResponse> getProducts(@AuthenticationPrincipal UserDetails userDetails) {
        // 1. 언어 설정 (기존과 동일)
        String targetLang = "EN";
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) targetLang = String.valueOf(user.getLanguage());
        }

        List<Product> products = productRepository.findAll();

        // [수정] DB가 비어있으면 -> 가짜 데이터를 "저장"해서 ID를 생성받음
        if (products.isEmpty()) {
            Product p1 = Product.builder()
                    .title("Spicy Kimchi Stew Recipe")
                    .description("Authentic Korean taste with deep flavor.")
                    .price(5000)
                    .category(ProductCategory.EBOOK)
                    .sellerName("Chef_Kim")
                    .build();

            Product p2 = Product.builder()
                    .title("Python AI Source Code")
                    .description("Easy to learn AI programming for beginners.")
                    .price(30000)
                    .category(ProductCategory.SOURCE_CODE)
                    .sellerName("Dev_Lee")
                    .build();

            // ★★★ 여기서 저장(save)을 해야 ID(1, 2)가 생깁니다! ★★★
            productRepository.saveAll(List.of(p1, p2));

            // 저장된 걸 다시 가져옴
            products = productRepository.findAll();
        }

        // 3. 번역 및 변환 (기존과 동일)
        String finalLang = targetLang;
        return products.stream().map(product -> {
            String transTitle = translationService.translate(product.getTitle(), finalLang);
            String transDesc = translationService.translate(product.getDescription(), finalLang);
            return new ProductResponse(product, transTitle, transDesc);
        }).collect(Collectors.toList());
    }
}