package com.project.facility.dto;

import com.project.facility.entity.Favorite;
import lombok.Getter;

// 즐겨찾기 응답 데이터를 담는 DTO
@Getter
public class FavoriteResponse {

    // 즐겨찾기 ID
    private Long id;

    // 사용자 ID
    private Long userId;

    // 시설 ID
    private Long facilityId;

    // 시설명
    private String facilityName;

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.userId = favorite.getUser().getId();
        this.facilityId = favorite.getFacility().getId();
        this.facilityName = favorite.getFacility().getName();
    }
}