package com.project.facility.repository;

import com.project.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

// Facility Entity에 대한 DB 접근을 담당하는 Repository
// JpaRepository를 상속받아 기본 CRUD 기능을 자동으로 사용할 수 있음
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}