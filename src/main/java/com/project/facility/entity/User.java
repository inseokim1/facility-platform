package com.project.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자 정보를 DB 테이블과 매핑하는 Entity 클래스
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    // 사용자 고유 ID, 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이메일
    // 추후 로그인 ID로 사용할 예정
    private String email;

    // 사용자 비밀번호
    // 추후 Spring Security 적용 시 BCrypt로 암호화 예정
    private String password;

    // 사용자 이름
    private String name;

    // 사용자 권한
    // 예: USER, ADMIN
    private String role;
}