package com.project.facility.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 이 클래스가 Spring 설정 클래스라는 의미
@Configuration
public class SecurityConfig {

    // 이 메서드가 반환하는 객체를 Spring Bean으로 등록한다는 의미
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // API 테스트를 위해 CSRF 보안 기능을 임시로 비활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 요청별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 모든 요청을 로그인 없이 허용
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}