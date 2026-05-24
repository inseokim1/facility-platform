package com.project.facility.service;

import com.project.facility.entity.Facility;
import com.project.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    // 시설 저장
    public Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    // 전체 조회
    public List<Facility> getFacilities() {
        return facilityRepository.findAll();
    }
}