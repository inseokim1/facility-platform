package com.project.facility.dto;

import com.project.facility.entity.User;
import lombok.Getter;

// 사용자 응답 데이터를 담는 DTO
@Getter
public class UserResponse {

    // 사용자 고유 ID
    private Long id;

    // 사용자 이메일
    private String email;

    // 사용자 이름
    private String name;

    // 사용자 권한
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
    }
}