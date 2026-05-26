# Facility Platform

공공시설 정보를 효율적으로 조회하고 관리할 수 있는 Spring Boot 기반 공공시설 통합 플랫폼입니다.

이 프로젝트는 공공데이터를 활용하여 시설 정보를 제공하고, 사용자가 카테고리별 시설 조회, 위치 기반 검색, 즐겨찾기, 리뷰 기능을 사용할 수 있도록 설계되었습니다.

## 초기 구현 내용

본 프로젝트는 공공시설 정보를 카테고리 기반으로 관리하는 플랫폼을 목표로 시작했습니다.

초기 단계에서는 Spring Boot 기반 프로젝트 구조를 구성하고, MySQL 데이터베이스와 연동하여 기본적인 Category API를 구현했습니다.

### 주요 작업

- Spring Boot 프로젝트 생성
- MySQL 데이터베이스 연결
- Category Entity 생성
- CategoryRepository 생성
- CategoryService 생성
- CategoryController 생성
- Request/Response DTO 적용
- Postman을 활용한 기본 API 테스트

### 초기 API

- POST /api/categories
- GET /api/categories

### 설계 방향

초기 구현에서는 Controller가 Entity를 직접 주고받지 않도록 DTO를 적용했습니다.  
이를 통해 API 요청/응답 구조와 DB Entity 구조를 분리하고, 이후 기능 확장과 유지보수가 쉬운 구조를 만들고자 했습니다.
