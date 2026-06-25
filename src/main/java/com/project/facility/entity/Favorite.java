package com.project.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자와 시설의 즐겨찾기 관계를 저장하는 Entity
@Entity

// 동일 사용자가 같은 시설을 중복 즐겨찾기하지 못하도록 unique 제약 설정
@Table(
        name = "favorites",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "facility_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    // 즐겨찾기 고유 ID
    // JPA 매핑 단순성을 위해 별도 PK 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 즐겨찾기를 등록한 사용자
    // 여러 Favorite은 하나의 User를 참조할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)

    // favorites 테이블의 user_id 컬럼과 User 테이블의 id를 연결
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 즐겨찾기 대상 시설
    // 여러 Favorite은 하나의 Facility를 참조할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)

    // favorites 테이블의 facility_id 컬럼과 Facility 테이블의 id를 연결
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    // 즐겨찾기가 속한 그룹
    // 예: 운동시설, 화장실, 공부장소
    @ManyToOne(fetch = FetchType.LAZY)

    // favorites 테이블의 group_id 컬럼과 favorite_groups 테이블의 id를 연결
    @JoinColumn(name = "group_id", nullable = false)
    private FavoriteGroup favoriteGroup;
}