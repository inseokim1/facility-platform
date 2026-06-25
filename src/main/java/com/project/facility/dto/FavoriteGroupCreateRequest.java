package com.project.facility.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 즐겨찾기 그룹 생성 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class FavoriteGroupCreateRequest {

    // 그룹을 생성할 사용자 ID
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    // 즐겨찾기 그룹 이름
    // 예: 화장실, 운동시설, 공부장소
    @NotBlank(message = "그룹명은 필수입니다.")
    private String name;
}