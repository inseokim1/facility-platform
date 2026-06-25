package com.project.facility.controller;

import com.project.facility.dto.FavoriteGroupCreateRequest;
import com.project.facility.dto.FavoriteGroupResponse;
import com.project.facility.dto.FavoriteGroupUpdateRequest;
import com.project.facility.service.FavoriteGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 즐겨찾기 그룹 관련 HTTP 요청을 처리하는 Controller
@RestController

// 기본 URL
// 예: /api/favorite-groups
@RequestMapping("/api/favorite-groups")

// 생성자 주입
@RequiredArgsConstructor
public class FavoriteGroupController {

    // 즐겨찾기 그룹 관련 비즈니스 로직을 처리하는 Service
    private final FavoriteGroupService favoriteGroupService;

    // 즐겨찾기 그룹 생성
    // POST /api/favorite-groups
    @PostMapping
    public FavoriteGroupResponse saveFavoriteGroup(

            // DTO Validation 검사
            @Valid

            // JSON -> FavoriteGroupCreateRequest DTO 변환
            @RequestBody FavoriteGroupCreateRequest request
    ) {
        return favoriteGroupService.saveFavoriteGroup(request);
    }

    // 특정 사용자의 즐겨찾기 그룹 목록 조회
    // GET /api/favorite-groups/users/{userId}
    @GetMapping("/users/{userId}")
    public List<FavoriteGroupResponse> getFavoriteGroupsByUser(

            // URL 경로의 사용자 ID
            @PathVariable Long userId
    ) {
        return favoriteGroupService.getFavoriteGroupsByUser(userId);
    }
    // 즐겨찾기 그룹 삭제
    // DELETE /api/favorite-groups/{groupId}
    @DeleteMapping("/{groupId}")
    public String deleteFavoriteGroup(

            // URL 경로의 즐겨찾기 그룹 ID
            @PathVariable Long groupId
    ) {
        favoriteGroupService.deleteFavoriteGroup(groupId);

        return "즐겨찾기 그룹 삭제가 완료되었습니다.";
    }
    // 즐겨찾기 그룹 이름 수정
    // PUT /api/favorite-groups/{groupId}
    @PutMapping("/{groupId}")
    public FavoriteGroupResponse updateFavoriteGroup(

            // URL 경로의 즐겨찾기 그룹 ID
            @PathVariable Long groupId,

            // DTO Validation 검사
            @Valid

            // JSON -> FavoriteGroupUpdateRequest DTO 변환
            @RequestBody FavoriteGroupUpdateRequest request
    ) {
        return favoriteGroupService.updateFavoriteGroup(groupId, request);
    }
}