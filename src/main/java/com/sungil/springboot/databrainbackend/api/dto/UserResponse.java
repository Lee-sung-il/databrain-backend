package com.sungil.springboot.databrainbackend.api.dto;

import com.sungil.springboot.databrainbackend.api.domain.Language;
import com.sungil.springboot.databrainbackend.api.domain.User;
import lombok.Builder;
import lombok.Getter;

/**
 * [User Profile Response DTO]
 * 클라이언트에게 안전하게 사용자 프로필 정보를 전달합니다.
 */
@Getter
@Builder
public class UserResponse {
    private String email;
    private String nickname;
    private Language language;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .language(user.getLanguage())
                .build();
    }
}