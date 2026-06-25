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
    // 특정 사용자의 즐겨찾기 중 지역명으로 필터링
    // Favorite은 Facility와 연관관계가 있으므로
    // Facility의 address 필드를 기준으로 검색
    List<Favorite> findByUser_IdAndFacility_AddressContaining(
            Long userId,
            String region
    );

    // 특정 사용자의 즐겨찾기 중 카테고리 ID로 필터링
    // Facility의 Category id를 기준으로 검색
    List<Favorite> findByUser_IdAndFacility_Category_Id(
            Long userId,
            Long categoryId
    );

    // 특정 사용자의 즐겨찾기 중 지역명 + 카테고리 ID로 필터링
    List<Favorite> findByUser_IdAndFacility_AddressContainingAndFacility_Category_Id(
            Long userId,
            String region,
            Long categoryId
    );
    // 특정 그룹에 속한 즐겨찾기 목록 조회
    List<Favorite> findByFavoriteGroup_Id(Long groupId);

    // 특정 그룹에 속한 즐겨찾기 전체 삭제
    void deleteByFavoriteGroup_Id(Long groupId);
}