package com.project.facility.controller;

import com.project.facility.dto.UserCreateRequest;
import com.project.facility.dto.UserResponse;
import com.project.facility.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 사용자 관련 HTTP 요청을 처리하는 Controller
@RestController

// 기본 URL
@RequestMapping("/api/users")

// 생성자 주입
@RequiredArgsConstructor
public class UserController {

    // 사용자 서비스
    private final UserService userService;

    // 사용자 등록
    // POST /api/users
    @PostMapping
    public UserResponse saveUser(

            // DTO Validation 검사
            @Valid

            // JSON -> DTO 변환
            @RequestBody UserCreateRequest request
    ) {
        return userService.saveUser(request);
    }

    // 사용자 전체 조회
    // GET /api/users
    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    // 사용자 단건 조회
    // GET /api/users/{id}
    @GetMapping("/{id}")
    public UserResponse getUser(

            // URL 경로 값 받기
            @PathVariable Long id
    ) {
        return userService.getUser(id);
    }
    // 사용자 수정
    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public UserResponse updateUser(

            // URL 경로의 사용자 ID
            @PathVariable Long id,
            // DTO Validation 검사
            @Valid
            // JSON -> DTO 변환
            @RequestBody UserCreateRequest request
    ) {
        return userService.updateUser(id, request);
    }
    // 사용자 삭제
    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public String deleteUser(

            // URL 경로의 사용자 ID
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return "사용자 삭제가 완료되었습니다.";
    }
}