package com.project.facility.controller;

import com.project.facility.dto.LoginRequest;
import com.project.facility.dto.LoginResponse;
import com.project.facility.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 로그인 API를 처리하는 Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    // 로그인 서비스
    private final LoginService loginService;

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return loginService.login(request);
    }
}