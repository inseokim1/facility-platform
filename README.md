## 작업 내용
- 시설 전체 조회 API에 페이징 기능 적용
- page, size 요청 파라미터 추가
- PageRequest를 사용해 Pageable 객체 생성
- Facility Entity를 FacilityResponse DTO로 변환
- 시설 목록을 id 기준 최신 등록순으로 정렬

## 구현 API
- GET /api/facilities?page=0&size=3

## 테스트
- page=0, size=3 요청 시 첫 페이지 데이터 3개 조회 확인
- page=1, size=3 요청 시 다음 페이지 데이터 조회 확인
- totalElements, totalPages, first, last 값 확인
- id 기준 내림차순 정렬 확인

## 테스트 결과

### page=0, size=3

- 시설 목록 페이징 조회 확인
- 총 데이터 수(totalElements) 조회 확인
- 총 페이지 수(totalPages) 조회 확인
- id 기준 내림차순 정렬 확인
- 첫 페이지(first=true) 확인

### 결과
<img width="718" height="1315" alt="image" src="https://github.com/user-attachments/assets/b798fa40-3f08-46bf-b4f3-c2fc85bff873" />

