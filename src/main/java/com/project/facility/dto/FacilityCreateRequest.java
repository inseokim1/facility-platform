package com.project.facility.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 시설 등록/수정 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class FacilityCreateRequest {

    // 시설 이름
    private String name;

    // 시설 주소
    private String address;

    // 시설 전화번호
    private String phone;

    // 시설 위도
    private Double latitude;

    // 시설 경도
    private Double longitude;

    // 시설 운영 시간
    private String openTime;

    // 시설이 속할 카테고리 id
    // Category Entity 전체를 받지 않고 id만 받아서 연결함
    private Long categoryId;
}