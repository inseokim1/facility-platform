package com.project.facility.controller;

import com.project.facility.dto.FacilityCreateRequest;
import com.project.facility.dto.FacilityResponse;
import com.project.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST API 컨트롤러
// 시설 관련 HTTP 요청을 받는 클래스
@RestController

// 이 컨트롤러의 기본 URL 경로
// 예: /api/facilities
@RequestMapping("/api/facilities")

// final 필드를 매개변수로 받는 생성자를 자동 생성
// FacilityService를 생성자 주입으로 받음
@RequiredArgsConstructor
public class FacilityController {

    // 시설 관련 비즈니스 로직을 처리하는 Service
    private final FacilityService facilityService;

    // 시설 등록 요청 처리
    // POST /api/facilities
    @PostMapping
    public FacilityResponse saveFacility(
            // 요청 Body의 JSON 데이터를 FacilityCreateRequest DTO로 변환
            @RequestBody FacilityCreateRequest request
    ) {
        // Service에 시설 저장 요청
        return facilityService.saveFacility(request);
    }


    // GET /api/facilities
    @GetMapping
    public List<FacilityResponse> getFacilities() {
        // Service에 시설 목록 조회 요청
        return facilityService.getFacilities();
    }
    // 시설 단건 조회 요청 처리
    // GET /api/facilities/{id}
    @GetMapping("/{id}")
    public FacilityResponse getFacility(
            // URL 경로에 있는 id 값을 Long 타입으로 받음
            @PathVariable Long id
    ) {
        // Service에 시설 단건 조회 요청
        return facilityService.getFacility(id);
    }
    // 시설 수정 요청 처리
    // PUT /api/facilities/{id}
    @PutMapping("/{id}")
    public FacilityResponse updateFacility(
            // URL 경로에 있는 id 값을 Long 타입으로 받음
            @PathVariable Long id,

            // 요청 Body의 JSON 데이터를 FacilityCreateRequest DTO로 변환
            @RequestBody FacilityCreateRequest request
    ) {
        // Service에 시설 수정 요청
        return facilityService.updateFacility(id, request);
    }// 시설 전체 조회 요청 처리
    // 시설 삭제 요청 처리
    // DELETE /api/facilities/{id}
    @DeleteMapping("/{id}")
    public String deleteFacility(
            // URL 경로에 있는 id 값을 Long 타입으로 받음
            @PathVariable Long id
    ) {
        // Service에 시설 삭제 요청
        facilityService.deleteFacility(id);

        // 삭제 성공 메시지 반환
        return "시설 삭제가 완료되었습니다.";
    }
    // 시설 검색 요청 처리
    // GET /api/facilities/search?keyword=성북
    @GetMapping("/search")
    public List<FacilityResponse> searchFacilities(

            // URL의 query parameter를 받음
            @RequestParam String keyword
    ) {
        // Service에 검색 요청
        return facilityService.searchFacilities(keyword);
    }
    // 카테고리별 시설 검색 요청 처리
    // GET /api/facilities/search/category?categoryId=1
    @GetMapping("/search/category")
    public List<FacilityResponse> searchFacilitiesByCategory(
            // URL의 query parameter에서 categoryId 값을 받음
            @RequestParam Long categoryId
    ) {
        // Service에 카테고리별 시설 조회 요청
        return facilityService.searchFacilitiesByCategory(categoryId);
    }
    // 시설명 + 카테고리 복합 검색 요청 처리
    // GET /api/facilities/search/detail?keyword=성북&categoryId=1
    @GetMapping("/search/detail")
    public List<FacilityResponse> searchFacilitiesByKeywordAndCategory(
            @RequestParam String keyword,
            @RequestParam Long categoryId
    ) {
        return facilityService.searchFacilitiesByKeywordAndCategory(keyword, categoryId);
    }

}