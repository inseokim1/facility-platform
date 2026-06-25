package com.project.facility.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자가 즐겨찾기를 분류하기 위해 만드는 그룹 Entity
// 예: 화장실, 운동시설, 공부장소
@Entity

// 동일 사용자가 같은 이름의 그룹을 중복 생성하지 못하도록 unique 제약 설정
@Table(
        name = "favorite_groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FavoriteGroup {

    // 즐겨찾기 그룹 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 그룹을 생성한 사용자
    // 여러 FavoriteGroup은 하나의 User에 속할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)

    // favorite_groups 테이블의 user_id 컬럼과 User 테이블의 id를 연결
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 사용자가 직접 지정하는 그룹 이름
    // 예: 화장실, 운동시설, 공부장소
    @Column(nullable = false)
    private String name;
}