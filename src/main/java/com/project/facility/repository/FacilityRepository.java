package com.project.facility.repository;

import com.project.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// Facility Entity에 대한 DB 접근을 담당하는 Repository
// JpaRepository를 상속받아 기본 CRUD 기능을 자동으로 사용할 수 있음
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // 시설명에 특정 문자열이 포함된 시설 검색
    List<Facility> findByNameContaining(String keyword);
    // 특정 카테고리에 속한 시설 목록 조회
    List<Facility> findByCategoryId(Long categoryId);
    // 시설명에 keyword가 포함되고, 특정 카테고리에 속한 시설 목록 조회
    List<Facility> findByNameContainingAndCategoryId(String keyword, Long categoryId);
}