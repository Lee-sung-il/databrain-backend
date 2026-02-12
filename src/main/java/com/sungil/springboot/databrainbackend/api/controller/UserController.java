package com.sungil.springboot.databrainbackend.api.controller;

import com.sungil.springboot.databrainbackend.api.dto.*;
import com.sungil.springboot.databrainbackend.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * [User Management Controller - Final Version]
 * 사용자 가입, 인증, 프로필 관리 및 다국어 설정을 담당합니다.
 * 시니어 포인트:
 * 1. produces 설정을 통해 한글 깨짐(????) 현상을 원천 차단합니다.
 * 2. @AuthenticationPrincipal을 사용하여 토큰에서 유저 정보를 우아하게 추출합니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     * POST http://localhost:8080/api/users/signup
     */
    @PostMapping(value = "/signup", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Map<String, String>> signup(@RequestBody @Valid UserSignupRequest request) {
        log.info("New signup request: {}", request.getEmail());
        Map<String, String> result = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 로그인 API
     * POST http://localhost:8080/api/users/login
     */
    @PostMapping(value = "/login", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid UserLoginRequest request) {
        log.info("Login attempt: {}", request.getEmail());
        Map<String, Object> loginResponse = userService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * 내 정보 조회 (마이페이지)
     * GET http://localhost:8080/api/users/me
     * 인증 토큰(JWT) 필수
     */
    @GetMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<UserResponse> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Accessing MyPage: {}", userDetails.getUsername());
        UserResponse profile = userService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    /**
     * 언어 설정 변경
     * PATCH http://localhost:8080/api/users/language
     * 인증 토큰(JWT) 필수
     */
    @PatchMapping(value = "/language", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Map<String, String>> updateLanguage(
            @RequestBody @Valid UserLanguageUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Changing language for user {}: {}", userDetails.getUsername(), request.getLanguage());
        Map<String, String> result = userService.updateLanguage(userDetails.getUsername(), request.getLanguage());
        return ResponseEntity.ok(result);
    }
}