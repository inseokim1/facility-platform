package com.project.facility.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 카테고리 생성 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class CategoryCreateRequest {

    // 사용자가 등록할 카테고리 이름
    private String name;
}