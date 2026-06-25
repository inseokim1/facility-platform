package com.project.facility.service;

import com.project.facility.dto.FavoriteGroupCreateRequest;
import com.project.facility.dto.FavoriteGroupResponse;
import com.project.facility.dto.FavoriteGroupUpdateRequest;
import com.project.facility.entity.FavoriteGroup;
import com.project.facility.entity.User;
import com.project.facility.repository.FavoriteGroupRepository;
import com.project.facility.repository.FavoriteRepository;
import com.project.facility.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 즐겨찾기 그룹 관련 비즈니스 로직을 처리하는 Service
@Service
@RequiredArgsConstructor
public class FavoriteGroupService {

    // 즐겨찾기 그룹 DB 접근 객체
    private final FavoriteGroupRepository favoriteGroupRepository;
    private final FavoriteRepository favoriteRepository;


    // 사용자 DB 접근 객체
    private final UserRepository userRepository;

    // 즐겨찾기 그룹 생성
    public FavoriteGroupResponse saveFavoriteGroup(
            FavoriteGroupCreateRequest request
    ) {

        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 동일 사용자의 그룹명 중복 확인
        if (favoriteGroupRepository.existsByUser_IdAndName(
                request.getUserId(),
                request.getName()
        )) {
            throw new IllegalArgumentException("이미 존재하는 그룹명입니다.");
        }

        // FavoriteGroup Entity 생성
        FavoriteGroup favoriteGroup = new FavoriteGroup();
        favoriteGroup.setUser(user);
        favoriteGroup.setName(request.getName());

        // DB 저장
        FavoriteGroup savedGroup =
                favoriteGroupRepository.save(favoriteGroup);

        // Entity -> Response DTO 변환
        return new FavoriteGroupResponse(savedGroup);
    }

    // 특정 사용자의 즐겨찾기 그룹 목록 조회
    public List<FavoriteGroupResponse> getFavoriteGroupsByUser(
            Long userId
    ) {

        // 사용자 존재 여부 확인
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 사용자 ID 기준으로 즐겨찾기 그룹 목록 조회
        List<FavoriteGroup> groups =
                favoriteGroupRepository.findByUser_Id(userId);

        // Entity -> Response DTO 변환
        return groups.stream()
                .map(FavoriteGroupResponse::new)
                .toList();
    }
    // 즐겨찾기 그룹 삭제
    @Transactional
    public void deleteFavoriteGroup(Long groupId) {

        // 그룹 존재 여부 확인
        if (!favoriteGroupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("즐겨찾기 그룹을 찾을 수 없습니다.");
        }

        // 해당 그룹에 속한 즐겨찾기 먼저 삭제
        favoriteRepository.deleteByFavoriteGroup_Id(groupId);

        // 즐겨찾기 그룹 삭제
        favoriteGroupRepository.deleteById(groupId);
    }
    @Transactional
    public FavoriteGroupResponse updateFavoriteGroup(
            Long groupId,
            FavoriteGroupUpdateRequest request
    ) {

        FavoriteGroup favoriteGroup =
                favoriteGroupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("즐겨찾기 그룹을 찾을 수 없습니다."));

        // 동일 사용자 그룹명 중복 검사
        if (!favoriteGroup.getName().equals(request.getName())
                        &&
                        favoriteGroupRepository.existsByUser_IdAndName(
                                favoriteGroup.getUser().getId(),
                                request.getName()
                        )
        ) {
            throw new IllegalArgumentException("이미 존재하는 그룹명입니다.");
        }

        favoriteGroup.setName(request.getName());

        return new FavoriteGroupResponse(favoriteGroup);
    }
}