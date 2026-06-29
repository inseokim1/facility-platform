# 즐겨찾기(Favorite) 기능 구현

## 작업 내용

### Favorite Entity 생성

* Favorite Entity 생성
* User와 Facility를 연결하는 중간 테이블 구현
* @ManyToOne 연관관계 적용
* FetchType.LAZY 적용

### 즐겨찾기 기능 구현

* 즐겨찾기 등록
* 사용자별 즐겨찾기 조회
* 즐겨찾기 삭제

### 중복 즐겨찾기 방지

* user_id + facility_id 조합에 Unique Constraint 적용
* 동일 사용자가 같은 시설을 중복 등록할 수 없도록 구현
* 중복 등록 시 예외 처리

---

## Favorite 테이블 구조

```text
favorites

id
user_id
facility_id
```

### 설계 이유

Favorite은 User와 Facility의 다대다(M:N) 관계를 관리하기 위한 중간 테이블로 구현하였다.

기본키는 별도의 id(PK)를 사용하였다.

```text
PK = id
UNIQUE = (user_id, facility_id)
```

복합키(user_id, facility_id)를 사용하는 방식도 고려하였으나,

* JPA 매핑 단순화
* Entity 관리 편의성
* 향후 컬럼 확장(createdAt 등)

을 고려하여 별도 PK를 채택하였다.

대신 user_id + facility_id 조합에 Unique Constraint를 적용하여 중복 즐겨찾기를 방지하였다.

---

## 즐겨찾기 등록 흐름

```text
Postman
↓
FavoriteCreateRequest
↓
User 조회
↓
Facility 조회
↓
중복 여부 확인
↓
Favorite 생성
↓
DB 저장
↓
FavoriteResponse 반환
```

---

## 즐겨찾기 조회 흐름

```text
GET /api/favorites/users/{userId}
↓
userId 기준 조회
↓
Favorite 목록 조회
↓
FavoriteResponse 변환
↓
응답 반환
```

---

## 테스트

### 즐겨찾기 등록

요청

```json
{
  "userId": 1,
  "facilityId": 2
}
```

결과
<img width="739" height="625" alt="favorite 등록" src="https://github.com/user-attachments/assets/d69b4156-f754-4cff-81ec-085d2ed9cfad" />

```

---

### 즐겨찾기 조회

요청

```http
GET /api/favorites/users/1
```

결과

등록된 즐겨찾기 목록 조회 성공

<img width="725" height="630" alt="favorite 조회" src="https://github.com/user-attachments/assets/b3eb65a0-33ee-4434-83e0-a76ffbbc1170" />


---

### 즐겨찾기 삭제

요청

```http
DELETE /api/favorites/1
```

결과

```text
즐겨찾기 삭제가 완료되었습니다.
```



<img width="725" height="516" alt="favorite 삭제" src="https://github.com/user-attachments/assets/6c195ea5-aefa-4b54-82ce-55e5cb16e483" />

삭제 후 조회 시 빈 배열([]) 반환 확인

<img width="738" height="513" alt="favorite 삭제 확인" src="https://github.com/user-attachments/assets/86b0e593-081c-4b45-ad05-bb0770b4a807" />

---

## 학습 내용

### 연관관계

* Favorite은 User와 Facility를 연결하는 중간 Entity로 설계
* @ManyToOne을 사용하여 User, Facility와 연관관계 설정

### FetchType.LAZY

* 연관 객체(User, Facility)를 즉시 조회하지 않고 필요 시 조회
* 불필요한 SQL 실행을 줄이는 방법 학습

### PK 설계

* email과 같은 자연키 대신 Long id를 PK로 사용
* 변경 가능한 값보다 변경되지 않는 식별자가 PK로 적합함을 학습

### Unique Constraint

* user_id + facility_id 조합으로 중복 즐겨찾기 방지
* DB 제약조건과 Service 검증을 함께 적용

### 향후 개선 예정

현재는 인증 기능이 구현되지 않아 userId를 PathVariable로 전달하여 조회하도록 구현하였다.

향후 Spring Security + JWT 적용 후에는

```http
GET /api/favorites/me
```

형태로 변경하여 로그인 사용자 기준 조회를 구현할 예정이다.

또한 즐겨찾기 목록에 지역 및 카테고리 필터 기능을 추가할 예정이다.
