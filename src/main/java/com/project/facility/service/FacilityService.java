package com.project.facility.service;

import com.project.facility.dto.FacilityCreateRequest;
import com.project.facility.dto.FacilityResponse;
import com.project.facility.entity.Category;
import com.project.facility.entity.Facility;
import com.project.facility.repository.CategoryRepository;
import com.project.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

// 시설 관련 비즈니스 로직을 처리하는 Service 계층
@Service
@RequiredArgsConstructor
public class FacilityService {

    // 시설 DB 접근을 담당하는 Repository
    private final FacilityRepository facilityRepository;

    // 카테고리 DB 접근을 담당하는 Repository
    private final CategoryRepository categoryRepository;

    // 시설 등록 기능
    public FacilityResponse saveFacility(FacilityCreateRequest request) {

        // 요청으로 받은 categoryId에 해당하는 Category 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // Facility Entity 생성
        Facility facility = new Facility();

        // 요청 DTO 값을 Entity에 세팅
        facility.setName(request.getName());
        facility.setAddress(request.getAddress());
        facility.setPhone(request.getPhone());
        facility.setLatitude(request.getLatitude());
        facility.setLongitude(request.getLongitude());
        facility.setOpenTime(request.getOpenTime());

        // 조회한 Category Entity를 Facility에 연결
        facility.setCategory(category);

        // 시설 정보를 DB에 저장
        Facility savedFacility = facilityRepository.save(facility);

        // 저장된 Entity를 Response DTO로 변환해서 반환
        return new FacilityResponse(savedFacility);
    }

    // 시설 전체 조회 기능 + 페이징
    public Page<FacilityResponse> getFacilities(int page, int size) {

        // page, size 값을 기반으로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        // DB에서 해당 페이징 조건에 해당하는 시설 목록 조회
        Page<Facility> facilities = facilityRepository.findAll(pageable);

        // Entity -> Response DTO 변환
        return facilities.map(FacilityResponse::new);
    }

    // 시설 단건 조회 기능
    public FacilityResponse getFacility(Long id) {

        // id로 시설을 조회
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시설입니다."));

        // 조회한 Entity를 Response DTO로 변환해서 반환
        return new FacilityResponse(facility);
    }

    // 시설 수정 기능
    public FacilityResponse updateFacility(Long id, FacilityCreateRequest request) {

        // 수정할 시설 조회
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시설입니다."));

        // 요청으로 받은 categoryId에 해당하는 Category 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 요청 DTO 값으로 기존 Facility Entity 값 수정
        facility.setName(request.getName());
        facility.setAddress(request.getAddress());
        facility.setPhone(request.getPhone());
        facility.setLatitude(request.getLatitude());
        facility.setLongitude(request.getLongitude());
        facility.setOpenTime(request.getOpenTime());

        // 수정된 카테고리 연결
        facility.setCategory(category);

        // 변경된 Entity를 DB에 저장
        Facility updatedFacility = facilityRepository.save(facility);

        // Entity를 Response DTO로 변환해서 반환
        return new FacilityResponse(updatedFacility);
    }

    // 시설 삭제 기능
    public void deleteFacility(Long id) {

        // 삭제할 시설 조회
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시설입니다."));

        // 조회한 시설 Entity를 DB에서 삭제
        facilityRepository.delete(facility);
    }

    // 시설명 부분 검색 기능
    public List<FacilityResponse> searchFacilities(String keyword) {

        // 시설명에 keyword가 포함된 시설 목록 조회
        // SQL: WHERE name LIKE '%keyword%'
        return facilityRepository.findByNameContaining(keyword)
                .stream()
                .map(FacilityResponse::new)
                .toList();
    }

    // 시설명 정확 일치 검색 기능
    public List<FacilityResponse> searchFacilitiesByExactName(String name) {

        // 시설명과 정확히 일치하는 시설 목록 조회
        // SQL: WHERE name = ?
        // name 인덱스가 있으면 B-Tree 인덱스를 효과적으로 사용할 수 있음
        return facilityRepository.findByName(name)
                .stream()
                .map(FacilityResponse::new)
                .toList();
    }

    // 카테고리별 시설 조회 기능
    public List<FacilityResponse> searchFacilitiesByCategory(Long categoryId) {

        // categoryId에 해당하는 시설 목록 조회
        return facilityRepository.findByCategoryId(categoryId)
                .stream()
                .map(FacilityResponse::new)
                .toList();
    }

    // 시설명 + 카테고리 복합 검색 기능
    public List<FacilityResponse> searchFacilitiesByKeywordAndCategory(String keyword, Long categoryId) {

        // 시설명에 keyword가 포함되고 categoryId가 일치하는 시설 목록 조회
        return facilityRepository.findByNameContainingAndCategoryId(keyword, categoryId)
                .stream()
                .map(FacilityResponse::new)
                .toList();
    }
}