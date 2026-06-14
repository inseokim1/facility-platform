package com.project.facility.repository;

import com.project.facility.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FavoriteRepository
        extends JpaRepository<Favorite, Long> {

    // 중복 즐겨찾기 확인
    boolean existsByUser_IdAndFacility_Id(
            Long userId,
            Long facilityId
    );

    // 특정 사용자의 즐겨찾기 목록 조회
    List<Favorite> findByUserId(Long userId);
}