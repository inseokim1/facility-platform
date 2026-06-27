package com.project.facility.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class LoginRequest {

    // 사용자 이메일
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    // 사용자 비밀번호
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}