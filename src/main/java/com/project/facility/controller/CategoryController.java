package com.project.facility.controller;

import com.project.facility.dto.CategoryCreateRequest;
import com.project.facility.dto.CategoryResponse;
import com.project.facility.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API 컨트롤러라는 의미
// 메서드 반환값이 JSON 형태로 응답됨
@RestController

// 이 컨트롤러의 기본 URL 경로
// 예: /api/categories
@RequestMapping("/api/categories")

// final 필드를 매개변수로 받는 생성자를 자동 생성
// CategoryService를 생성자 주입으로 주입받음
@RequiredArgsConstructor
public class CategoryController {

    // 비즈니스 로직을 처리하는 Service 계층
    private final CategoryService categoryService;

    // POST 요청 처리
    // POST /api/categories
    @PostMapping
    public CategoryResponse saveCategory(
            // 요청 Body의 JSON 데이터를 CategoryCreateRequest DTO로 변환
            @RequestBody CategoryCreateRequest request
    ) {
        // Service에 카테고리 저장 요청
        return categoryService.saveCategory(request);
    }

    // GET 요청 처리
    // GET /api/categories
    @GetMapping
    public List<CategoryResponse> getCategories() {

        // Service에서 카테고리 목록 조회
        return categoryService.getCategories();
    }
}