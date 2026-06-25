package com.project.facility.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 즐겨찾기를 다른 그룹으로 이동하는 요청 DTO
@Getter
@NoArgsConstructor
public class FavoriteMoveGroupRequest {

    // 이동할 대상 그룹 ID
    @NotNull(message = "이동할 그룹 ID는 필수입니다.")
    private Long groupId;
}