package com.project.facility.dto;

import com.project.facility.entity.FavoriteGroup;
import lombok.Getter;

// 즐겨찾기 그룹 응답 데이터를 담는 DTO
@Getter
public class FavoriteGroupResponse {

    // 즐겨찾기 그룹 ID
    private Long id;

    // 그룹을 생성한 사용자 ID
    private Long userId;

    // 즐겨찾기 그룹 이름
    private String name;

    public FavoriteGroupResponse(FavoriteGroup favoriteGroup) {
        this.id = favoriteGroup.getId();
        this.userId = favoriteGroup.getUser().getId();
        this.name = favoriteGroup.getName();
    }
}