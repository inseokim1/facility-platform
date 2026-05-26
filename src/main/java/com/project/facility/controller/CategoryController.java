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
    // PUT 요청 처리
    // PUT /api/categories/{id}
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryCreateRequest request
    ) {
        return categoryService.updateCategory(id, request);
    }

    // GET 요청 처리 전체 조회
    // GET /api/categories
    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }
    // GET 요청 처리 단건 조회
    // GET /api/categories/{id}
    @GetMapping("/{id}")
    public CategoryResponse getCategory(
            // URL 경로에 있는 id 값을 Long 타입으로 받음
            @PathVariable Long id
    ) {
        // Service에 카테고리 단건 조회 요청
        return categoryService.getCategory(id);
    }
    // DELETE 요청 처리
    // DELETE /api/categories/{id}
    @DeleteMapping("/{id}")
    public String deleteCategory(
            // URL 경로의 id 값을 가져옴
            @PathVariable Long id
    ) {
        // Service에 카테고리 삭제 요청
        categoryService.deleteCategory(id);

        // 삭제 성공 메시지 반환
        return "카테고리 삭제가 완료되었습니다.";
    }

}