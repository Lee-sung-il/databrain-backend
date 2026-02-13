package com.sungil.springboot.databrainbackend.api.service;

import com.sungil.springboot.databrainbackend.api.domain.Language;
import com.sungil.springboot.databrainbackend.api.domain.user.User;
import com.sungil.springboot.databrainbackend.api.repository.UserRepository;
import com.sungil.springboot.databrainbackend.api.dto.*;
import com.sungil.springboot.databrainbackend.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

/**
 * [User Service Layer]
 * 비즈니스 로직을 수행하며 다국어 메시지 처리를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    /**
     * 회원가입 - 중복 이메일 체크 및 엔티티 저장
     */
    @Transactional
    public Map<String, String> signUp(UserSignupRequest request) {
        Locale locale = getLocale(request.getLanguage());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(messageSource.getMessage("error.email.exists", null, locale));
        }

        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return Map.of("message", messageSource.getMessage("signup.success", null, locale));
    }

    /**
     * 로그인 - 자격 증명 확인 및 JWT 토큰 발행
     */
    public Map<String, Object> login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage("error.password.wrong", null, getLocale(user.getLanguage())));
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
        return Map.of(
                "token", token,
                "language", user.getLanguage(),
                "message", messageSource.getMessage("login.success", null, getLocale(user.getLanguage()))
        );
    }

    /**
     * [추가] 내 정보 조회 (마이페이지)
     * 시니어 포인트: 보안을 위해 엔티티 대신 전용 DTO(UserResponse)를 반환합니다.
     */
    public UserResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserResponse.fromEntity(user);
    }

    /**
     * [수정] 언어 설정 변경 (컨트롤러와 이름 통일)
     */
    @Transactional
    public Map<String, String> updateLanguage(String email, Language newLang) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // JPA의 변경 감지(Dirty Checking)를 통해 자동 업데이트됩니다.
        user.updateLanguage(newLang);

        return Map.of(
                "message", messageSource.getMessage("language.changed", null, getLocale(newLang)),
                "currentLanguage", newLang.toString()
        );
    }

    /**
     * 언어 코드(Enum)를 시스템 Locale로 변환
     */
    private Locale getLocale(Language lang) {
        return lang == null ? Locale.ENGLISH : switch (lang) {
            case KO -> Locale.KOREAN;
            case JA -> Locale.JAPANESE;
            case ZH -> Locale.CHINESE;
            default -> Locale.ENGLISH;
        };
    }
}