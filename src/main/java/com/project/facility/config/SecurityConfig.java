package com.project.facility.config;

import com.project.facility.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 관련 설정 클래스
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCrypt 알고리즘으로 비밀번호를 단방향 해시 처리
        return new BCryptPasswordEncoder();
    }

    // HTTP 요청에 대한 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // API 테스트를 위해 CSRF 보안 기능을 임시로 비활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 요청별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 회원가입은 로그인 없이 허용
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // 로그인은 인증 없이 허용
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                        // 시설 조회/검색은 로그인 없이 허용
                        .requestMatchers(HttpMethod.GET, "/api/facilities/**").permitAll()

                        // 카테고리 조회는 로그인 없이 허용
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 시설 등록/수정/삭제는 관리자만 허용
                        .requestMatchers(HttpMethod.POST, "/api/facilities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/facilities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/facilities/**").hasRole("ADMIN")

                        // 카테고리 등록/수정/삭제는 관리자만 허용
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // 즐겨찾기 관련 API는 로그인 필요
                        .requestMatchers("/api/favorites/**").authenticated()
                        .requestMatchers("/api/favorite-groups/**").authenticated()

                        // 사용자 조회/수정/삭제도 로그인 필요
                        .requestMatchers("/api/users/**").authenticated()

                        // 나머지 요청은 로그인 필요
                        .anyRequest().authenticated()
                );
        // JWT 인증 필터 등록
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
}