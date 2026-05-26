package com.project.facility.dto;

import com.project.facility.entity.Facility;
import lombok.Getter;

// 시설 응답 데이터를 담는 DTO
@Getter
public class FacilityResponse {

    // 시설 고유 번호
    private Long id;

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

    // 카테고리 id
    private Long categoryId;

    // 카테고리 이름
    private String categoryName;

    public FacilityResponse(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.address = facility.getAddress();
        this.phone = facility.getPhone();
        this.latitude = facility.getLatitude();
        this.longitude = facility.getLongitude();
        this.openTime = facility.getOpenTime();
        this.categoryId = facility.getCategory().getId();
        this.categoryName = facility.getCategory().getName();
    }
}