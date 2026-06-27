package com.project.facility.repository;

import com.project.facility.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// User Entity에 대한 DB 접근을 담당하는 Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 확인
    // 예:
    // existsByEmail("test@test.com")
    // -> true / false 반환
    boolean existsByEmail(String email);

    // 이메일로 사용자 조회
    // 로그인 시 사용
    Optional<User> findByEmail(String email);
}