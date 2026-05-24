package com.project.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 시설명
    private String category;    // 카테고리
    private String address;     // 주소
    private String phone;       // 전화번호

    private Double latitude;    // 위도
    private Double longitude;   // 경도

    private String openTime;    // 운영시간
}