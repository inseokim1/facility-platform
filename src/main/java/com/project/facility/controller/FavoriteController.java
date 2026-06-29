package com.project.facility.controller;

import com.project.facility.dto.FavoriteCreateRequest;
import com.project.facility.dto.FavoriteMoveGroupRequest;
import com.project.facility.dto.FavoriteResponse;
import com.project.facility.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// 즐겨찾기 관련 HTTP 요청을 처리하는 Controller
@RestController

// 기본 URL
// 예: /api/favorites
@RequestMapping("/api/favorites")

// 생성자 주입
@RequiredArgsConstructor
public class FavoriteController {

    // 즐겨찾기 관련 비즈니스 로직을 처리하는 Service
    private final FavoriteService favoriteService;

    // 즐겨찾기 등록
    // POST /api/favorites
    @PostMapping
    public FavoriteResponse saveFavorite(

            // DTO Validation 검사
            @Valid

            // JSON -> FavoriteCreateRequest DTO 변환
            @RequestBody FavoriteCreateRequest request
    ) {
        return favoriteService.saveFavorite(request);
    }

    // 특정 사용자의 즐겨찾기 목록 조회
    // GET /api/favorites/users/{userId}
    @GetMapping("/users/{userId}")
    public List<FavoriteResponse> getFavoritesByUser(

            // URL 경로의 사용자 ID
            @PathVariable Long userId
    ) {
        return favoriteService.getFavoritesByUser(userId);
    }

    // 즐겨찾기 삭제
    // DELETE /api/favorites/{favoriteId}
    @DeleteMapping("/{favoriteId}")
    public String deleteFavorite(

            // URL 경로의 즐겨찾기 ID
            @PathVariable Long favoriteId
    ) {
        // Service에 즐겨찾기 삭제 요청
        favoriteService.deleteFavorite(favoriteId);

        // 삭제 성공 메시지 반환
        return "즐겨찾기 삭제가 완료되었습니다.";
    }
    // 즐겨찾기 필터 조회
    @GetMapping("/users/{userId}/filter")
    public List<FavoriteResponse> getFavoritesWithFilter(

            // 사용자 ID
            @PathVariable Long userId,

            // 지역 (선택)
            @RequestParam(required = false)
            String region,

            // 카테고리 ID (선택)
            @RequestParam(required = false)
            Long categoryId
    ) {
        return favoriteService.getFavoritesWithFilter(
                userId,
                region,
                categoryId
        );
    }
    // 특정 그룹에 속한 즐겨찾기 목록 조회
    // GET /api/favorites/groups/{groupId}
    @GetMapping("/groups/{groupId}")
    public List<FavoriteResponse> getFavoritesByGroup(

            // URL 경로의 그룹 ID
            @PathVariable Long groupId
    ) {
        return favoriteService.getFavoritesByGroup(groupId);
    }
    // 즐겨찾기를 다른 그룹으로 이동
    // PUT /api/favorites/{favoriteId}/group
    @PutMapping("/{favoriteId}/group")
    public FavoriteResponse moveFavoriteGroup(

            // URL 경로의 즐겨찾기 ID
            @PathVariable Long favoriteId,

            // DTO Validation 검사
            @Valid

            // JSON -> FavoriteMoveGroupRequest DTO 변환
            @RequestBody FavoriteMoveGroupRequest request
    ) {
        return favoriteService.moveFavoriteGroup(
                favoriteId,
                request
        );
    }
    // 현재 로그인한 사용자의 즐겨찾기 조회
    @GetMapping("/me")
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites() {

        // Service를 호출하여 현재 로그인한 사용자의 즐겨찾기 목록 조회
        List<FavoriteResponse> favorites = favoriteService.getMyFavorites();

        // 조회 결과를 200 OK와 함께 반환
        return ResponseEntity.ok(favorites);
    }
}