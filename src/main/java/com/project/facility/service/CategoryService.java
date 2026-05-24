package com.project.facility.service;

import com.project.facility.dto.CategoryCreateRequest;
import com.project.facility.dto.CategoryResponse;
import com.project.facility.entity.Category;
import com.project.facility.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 이 클래스가 비즈니스 로직을 담당하는 Service 계층이라는 의미
@Service

// final 필드를 매개변수로 받는 생성자를 자동 생성
// 즉 CategoryRepository를 생성자 주입 방식으로 주입받게 해줌
@RequiredArgsConstructor
public class CategoryService {

    // DB 접근을 담당하는 Repository
    private final CategoryRepository categoryRepository;

    // 카테고리 저장 기능
    // Controller에서 Entity가 아니라 CategoryCreateRequest DTO를 받아서 넘겨줌
    public CategoryResponse saveCategory(CategoryCreateRequest request) {

        // 같은 이름의 카테고리가 이미 존재하는지 확인
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }


        // DTO로 받은 데이터를 Entity로 변환
        // Entity는 DB에 저장될 객체
        Category category = new Category();

        // 요청 DTO에 들어있는 name 값을 Entity에 세팅
        category.setName(request.getName());

        // Repository의 save()를 통해 DB에 INSERT
        Category savedCategory = categoryRepository.save(category);

        // 저장된 Entity를 그대로 반환하지 않고 Response DTO로 변환해서 반환
        return new CategoryResponse(savedCategory);
    }

    // 카테고리 전체 조회 기능
    public List<CategoryResponse> getCategories() {

        // category 테이블의 전체 데이터를 조회
        return categoryRepository.findAll()

                // List<Category>를 Stream으로 변환
                .stream()

                // Category Entity 하나하나를 CategoryResponse DTO로 변환
                .map(CategoryResponse::new)

                // 변환된 DTO들을 다시 List로 모음
                .toList();
    }
}