package com.project.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 이 클래스가 JPA Entity임을 의미
// DB의 facility 테이블과 매핑됨
@Entity
// 모든 필드의 getter 메서드를 Lombok이 자동 생성
@Getter
// 모든 필드의 setter 메서드를 Lombok이 자동 생성
@Setter
// 매개변수가 없는 기본 생성자를 Lombok이 자동 생성
// JPA Entity는 기본 생성자가 필요함
@NoArgsConstructor
public class Facility {

    // 시설 고유 번호, 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // 여러 시설은 하나의 카테고리에 속함
    // 예: 여러 공영주차장이 하나의 "공영주차장" 카테고리에 속할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)

    // facility 테이블에 category_id 컬럼을 만들어 Category와 연결
    @JoinColumn(name = "category_id")
    private Category category;
}