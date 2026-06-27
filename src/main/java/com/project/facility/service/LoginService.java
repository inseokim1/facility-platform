package com.project.facility.service;

import com.project.facility.dto.LoginRequest;
import com.project.facility.dto.LoginResponse;
import com.project.facility.entity.User;
import com.project.facility.repository.UserRepository;
import com.project.facility.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// 로그인 관련 비즈니스 로직을 처리하는 Service
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    // 로그인
    public LoginResponse login(LoginRequest request) {

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 입력 비밀번호와 DB에 저장된 BCrypt 해시 비교
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성
        String token = jwtTokenProvider.createToken(user);

        // 로그인 성공 응답 반환
        return new LoginResponse(user, token);
    }
}