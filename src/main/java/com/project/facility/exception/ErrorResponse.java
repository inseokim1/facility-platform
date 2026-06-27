package com.project.facility.exception;

import lombok.Getter;

import java.time.LocalDateTime;

// 에러 응답 형식을 통일하기 위한 DTO
@Getter
public class ErrorResponse {

    // HTTP 상태 코드
    private int status;

    // 에러 메시지
    private String message;

    // 에러 발생 시간
    private LocalDateTime time;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}