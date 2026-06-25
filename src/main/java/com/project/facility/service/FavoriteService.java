package com.project.facility.service;

import com.project.facility.dto.FavoriteCreateRequest;
import com.project.facility.dto.FavoriteMoveGroupRequest;
import com.project.facility.dto.FavoriteResponse;
import com.project.facility.entity.Facility;
import com.project.facility.entity.Favorite;
import com.project.facility.entity.User;
import com.project.facility.repository.FacilityRepository;
import com.project.facility.repository.FavoriteRepository;
import com.project.facility.repository.UserRepository;
import com.project.facility.entity.FavoriteGroup;
import com.project.facility.repository.FavoriteGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteGroupRepository favoriteGroupRepository;

    // 즐겨찾기 등록
    public FavoriteResponse saveFavorite(FavoriteCreateRequest request) {

        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 시설 조회
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

        // 즐겨찾기 그룹 조회
        FavoriteGroup favoriteGroup = favoriteGroupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new IllegalArgumentException("즐겨찾기 그룹을 찾을 수 없습니다."));

        // 중복 즐겨찾기 확인
        if (favoriteRepository.existsByUser_IdAndFacility_Id(
                request.getUserId(),
                request.getFacilityId()
        )) {
            throw new IllegalArgumentException("이미 추가한 시설입니다.");
        }

        // Favorite Entity 생성
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setFacility(facility);
        favorite.setFavoriteGroup(favoriteGroup);

        // DB 저장
        Favorite savedFavorite = favoriteRepository.save(favorite);

        // Entity -> Response DTO 변환
        return new FavoriteResponse(savedFavorite);
    }

    // 특정 사용자의 즐겨찾기 목록 조회
    public List<FavoriteResponse> getFavoritesByUser(Long userId) {

        // 사용자 존재 여부 확인
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // userId 기준으로 즐겨찾기 목록 조회
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        // Entity -> DTO 변환
        return favorites.stream()
                .map(FavoriteResponse::new)
                .toList();
    }
    // 즐겨찾기 삭제
    public void deleteFavorite(Long favoriteId) {

        // 즐겨찾기 존재 여부 확인
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new IllegalArgumentException("즐겨찾기를 찾을 수 없습니다.");
        }

        // 즐겨찾기 삭제
        favoriteRepository.deleteById(favoriteId);
    }
    // 사용자 즐겨찾기 필터 조회
    public List<FavoriteResponse> getFavoritesWithFilter(
            Long userId,
            String region,
            Long categoryId
    ) {

        List<Favorite> favorites;

        // 지역 + 카테고리
        if (region != null && categoryId != null) {

            favorites =
                    favoriteRepository
                            .findByUser_IdAndFacility_AddressContainingAndFacility_Category_Id(
                                    userId,
                                    region,
                                    categoryId
                            );

        }
        // 지역만
        else if (region != null) {

            favorites =
                    favoriteRepository
                            .findByUser_IdAndFacility_AddressContaining(
                                    userId,
                                    region
                            );

        }
        // 카테고리만
        else if (categoryId != null) {

            favorites =
                    favoriteRepository
                            .findByUser_IdAndFacility_Category_Id(
                                    userId,
                                    categoryId
                            );

        }
        else {
            throw new IllegalArgumentException(
                    "지역 또는 카테고리 조건이 필요합니다."
            );
        }

        return favorites.stream()
                .map(FavoriteResponse::new)
                .toList();
    }
    // 특정 그룹에 속한 즐겨찾기 목록 조회
    public List<FavoriteResponse> getFavoritesByGroup(Long groupId) {

        // 그룹에 속한 즐겨찾기 목록 조회
        List<Favorite> favorites =
                favoriteRepository.findByFavoriteGroup_Id(groupId);

        // Entity -> DTO 변환
        return favorites.stream()
                .map(FavoriteResponse::new)
                .toList();
    }
    // 즐겨찾기를 다른 그룹으로 이동
    @Transactional
    public FavoriteResponse moveFavoriteGroup(
            Long favoriteId,
            FavoriteMoveGroupRequest request

    ) {

        // 즐겨찾기 조회
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() ->
                        new IllegalArgumentException("즐겨찾기를 찾을 수 없습니다."));

        // 이동할 그룹 조회
        FavoriteGroup targetGroup =
                favoriteGroupRepository.findById(request.getGroupId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("즐겨찾기 그룹을 찾을 수 없습니다."));

        // 즐겨찾기의 그룹 변경
        favorite.setFavoriteGroup(targetGroup);

        // 변경된 즐겨찾기 응답 반환
        return new FavoriteResponse(favorite);
    }

}