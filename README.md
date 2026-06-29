# 즐겨찾기(Favorite) 필터링 기능 구현

## 작업 내용

기존 즐겨찾기 기능에 조건 검색 기능을 추가하여 사용자가 원하는 즐겨찾기 시설만 조회할 수 있도록 개선하였습니다.

### 추가 기능

* 지역(region) 기반 필터링
* 카테고리(categoryId) 기반 필터링
* 지역 + 카테고리 복합 조건 검색

---

## API

### 지역 필터

```http
GET /api/favorites/users/{userId}/filter?region=서울
```

### 카테고리 필터

```http
GET /api/favorites/users/{userId}/filter?categoryId=2
```

### 지역 + 카테고리 필터

```http
GET /api/favorites/users/{userId}/filter?region=경기도&categoryId=2
```

---

## 구현 흐름

```text
사용자 요청
↓
@RequestParam 수신
↓
region / categoryId 값 확인
↓
조건 분기
↓
Repository 조회
↓
Entity → DTO 변환
↓
응답 반환
```

---

## 검색 조건 분기

```java
if (region != null && categoryId != null)

else if (region != null)

else if (categoryId != null)

else
```

복합 조건이 존재하는 경우를 가장 먼저 검사하도록 구현하였다.

예를 들어

```http
GET /api/favorites/users/1/filter?region=서울&categoryId=1
```

요청 시

```java
else if(region != null)
```

를 먼저 검사하면 categoryId 조건이 무시될 수 있기 때문에

```java
if(region != null && categoryId != null)
```

를 최우선으로 배치하였다.

---

## Repository 구현

초기 구현

```java
findByUserIdAndFacilityAddressContaining(...)
```

```java
findByUserIdAndFacilityCategoryId(...)
```

형태로 작성하였다.

하지만 의도한 결과가 반환되지 않는 문제가 발생하였다.

---

## 문제 발생

### 현상

```http
GET /api/favorites/users/1/filter?region=서울
```

조회 시

```text
서울 시설만 조회되어야 함
```

에도 불구하고

```text
경기도 시설까지 함께 조회
```

되는 문제가 발생하였다.
<img width="642" height="704" alt="favoite에서의 조회 오류(_ 넣기전) " src="https://github.com/user-attachments/assets/8eef59a5-bdf5-48bc-adc0-b95cba3998c5" />

---

## 원인 분석

Favorite Entity는

```java
private User user;

private Facility facility;
```

구조를 가지고 있다.

즉 Favorite Entity에는

```java
userId
facilityId
```

필드가 직접 존재하지 않는다.

JPA는 실제로

```text
user.id
facility.address
facility.category.id
```

경로를 탐색해야 한다.

---

## 해결 방법

Repository 메서드를 아래와 같이 수정하였다.

### 수정 전

```java
findByUserIdAndFacilityAddressContaining(...)
```

### 수정 후

```java
findByUser_IdAndFacility_AddressContaining(...)
```

---

### 수정 전

```java
findByUserIdAndFacilityCategoryId(...)
```

### 수정 후

```java
findByUser_IdAndFacility_Category_Id(...)
```

---

### 수정 전

```java
findByUserIdAndFacilityAddressContainingAndFacilityCategoryId(...)
```

### 수정 후

```java
findByUser_IdAndFacility_AddressContainingAndFacility_Category_Id(...)
```

연관 객체의 필드를 명시적으로 탐색하도록 수정하여 문제를 해결하였다.

---

## 테스트 데이터

### Category

```text
1 | 공영주차장
2 | 체육관
```

### Facility

```text
성북구 공중화장실
서울특별시 성북구
categoryId = 1
```

```text
강남구 공중화장실
서울특별시 강남구
categoryId = 1
```

```text
수원시 체육관
경기도 수원시
categoryId = 2
```

```text
성남시 체육관
경기도 성남시
categoryId = 2
```
<img width="728" height="676" alt="favorite 카테고리 조회 테스트값 넣음" src="https://github.com/user-attachments/assets/d8d085dd-b0a4-40a5-bb2f-b9f7e9742df6" />

---

## 테스트 결과

### 지역 필터

```http
GET /api/favorites/users/1/filter?region=서울
```

결과

```text
성북구 공중화장실
강남구 공중화장실
```

서울 지역 시설만 정상 조회
<img width="742" height="598" alt="favorite 서울 조건 필터" src="https://github.com/user-attachments/assets/7d54bf7b-71f1-4cfb-b022-b8153a6b4e6c" />

---

### 카테고리 필터

```http
GET /api/favorites/users/1/filter?categoryId=2
```

결과

```text
수원시 체육관
성남시 체육관
```

체육관 카테고리 시설만 정상 조회
<img width="714" height="562" alt="favorite 카테고리2 조건 필터" src="https://github.com/user-attachments/assets/49d09e4e-b976-4cae-a009-0e5401e3e34e" />

---

### 지역 + 카테고리 필터

```http
GET /api/favorites/users/1/filter?region=경기도&categoryId=2
```

결과

```text
수원시 체육관
성남시 체육관
```

복합 조건 정상 동작 확인
<img width="709" height="572" alt="favorite 복합 조건 조회 성공" src="https://github.com/user-attachments/assets/c2a70920-5490-422b-becd-9c76fddec4d0" />

---

## 학습 내용

### @RequestParam

```java
@RequestParam(required = false)
```

를 사용하여 선택적인 검색 조건을 받을 수 있음을 학습하였다.

파라미터가 없는 경우에도 예외를 발생시키지 않고 null로 처리할 수 있다.

---

### JPA 연관관계 경로 탐색

연관관계 Entity를 조회할 경우

```text
user.id
facility.address
facility.category.id
```

형태로 탐색이 이루어짐을 학습하였다.

---

### 검색 API 설계

검색 조건이 여러 개 존재할 경우

```text
복합 조건
↓
단일 조건
↓
예외 처리
```

순서로 분기하는 것이 중요함을 학습하였다.

---

### Favorite 기능 확장 방향

현재 Favorite 기능은

* 등록
* 조회
* 삭제
* 조건 검색

까지 구현하였다.

향후에는

* Favorite Group 기능
* 사용자 정의 즐겨찾기 폴더
* 시설명 검색
* Favorite 목록 페이징 적용
* Favorite 목록 정렬 기능
* Spring Security 기반 사용자 인증

기능을 추가할 예정이다.
