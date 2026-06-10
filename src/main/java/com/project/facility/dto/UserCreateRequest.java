package com.project.facility.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 사용자 등록/수정 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class UserCreateRequest {

    // 사용자 이메일
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    // 사용자 비밀번호
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    // 사용자 이름
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    // 사용자 권한
    // 예: USER, ADMIN
    @NotBlank(message = "권한은 필수입니다.")
    private String role;
}