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

    // 카테고리 단건 조회 기능
    public CategoryResponse getCategory(Long id) {

        // id로 카테고리를 조회
        // 없으면 IllegalArgumentException 예외 발생
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // Entity를 Response DTO로 변환해서 반환
        return new CategoryResponse(category);
    }

    // 카테고리 수정 기능
    public CategoryResponse updateCategory(Long id, CategoryCreateRequest request) {

        // 수정할 카테고리가 존재하는지 id로 조회
        // 없으면 예외 발생
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 같은 이름의 카테고리가 이미 존재하면 중복 방지
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        // 조회한 Entity의 이름을 요청 값으로 변경
        category.setName(request.getName());

        // 변경된 Entity를 DB에 저장
        Category updatedCategory = categoryRepository.save(category);

        // Entity를 Response DTO로 변환해서 반환
        return new CategoryResponse(updatedCategory);
    }
    // 카테고리 삭제 기능
    public void deleteCategory(Long id) {

        // 삭제할 카테고리가 존재하는지 먼저 확인
        // 존재하지 않으면 예외 발생
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 조회한 카테고리 Entity를 DB에서 삭제
        categoryRepository.delete(category);
    }
}