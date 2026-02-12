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

    @Value("${translation.api.url}")
    private String apiUrl; // http://localhost:5001/translate

    /**
     * 1. 언어 감지: 입력된 텍스트가 어떤 언어인지 알아냅니다.
     */
    public String detectLanguage(String text) {
        if (text == null || text.isBlank()) return "en";
        try {
            RestTemplate restTemplate = new RestTemplate();
            // /translate 대신 /detect 엔드포인트 사용
            String detectUrl = apiUrl.replace("/translate", "/detect");

            Map<String, String> body = Map.of("q", text);
            List<Map<String, Object>> response = restTemplate.postForObject(detectUrl, body, List.class);

            // 가장 확률이 높은 첫 번째 언어 코드 반환 (예: "ko", "en")
            if (response != null && !response.isEmpty()) {
                return (String) response.get(0).get("language");
            }
        } catch (Exception e) {
            return "en"; // 실패 시 기본값 영어
        }
        return "en";
    }

    /**
     * 2. 번역: 원문 언어(source)를 직접 받아서 번역합니다.
     */
    public String translate(String text, String source, String target) {
        if (text == null || text.isBlank()) return "";
        if (source.equals(target)) return text; // 원문과 목적지가 같으면 번역 생략

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("q", text);
            requestBody.put("source", source); // 감지된 언어 삽입
            requestBody.put("target", target);
            requestBody.put("format", "text");

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestBody, Map.class);
            return (String) response.getBody().get("translatedText");
        } catch (Exception e) {
            return text;
        }
    }
}