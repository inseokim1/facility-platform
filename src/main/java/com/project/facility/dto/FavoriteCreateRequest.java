package com.project.facility.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 즐겨찾기 등록 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class FavoriteCreateRequest {

    // 즐겨찾기를 등록할 사용자 ID
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    // 즐겨찾기할 시설 ID
    @NotNull(message = "시설 ID는 필수입니다.")
    private Long facilityId;
}