## 작업 내용
- Facility Entity 생성
- Facility DTO 생성
  - FacilityCreateRequest
  - FacilityResponse
- FacilityRepository 생성
- FacilityService 생성
- FacilityController 생성
- 시설 등록, 전체 조회, 단건 조회, 수정, 삭제 API 구현
- Facility와 Category 연관관계 연결

## 구현 API
- POST /api/facilities
- GET /api/facilities
- GET /api/facilities/{id}
- PUT /api/facilities/{id}
- DELETE /api/facilities/{id}

## 테스트
- Postman으로 시설 등록 확인
- Postman으로 시설 전체 조회 확인
- Postman으로 시설 단건 조회 확인
- Postman으로 시설 수정 확인
- Postman으로 시설 삭제 확인
- 존재하지 않는 시설 요청 시 "존재하지 않는 시설입니다." 메시지 반환 확인
<img width="764" height="688" alt="facility 추가" src="https://github.com/user-attachments/assets/403e60e7-1f8c-4b78-a8b7-12e82080c06c" />
<img width="714" height="611" alt="facility 수정 기능" src="https://github.com/user-attachments/assets/241c2f6e-2249-49dc-9b27-f790d394b70c" />
<img width="747" height="727" alt="facility 단건조회" src="https://github.com/user-attachments/assets/00231946-1fab-4e98-8bc0-2ee16cb33693" />

<img width="745" height="532" alt="facility 삭제" src="https://github.com/user-attachments/assets/99104813-9e0e-47dc-975c-796482333e5b" />
