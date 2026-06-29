## 작업 내용
- 시설명 검색 기능 추가
- 카테고리별 시설 조회 기능 추가
- 시설명 + 카테고리 복합 검색 기능 추가
- FacilityRepository에 검색용 Query Method 추가

## 구현 API
- GET /api/facilities/search?keyword=성북
- GET /api/facilities/search/category?categoryId=1
- GET /api/facilities/search/detail?keyword=성북&categoryId=1

## 테스트
- Postman으로 시설명 검색 정상 조회 확인
- Postman으로 카테고리별 시설 조회 확인
- Postman으로 복합 검색 정상 조회 확인

시설 검색 기능

<img width="753" height="692" alt="facility 시설검색 기능" src="https://github.com/user-attachments/assets/fe504fea-4fb5-442c-9c42-71c37b7f4811" />

시설 복합 검색 기능 

<img width="767" height="760" alt="facility 복합 검색 구현" src="https://github.com/user-attachments/assets/5393aaaa-2a73-46a0-b009-9f08ef4fc54b" />


