package com.project.facility.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 요청마다 JWT를 검사하는 필터
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Authorization Header 조회
        String authorizationHeader =
                request.getHeader("Authorization");

        // Header가 존재하고 Bearer로 시작하는 경우만 처리
        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {

            // "Bearer " 제거 후 JWT만 추출
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {

                // JWT에서 email, role 추출
                String email =
                        jwtTokenProvider.getEmailFromToken(token);

                String role =
                        jwtTokenProvider.getRoleFromToken(token);

                // Spring Security 권한 형식으로 변환
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                // 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(authority)
                        );

                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}