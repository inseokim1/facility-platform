package com.project.facility.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 즐겨찾기 그룹 수정 요청 DTO
@Getter
@NoArgsConstructor
public class FavoriteGroupUpdateRequest {

    // 수정할 그룹명
    @NotBlank(message = "그룹명은 필수입니다.")
    private String name;
}