package com.project.facility.repository;

import com.project.facility.entity.FavoriteGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 즐겨찾기 그룹 DB 접근을 담당하는 Repository
public interface FavoriteGroupRepository
        extends JpaRepository<FavoriteGroup, Long> {

    // 특정 사용자가 만든 즐겨찾기 그룹 목록 조회
    List<FavoriteGroup> findByUser_Id(Long userId);

    // 동일 사용자가 같은 이름의 그룹을 이미 만들었는지 확인
    boolean existsByUser_IdAndName(
            Long userId,
            String name
    );

}
