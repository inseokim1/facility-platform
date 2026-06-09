# Facility Platform Roadmap

## Phase 1. 기본 시설 관리 기능

- [x] Category CRUD 구현
- [x] Facility CRUD 구현
- [x] DTO 기반 요청/응답 구조 분리
- [x] GlobalExceptionHandler 기반 예외 처리
- [x] Validation 적용

## Phase 2. 조회 기능 고도화

- [x] 시설명 검색
- [x] 카테고리별 시설 조회
- [x] 시설명 + 카테고리 복합 검색
- [x] Pageable 기반 페이징
- [x] 최신 등록순 정렬

## Phase 3. 사용자 기능

- [ ] User CRUD 구현
- [ ] 즐겨찾기(Favorite) 기능 구현
- [ ] 리뷰(Review) 기능 구현
- [ ] 사용자별 관심 시설 관리

## Phase 4. 보안 기능

- [ ] Spring Security 적용
- [ ] BCrypt 기반 비밀번호 암호화
- [ ] 로그인 / 인증 기능 구현
- [ ] Role 기반 권한 관리
- [ ] 관리자 권한 분리

## Phase 5. 위치 기반 서비스

- [ ] 사용자 위치 기반 시설 조회
- [ ] 거리순 정렬 기능 구현
- [ ] 지도 API 연동
- [ ] 지도 위 시설 마커 표시

## Phase 6. 공공데이터 연동

- [ ] 공공데이터 API 연동
- [ ] 시설 데이터 자동 수집
- [ ] 중복 데이터 저장 방지
- [ ] 데이터 동기화 로그 관리

## Phase 7. 성능 개선

- [ ] 검색 쿼리 실행 계획 분석
- [ ] 인덱스 적용 전/후 성능 비교
- [ ] 대용량 데이터 페이징 최적화
- [ ] 캐시 적용 검토

## Phase 8. 동시성 문제 해결

- [ ] 즐겨찾기 중복 등록 문제 재현
- [ ] 공공데이터 동기화 중복 저장 문제 재현
- [ ] 여러 관리자 동시 수정 문제 재현
- [ ] Unique 제약 조건 적용
- [ ] Transaction 적용
- [ ] Optimistic Lock 적용 검토

## Phase 9. 운영 환경 구성

- [ ] Docker 적용
- [ ] MySQL 컨테이너 구성
- [ ] CI/CD 파이프라인 구성
- [ ] 서버 배포 환경 구성
- [ ] DB Migration 도구 검토

## Phase 10. 모니터링 및 운영 안정성

- [ ] API 응답 시간 로그 기록
- [ ] 에러 로그 관리
- [ ] Spring Actuator 적용 검토
- [ ] 트래픽 부하 테스트 진행
