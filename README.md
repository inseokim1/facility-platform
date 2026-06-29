# 공공데이터 기반 지역 생활 편의시설 통합 검색 플랫폼

## 1. 프로젝트 개요

공공데이터를 활용하여 지역 생활 편의시설 정보를 조회, 검색, 즐겨찾기할 수 있는 Spring Boot 기반 REST API 프로젝트입니다.

단순 CRUD 구현에 그치지 않고, 실제 서비스에서 자주 발생할 수 있는 검색 성능 문제를 해결하기 위해 약 11만 건 이상의 공공시설 데이터를 MySQL에 적재하고, 인덱스 적용 전후의 실행 계획과 API 응답 시간을 비교했습니다.

## 2. 주요 성과

| 항목            |     Before |             After | 개선 내용              |
| ------------- | ---------: | ----------------: | ------------------ |
| 시설명 정확 검색 API |       52ms |              12ms | 약 76.9% 개선         |
| 시설명 포함 검색 API |      397ms |              54ms | 약 86.4% 개선         |
| 실행 방식         | Table Scan |      Index Lookup | Full Table Scan 제거 |
| 사용 인덱스        |         없음 | idx_facility_name | B-Tree 인덱스 적용      |
| DB Cost       |      11666 |              0.35 | 실행 비용 감소           |
| 예상 조회 Rows    |      11385 |                 1 | 조회 대상 감소           |

## 3. 기술 스택

| 구분              | 기술                   |
| --------------- | -------------------- |
| Language        | Java 17              |
| Framework       | Spring Boot          |
| ORM             | Spring Data JPA      |
| Database        | MySQL                |
| Security        | Spring Security, JWT |
| Build Tool      | Gradle               |
| API Test        | Postman              |
| Version Control | Git, GitHub          |

## 4. 시스템 구조

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
MySQL
```

본 프로젝트는 계층형 아키텍처를 기반으로 Controller, Service, Repository 역할을 분리했습니다.

* Controller: HTTP 요청 처리
* Service: 비즈니스 로직 처리
* Repository: JPA 기반 데이터 접근
* Entity/DTO 분리: Entity 직접 노출 방지
* Global Exception Handler: 공통 예외 응답 처리

## 5. ERD

<img width="1536" height="1024" alt="ChatGPT Image 2026년 5월 14일 오후 07_56_12" src="https://github.com/user-attachments/assets/87dd2a9e-fc2a-41a9-9675-2d6d9f0c59b7" />

주요 테이블은 다음과 같습니다.

| 테이블             | 설명           |
| --------------- | ------------ |
| users           | 사용자 정보       |
| facilities      | 공공시설 정보      |
| categories      | 시설 카테고리      |
| favorites       | 즐겨찾기         |
| favorite_groups | 즐겨찾기 그룹      |
| reviews         | 리뷰           |
| search_logs     | 검색 로그        |
| api_sync_logs   | 공공데이터 동기화 로그 |

## 6. 구현 기능

### 6.1 사용자 기능

* 회원가입
* 로그인
* JWT 발급
* 현재 로그인 사용자 조회
* 사용자 권한 기반 접근 제어

### 6.2 시설 기능

* 시설 등록
* 시설 조회
* 시설 단건 조회
* 시설 수정
* 시설 삭제
* 시설 페이징 조회

### 6.3 검색 기능

* 시설명 포함 검색
* 시설명 정확 일치 검색
* 카테고리별 시설 조회
* 시설명 + 카테고리 복합 검색

### 6.4 즐겨찾기 기능

* 즐겨찾기 등록
* 즐겨찾기 조회
* 로그인 사용자 기준 즐겨찾기 조회
* 즐겨찾기 그룹 생성
* 즐겨찾기 그룹 수정
* 즐겨찾기 그룹 삭제
* 즐겨찾기 그룹 이동

### 6.5 리뷰 기능

* 시설 리뷰 등록
* 시설 리뷰 조회
* 사용자별 리뷰 관리

## 7. 공공데이터 적재

공공시설 CSV 데이터를 전처리하여 MySQL `facility` 테이블에 적재했습니다.

적재 데이터는 다음과 같습니다.

| 카테고리  |  데이터 수 |
| ----- | -----: |
| 공중화장실 | 53,455 |
| 체육시설  | 40,921 |
| 공영주차장 | 17,766 |
| 도서관   |  3,405 |
| 공연장   |  1,734 |

총 데이터 수는 약 117,281건입니다.

<img width="380" height="320" alt="공공시설 총 데이터건수" src="https://github.com/user-attachments/assets/1f72c49c-d980-450d-b82f-1d54f20296a2" />

<img width="376" height="376" alt="공공시설 컬럼 별 데이터 수" src="https://github.com/user-attachments/assets/00fd946d-1825-43ea-9e03-c1c7e61e498b" />


## 8. 검색 성능 개선

### 8.1 문제 상황

초기 시설명 검색은 `name` 컬럼에 인덱스가 없어 MySQL이 전체 테이블을 순차적으로 조회하는 Table Scan을 수행했습니다.

검색 대상 데이터가 약 11만 건 이상으로 증가하면서 검색 API 응답 시간이 증가할 수 있다고 판단했고, 실행 계획을 분석하여 인덱스를 적용했습니다.

### 8.2 Before: 인덱스 적용 전

실행 SQL:

```sql
EXPLAIN
SELECT *
FROM facility
WHERE name = '서울주차장';
```

결과:

```text
type = ALL
key = NULL
rows = 113854
Extra = Using where
```

의미:

```text
시설명 검색 시 인덱스를 사용하지 못하고 전체 테이블을 스캔함
```

<img width="994" height="563" alt="BEFORE 1" src="https://github.com/user-attachments/assets/7d724f02-5894-429b-9e00-8331c5498d5e" />

실행 분석 SQL:

```sql
EXPLAIN ANALYZE
SELECT *
FROM facility
WHERE name = '서울주차장';
```

결과:

```text
Table Scan
cost = 11666
rows = 11385
actual time = 약 47.8ms
```

<img width="993" height="570" alt="BEFORE 2" src="https://github.com/user-attachments/assets/c17e4761-2eb3-4c9e-b373-92245d25a1e5" />

### 8.3 Index 생성

시설명 정확 검색 성능 개선을 위해 `name` 컬럼에 인덱스를 생성했습니다.

```sql
CREATE INDEX idx_facility_name
ON facility(name);
```

<img width="1013" height="580" alt="INDEX CREATE" src="https://github.com/user-attachments/assets/50e9e55a-ff93-4e86-b18f-0109f00737a0" />

### 8.4 After: 인덱스 적용 후

동일한 SQL을 다시 실행했습니다.

```sql
EXPLAIN
SELECT *
FROM facility
WHERE name = '서울주차장';
```

결과:

```text
type = ref
key = idx_facility_name
rows = 1
```

의미:

```text
Table Scan이 제거되고 idx_facility_name 인덱스를 사용하여 Index Lookup 수행
```

<img width="992" height="564" alt="AFTER 1" src="https://github.com/user-attachments/assets/c288f708-7b90-4eb7-8518-4abbec393c8b" />
실행 분석 SQL:

```sql
EXPLAIN ANALYZE
SELECT *
FROM facility
WHERE name = '서울주차장';
```

결과:

```text
Index Lookup
cost = 0.35
rows = 1
actual time = 약 0.0428ms
```

<img width="1012" height="572" alt="AFTER 2" src="https://github.com/user-attachments/assets/731a056f-3055-4e62-94d4-ddad988b5c47" />
## 9. API 응답 시간 비교

### 9.1 시설명 포함 검색 API

```http
GET /api/facilities/search?keyword=서울주차장
```

| 구분     | 응답 시간 |
| ------ | ----: |
| Before | 397ms |
| After  |  54ms |

개선율:

```text
(397 - 54) / 397 * 100 = 약 86.4%
```

<img width="711" height="605" alt="index before 1" src="https://github.com/user-attachments/assets/f873d595-33ac-48e5-b0f3-30c5aa9bec00" />

<img width="723" height="603" alt="index after 1" src="https://github.com/user-attachments/assets/8c8012bb-50d0-49d7-ba53-3c0261ae8924" />

### 9.2 시설명 정확 일치 검색 API

정확 일치 검색 API를 추가했습니다.

```http
GET /api/facilities/search/exact?name=서울주차장
```

Controller:

```java
@GetMapping("/search/exact")
public List<FacilityResponse> searchFacilitiesByExactName(
        @RequestParam String name
) {
    return facilityService.searchFacilitiesByExactName(name);
}
```

Service:

```java
public List<FacilityResponse> searchFacilitiesByExactName(String name) {
    return facilityRepository.findByName(name)
            .stream()
            .map(FacilityResponse::new)
            .toList();
}
```

Repository:

```java
List<Facility> findByName(String name);
```

API 응답 시간 비교:

| 구분     | 응답 시간 |
| ------ | ----: |
| Before |  52ms |
| After  |  12ms |

개선율:

```text
(52 - 12) / 52 * 100 = 약 76.9%
```

<img width="731" height="598" alt="인덱스 전" src="https://github.com/user-attachments/assets/1aaa8c99-92bc-497b-af27-18ffad8180f6" />

<img width="733" height="592" alt="인덱스 후" src="https://github.com/user-attachments/assets/e2cc4e17-e8ee-4968-9a08-0015467cc1e7" />

## 10. 복합 인덱스 적용

시설 검색은 단순 시설명 검색뿐 아니라 카테고리와 시설명을 함께 사용하는 경우가 많습니다.

예를 들어 공영주차장 카테고리 안에서 특정 시설명을 검색하는 경우입니다.

```sql
SELECT *
FROM facility
WHERE category_id = 1
AND name = '서울주차장';
```

이를 위해 복합 인덱스를 적용했습니다.

```sql
CREATE INDEX idx_facility_category_name
ON facility(category_id, name);
```

복합 인덱스는 왼쪽 컬럼부터 순서대로 사용됩니다.

```text
category_id
  ↓
name
```

따라서 다음 조건에서 효과적으로 사용됩니다.

```sql
WHERE category_id = ?
AND name = ?
```

복합 인덱스 적용 결과:

```text
Index Lookup using idx_facility_category_name
category_id = 1
name = '서울주차장'
cost = 0.35
rows = 1
```

<img width="1138" height="563" alt="복합인덱스 확인" src="https://github.com/user-attachments/assets/fd87e771-1985-4ffc-b2b6-a7dd5bd9cb30" />

<img width="1144" height="581" alt="복합인덱스 적용" src="https://github.com/user-attachments/assets/16fa6ef3-1f2f-42a9-a9ed-1428b833c726" />

## 11. 인덱스 적용 결과 정리

| 항목              |     Before |             After |
| --------------- | ---------: | ----------------: |
| 실행 방식           | Table Scan |      Index Lookup |
| type            |        ALL |               ref |
| 사용 인덱스          |         없음 | idx_facility_name |
| cost            |      11666 |              0.35 |
| rows            |      11385 |                 1 |
| Exact API 응답 시간 |       52ms |              12ms |
| 포함 검색 API 응답 시간 |      397ms |              54ms |

## 12. API 목록

### 시설 API

| Method | URL                                                | 설명            |
| ------ | -------------------------------------------------- | ------------- |
| GET    | /api/facilities                                    | 시설 목록 조회      |
| GET    | /api/facilities/{id}                               | 시설 단건 조회      |
| POST   | /api/facilities                                    | 시설 등록         |
| PUT    | /api/facilities/{id}                               | 시설 수정         |
| DELETE | /api/facilities/{id}                               | 시설 삭제         |
| GET    | /api/facilities/search?keyword=                    | 시설명 포함 검색     |
| GET    | /api/facilities/search/exact?name=                 | 시설명 정확 검색     |
| GET    | /api/facilities/search/category?categoryId=        | 카테고리별 시설 검색   |
| GET    | /api/facilities/search/detail?keyword=&categoryId= | 시설명 + 카테고리 검색 |

### 인증 API

| Method | URL           | 설명            |
| ------ | ------------- | ------------- |
| POST   | /api/users    | 회원가입          |
| POST   | /api/login    | 로그인           |
| GET    | /api/users/me | 현재 로그인 사용자 조회 |

### 즐겨찾기 API

| Method | URL                               | 설명              |
| ------ | --------------------------------- | --------------- |
| POST   | /api/favorites                    | 즐겨찾기 등록         |
| GET    | /api/favorites/me                 | 로그인 사용자 즐겨찾기 조회 |
| GET    | /api/favorites/users/{userId}     | 사용자별 즐겨찾기 조회    |
| DELETE | /api/favorites/{favoriteId}       | 즐겨찾기 삭제         |
| PUT    | /api/favorites/{favoriteId}/group | 즐겨찾기 그룹 이동      |

### 즐겨찾기 그룹 API

| Method | URL                                 | 설명         |
| ------ | ----------------------------------- | ---------- |
| POST   | /api/favorite-groups                | 즐겨찾기 그룹 생성 |
| GET    | /api/favorite-groups/users/{userId} | 사용자별 그룹 조회 |
| PUT    | /api/favorite-groups/{groupId}      | 그룹명 수정     |
| DELETE | /api/favorite-groups/{groupId}      | 그룹 삭제      |

## 13. 프로젝트 구조

```text
src/main/java/com/project/facility
 ├── config
 ├── controller
 ├── dto
 ├── entity
 ├── exception
 ├── global
 ├── repository
 ├── security
 └── service
```

| 패키지              | 설명                 |
| ---------------- | ------------------ |
| config           | Spring Security 설정 |
| controller       | REST API 요청 처리     |
| dto              | 요청/응답 DTO          |
| entity           | JPA Entity         |
| exception/global | 공통 예외 처리           |
| repository       | JPA Repository     |
| security         | JWT 인증 필터 및 토큰 처리  |
| service          | 비즈니스 로직 처리         |

## 14. 실행 방법

### 14.1 프로젝트 실행

```bash
git clone 프로젝트_URL
cd facility-platform
./gradlew bootRun
```

### 14.2 MySQL 설정

`application.yml` 또는 `application.properties`에 DB 정보를 설정합니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/facility_platform
spring.datasource.username=DB_USER
spring.datasource.password=DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### 14.3 인덱스 생성 SQL

```sql
CREATE INDEX idx_facility_name
ON facility(name);

CREATE INDEX idx_facility_category_name
ON facility(category_id, name);
```

## 15. 트러블슈팅

### 15.1 CSV Import 인코딩 문제

공공데이터 CSV를 MySQL Workbench로 Import하는 과정에서 한글 인코딩 문제가 발생했습니다.

해결:

```text
CSV 파일을 UTF-8 BOM 형식으로 변환하여 Import
```

### 15.2 좌표 데이터 누락

일부 공공데이터에는 위도/경도 정보가 존재하지 않았습니다.

해결:

```text
검색 성능 측정 목적에서는 시설명, 주소, 카테고리 중심으로 데이터를 활용
좌표 데이터가 없는 경우 NULL 또는 0.0으로 처리
```

### 15.3 기존 검색 방식의 한계

기존 `Containing` 검색은 SQL에서 다음과 같이 실행됩니다.

```sql
WHERE name LIKE '%keyword%'
```

이 방식은 B-Tree 인덱스를 충분히 활용하기 어렵습니다.

해결:

```text
정확 일치 검색 API를 추가하여 name = ? 조건에서 B-Tree 인덱스가 활용되도록 개선
```

## 16. 향후 개선 사항

* QueryDSL을 활용한 동적 검색 조건 개선
* Elasticsearch를 활용한 부분 문자열 검색 고도화
* Redis Cache 적용
* 위치 기반 검색 기능 개선
* Docker 기반 배포 환경 구성
* JMeter 기반 부하 테스트
* AWS 배포 및 CI/CD 구성

## 17. 회고

이번 프로젝트를 통해 단순히 API를 구현하는 것에서 나아가, 실제 데이터 규모가 커졌을 때 발생할 수 있는 검색 성능 문제를 직접 확인하고 개선했습니다.

특히 MySQL의 EXPLAIN과 EXPLAIN ANALYZE를 활용하여 실행 계획을 분석했고, 인덱스 적용 전후의 Table Scan과 Index Lookup 차이를 확인했습니다.

또한 API 응답 시간을 Postman으로 측정하여 DB 수준의 성능 개선이 실제 API 응답 시간에도 영향을 줄 수 있음을 확인했습니다.
