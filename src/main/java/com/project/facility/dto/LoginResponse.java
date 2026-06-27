package com.project.facility.dto;

import com.project.facility.entity.User;
import lombok.Getter;

// 로그인 응답 데이터를 담는 DTO
@Getter
public class LoginResponse {

    // 응답 메시지
    private String message;

    // 사용자 ID
    private Long userId;

    // 사용자 이메일
    private String email;

    // 사용자 이름
    private String name;

    // 사용자 권한
    private String role;

    //JWT Token
    private String token;

    public LoginResponse(User user, String token) {
        this.message = "로그인 성공";
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.token = token;
    }
}