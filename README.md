# Facility Platform

공공시설 정보를 효율적으로 조회하고 관리할 수 있는 Spring Boot 기반 공공시설 통합 플랫폼입니다.

공공데이터를 활용하여 시설 정보를 제공하고, 사용자가 카테고리별 시설 조회, 시설 검색, 즐겨찾기, 리뷰, 위치 기반 검색 기능을 사용할 수 있도록 설계하고 있습니다.

---

## 프로젝트 목표

기존 공공시설 정보는 여러 사이트에 분산되어 있어 원하는 시설을 찾기 어렵습니다.

본 프로젝트는 공공시설 정보를 통합 관리하고, 사용자가 쉽고 빠르게 시설 정보를 조회할 수 있는 플랫폼 구축을 목표로 합니다.

또한 단순 CRUD 구현에 그치지 않고, 검색 기능, 페이징, 정렬, Validation, 보안, 위치 기반 서비스 등을 단계적으로 적용하며 실무 환경과 유사한 구조를 학습하는 것을 목표로 합니다.

---

## 기술 스택

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Lombok
* Validation
* Gradle
* Git / GitHub

---

## 현재 구현 기능

### Category

* 카테고리 등록
* 카테고리 조회
* 카테고리 수정
* 카테고리 삭제
* 카테고리 중복 등록 방지

### Facility

* 시설 등록
* 시설 조회
* 시설 단건 조회
* 시설 수정
* 시설 삭제

### 시설 검색

* 시설명 기반 검색
* 카테고리 기반 검색
* 시설명 + 카테고리 복합 검색

### Pagination & Sort

* Pageable 기반 페이지 조회
* 최신 등록순 정렬 (id DESC)

### Validation

* @NotBlank 기반 문자열 검증
* @NotNull 기반 필수값 검증
* @Valid 적용
* GlobalExceptionHandler 기반 예외 처리

---

## API 예시

### 카테고리 등록

```http
POST /api/categories
```

### 카테고리 조회

```http
GET /api/categories
```

### 시설 등록

```http
POST /api/facilities
```

### 시설 조회

```http
GET /api/facilities
```

### 시설 검색

```http
GET /api/facilities/search?keyword=주차장
```

### 페이징 조회

```http
GET /api/facilities?page=0&size=3
```

---

## 개발 진행 현황

* [x] Category CRUD
* [x] Facility CRUD
* [x] 시설 검색
* [x] Pagination
* [x] Sort
* [x] Validation
* [ ] User CRUD
* [ ] Favorite
* [ ] Review
* [ ] Spring Security
* [ ] 위치 기반 검색
* [ ] 공공데이터 연동

---

## 프로젝트 구조

```text
Controller
↓
DTO
↓
Service
↓
Entity
↓
Repository
↓
Database
```

### Pagination 흐름

```text
Postman
↓
Controller
↓
Pageable
↓
Repository
↓
Page<Entity>
↓
Page<Response DTO>
↓
JSON 응답
```

### Validation 흐름

```text
Postman
↓
JSON 요청
↓
@RequestBody
↓
DTO 생성
↓
@Valid
↓
@NotBlank / @NotNull 검사
↓
MethodArgumentNotValidException
↓
GlobalExceptionHandler
↓
400 Bad Request
```

---

## 주요 학습 내용

* DTO와 Entity 분리
* JPA Repository 활용
* 메서드 네이밍 기반 Query 생성
* Pageable 기반 페이징 처리
* Sort를 활용한 정렬 처리
* Validation 및 전역 예외 처리
* 계층형 아키텍처 기반 API 설계

---

## 향후 계획

### 사용자 기능 확장

* User CRUD 구현
* 사용자별 시설 이용 기능 구축
* 사용자 정보 관리 기능 추가

### 사용자 맞춤 서비스

* 즐겨찾기(Favorite) 기능 구현
* 리뷰(Review) 기능 구현
* 사용자 활동 기반 서비스 확장

### 보안 강화

* Spring Security 적용
* BCrypt를 이용한 비밀번호 암호화
* 인증(Authentication) 및 인가(Authorization) 구현
* Role 기반 접근 제어 적용

### 위치 기반 서비스

* 사용자 위치 기반 시설 조회
* 거리순 정렬 기능 제공
* 지도 API 연동

### 공공데이터 연동

* 공공데이터 API 연동
* 시설 정보 자동 수집 및 동기화
* 중복 데이터 저장 방지 전략 적용

### 운영 환경 고려

* 서버 운영 중 데이터베이스 스키마 변경 전략 학습
* 데이터 마이그레이션 방식 적용
* 무중단 배포 환경 구성 검토

### 성능 최적화

* 인덱스(Index)를 활용한 조회 성능 개선
* 대용량 데이터 환경에서의 페이징 최적화
* 캐시 적용 검토

### 동시성 문제 해결

* 공공데이터 동기화 중복 저장 방지
* 여러 관리자 동시 수정 상황 처리
* 즐겨찾기 중복 등록 방지
* 트랜잭션(Transaction) 기반 데이터 정합성 유지

### 네트워크 및 보안 학습

* HTTPS 통신 구조 이해
* RSA 기반 키 교환 방식 학습
* AES 기반 데이터 암호화 적용
* 인증 정보 및 암호화 키 관리 방법 학습

