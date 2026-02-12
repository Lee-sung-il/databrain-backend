package com.sungil.springboot.databrainbackend.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message; //에러 메시지
    private final int statusCode;  // 상태 코드
}
