package com.project.facility.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 모든 Controller에서 발생하는 예외를 공통으로 처리하는 클래스
@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 예외가 발생했을 때 실행되는 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        // 400 Bad Request 상태 코드와 예외 메시지를 응답으로 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}