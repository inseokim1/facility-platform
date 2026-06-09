package com.project.facility.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 시설 등록/수정 요청 데이터를 담는 DTO
@Getter
@NoArgsConstructor
public class FacilityCreateRequest {


    // 시설 이름은 비어 있으면 안 됨
    @NotBlank(message = "시설명은 필수입니다.")
    // 시설 이름
    private String name;


    // 시설 주소는 비어 있으면 안 됨
    @NotBlank(message = "주소는 필수입니다.")
    // 시설 주소
    private String address;

    // 시설 전화번호는 비어 있으면 안 됨
    @NotBlank(message = "전화번호는 필수입니다.")
    // 시설 전화번호
    private String phone;

    // 시설 위도는 필수
    @NotNull(message = "위도는 필수입니다.")
    // 시설 위도
    private Double latitude;

    // 시설 경도는 필수
    @NotNull(message = "경도는 필수입니다.")
    // 시설 경도
    private Double longitude;

    // 운영 시간은 비어 있으면 안 됨
    @NotBlank(message = "운영시간은 필수입니다.")
    // 시설 운영 시간
    private String openTime;


    // Category Entity 전체를 받지 않고 id만 받아서 연결함
    // 카테고리 id는 필수 Null 불가
    @NotNull(message = "카테고리 ID는 필수입니다.")
    // 시설이 속할 카테고리 id
    private Long categoryId;
}