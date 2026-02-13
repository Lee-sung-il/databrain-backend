package com.sungil.springboot.databrainbackend.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranslationService {

    // application.properties에 설정된 주소를 가져옵니다.
    @Value("${translation.api.url}")
    private String apiUrl;

    // 1. 언어 감지 (기존 코드 유지)
    public String detectLanguage(String text) {
        if (text == null || text.isBlank()) return "en";
        try {
            RestTemplate restTemplate = new RestTemplate();
            String detectUrl = apiUrl.replace("/translate", "/detect");
            Map<String, String> body = Map.of("q", text);
            List<Map<String, Object>> response = restTemplate.postForObject(detectUrl, body, List.class);
            if (response != null && !response.isEmpty()) {
                return (String) response.get(0).get("language");
            }
        } catch (Exception e) {
            return "en";
        }
        return "en";
    }

    // 2. 번역 (기존 코드 유지 - 인자 3개)
    public String translate(String text, String source, String target) {
        if (text == null || text.isBlank()) return "";
        if (source.equals(target)) return text;

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> requestBody = new HashMap<>(); // Object로 변경 (유연성)
            requestBody.put("q", text);
            requestBody.put("source", source);
            requestBody.put("target", target);
            requestBody.put("format", "text");

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestBody, Map.class);
            return (String) response.getBody().get("translatedText");
        } catch (Exception e) {
            System.err.println("Translation Error: " + e.getMessage()); // 로그 추가
            return text;
        }
    }

    // [NEW] ★★★ 이 메서드를 추가하세요! ★★★
    // 컨트롤러가 편하게 쓸 수 있도록 도와주는 '다리' 메서드입니다.
    public String translate(String text, String target) {
        // 1. 먼저 언어를 감지하고
        String source = detectLanguage(text);
        // 2. 감지된 언어를 넣어서 진짜 번역을 돌립니다.
        return translate(text, source, target);
    }
}