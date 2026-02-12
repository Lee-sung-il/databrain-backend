package com.sungil.springboot.databrainbackend.api.dto;

import com.sungil.springboot.databrainbackend.api.domain.Language;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [Language Update Request DTO]
 * 사용자의 선호 언어 변경 요청을 처리하기 위한 객체입니다.
 * 시니어 포인트:
 * 1. Enum(Language)을 직접 사용하여 잘못된 언어 코드가 들어오는 것을 원천 차단합니다.
 * 2. @NotNull을 통해 필수값 누락을 방지합니다.
 */
@Getter
@NoArgsConstructor
public class UserLanguageUpdateRequest {

    @NotNull(message = "변경할 언어를 선택해주세요. (KO, EN, JA, ZH)")
    private Language language;
}