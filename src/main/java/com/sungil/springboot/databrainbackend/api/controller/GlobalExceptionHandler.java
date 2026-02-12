package com.sungil.springboot.databrainbackend.api.controller;

import com.sungil.springboot.databrainbackend.api.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * [Centralized Exception Handler]
 * 애플리케이션 전역에서 발생하는 예외를 감지하고 규격화된 에러 응답을 생성합니다.
 * * Senior Point:
 * 1. Slf4j 로깅: 에러 발생 시 로그를 남겨 운영팀이 문제를 즉시 추적할 수 있게 합니다.
 * 2. Exception 관리: 비즈니스 예외와 시스템 예외를 명확히 분리합니다.
 * 3. 보안: 모든 예외의 스택 트레이스를 숨겨 서버 내부 구조 노출을 방지합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 상의 예외 처리 (ID 중복, 비밀번호 불일치 등)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Business Logic Error: {}", e.getMessage()); // 경고 수준 로그
        ErrorResponse response = new ErrorResponse(e.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * @Valid 검증 실패 시 발생하는 예외 처리 (형식 오류, 빈칸 등)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Failed: {}", errorMessage);

        ErrorResponse response = new ErrorResponse(errorMessage, 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * [Senior's Defense] 예상치 못한 모든 시스템 에러 처리 (500 에러)
     * 서버가 갑자기 죽지 않게 하고, 사용자에게는 정제된 메시지만 전달합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error("Unhandled System Error: ", e); // 에러 수준 로그와 스택 트레이스 기록

        ErrorResponse response = new ErrorResponse("An internal server error occurred.", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}