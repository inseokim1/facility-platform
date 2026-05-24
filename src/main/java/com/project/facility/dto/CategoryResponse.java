package com.project.facility.dto;

import com.project.facility.entity.Category;
import lombok.Getter;

// 카테고리 응답 데이터를 담는 DTO
@Getter
public class CategoryResponse {

    private Long id;
    private String name;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}