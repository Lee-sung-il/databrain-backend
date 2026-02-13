package com.sungil.springboot.databrainbackend.api.dto;

import com.sungil.springboot.databrainbackend.api.domain.Language;
import com.sungil.springboot.databrainbackend.api.domain.Role;
import com.sungil.springboot.databrainbackend.api.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [User Signup DTO]
 * 회원가입 요청 데이터를 캡슐화하고 유효성을 검증합니다.
 * 시니어급 포인트: DTO 내부에서 Entity 변환 로직(toEntity)을 관리하여 서비스 계층의 부담을 줄입니다.
 */
@Getter
@NoArgsConstructor
public class UserSignupRequest {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    /**
     * [다국어 선택]
     * 사용자가 회원가입 시 선택한 기본 언어 설정 (KO, EN, JA, ZH)
     */
    @NotNull(message = "선호 언어 선택은 필수입니다.")
    private Language language;

    /**
     * DTO를 User 엔티티로 변환하는 정적 팩토리 메서드 스타일의 변환 로직
     * @param encodedPassword 암호화된 비밀번호
     * @return 빌더 패턴을 적용한 User 엔티티
     */
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickname(this.nickname)
                .role(Role.ROLE_FREE) // 신규 가입 시 기본 권한 부여
                .language(this.language) // 선택한 언어 설정 저장
                .build();
    }
}