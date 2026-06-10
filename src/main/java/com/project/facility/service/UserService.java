package com.project.facility.service;

import com.project.facility.dto.UserCreateRequest;
import com.project.facility.dto.UserResponse;
import com.project.facility.entity.User;
import com.project.facility.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 사용자 관련 비즈니스 로직을 처리하는 Service
@Service

// final 필드를 생성자 주입으로 자동 생성
@RequiredArgsConstructor
public class UserService {

    // User DB 접근 객체
    private final UserRepository userRepository;

    // 사용자 등록
    public UserResponse saveUser(UserCreateRequest request) {

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // DTO -> Entity 변환
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRole(request.getRole());

        // DB 저장
        User savedUser = userRepository.save(user);

        // Entity -> Response DTO 변환
        return new UserResponse(savedUser);
    }

    // 사용자 전체 조회
    public List<UserResponse> getUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponse::new)
                .toList();
    }
    // 사용자 단건 조회
    public UserResponse getUser(Long id) {

        // 사용자 조회
        User user = userRepository.findById(id)
                //있으면 반환 없으면 예외 발생
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Entity -> DTO 변환
        return new UserResponse(user);
    }
    // 사용자 수정
    public UserResponse updateUser(
            Long id,
            UserCreateRequest request
    ) {

        // 수정할 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이메일 수정
        user.setEmail(request.getEmail());

        // 비밀번호 수정
        user.setPassword(request.getPassword());

        // 이름 수정
        user.setName(request.getName());

        // 권한 수정
        user.setRole(request.getRole());

        // 수정 내용 저장
        User updatedUser = userRepository.save(user);

        // Entity -> DTO 변환
        return new UserResponse(updatedUser);
    }
    // 사용자 삭제
    public void deleteUser(Long id) {

        // 사용자 존재 여부 확인
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 사용자 삭제
        userRepository.deleteById(id);
    }
}