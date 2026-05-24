package com.project.facility.repository;

import com.project.facility.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // name 값이 이미 존재하는지 확인하는 메서드
    // Spring Data JPA가 메서드 이름을 보고 자동으로 쿼리를 만들어줌
    boolean existsByName(String name);
}