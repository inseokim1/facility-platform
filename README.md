# Favorite Group 기능 구현

## 구현 배경

기존 즐겨찾기 기능은 사용자가 저장한 시설을 하나의 목록으로만 관리할 수 있었습니다.

하지만 실제 서비스에서는 사용자가 원하는 기준으로 즐겨찾기를 분류할 수 있어야 한다고 판단했습니다.

예를 들어 사용자는 다음과 같은 그룹을 직접 만들 수 있습니다.

```text
실내운동
화장실
공부장소
자주 방문하는 시설
```

이를 위해 FavoriteGroup Entity를 추가하고, Favorite가 특정 그룹에 속할 수 있도록 구조를 확장했습니다.

---

## 전체 구조

기존 구조는 다음과 같았습니다.

```text
User
↓
Favorite
↓
Facility
```

FavoriteGroup 기능 추가 후 구조는 다음과 같습니다.

```text
User
↓
FavoriteGroup
↓
Favorite
↓
Facility
```

실제 테이블 구조는 다음과 같습니다.

```text
favorite_groups

id
user_id
name
```

```text
favorites

id
user_id
facility_id
group_id
```

Java Entity에서는 객체 참조를 사용하지만, DB에는 객체가 저장되는 것이 아니라 외래키 값이 저장됩니다.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "group_id", nullable = false)
private FavoriteGroup favoriteGroup;
```

---

## 구현 내용

## 1. FavoriteGroup CRUD 구현

다음 기능을 구현했습니다.

```text
FavoriteGroup 생성
FavoriteGroup 조회
FavoriteGroup 수정
FavoriteGroup 삭제
```

---

## 2. Favorite와 FavoriteGroup 연관관계 추가

기존 Favorite는 User와 Facility만 참조했습니다.

```text
Favorite
- user
- facility
```

FavoriteGroup 기능을 추가하면서 Favorite가 FavoriteGroup도 참조하도록 변경했습니다.

```text
Favorite
- user
- facility
- favoriteGroup
```

이를 통해 하나의 즐겨찾기가 하나의 그룹에 속할 수 있도록 설계했습니다.

---

## 3. 즐겨찾기 등록 시 그룹 선택 기능

기존 즐겨찾기 등록 요청은 userId와 facilityId만 전달했습니다.

```json
{
  "userId": 1,
  "facilityId": 9
}
```

FavoriteGroup 기능 추가 후에는 groupId도 함께 전달합니다.

```json
{
  "userId": 1,
  "facilityId": 9,
  "groupId": 2
}
```

### 처리 흐름

```text
Postman 요청
↓
FavoriteCreateRequest
↓
User 조회
↓
Facility 조회
↓
FavoriteGroup 조회
↓
중복 즐겨찾기 검사
↓
Favorite 생성
↓
user / facility / favoriteGroup 연결
↓
DB 저장
↓
FavoriteResponse 반환
```

### 실행 결과

<img width="718" height="902" alt="favoite조회 확인 groupID 포함해서 등록" src="https://github.com/user-attachments/assets/a9237f80-d8c2-46c1-a362-f38828642e22" />

---

## 4. 그룹별 즐겨찾기 조회

특정 그룹에 포함된 즐겨찾기만 조회할 수 있도록 API를 추가했습니다.

```http
GET /api/favorites/groups/{groupId}
```

### 처리 흐름

```text
groupId 요청
↓
FavoriteRepository
↓
findByFavoriteGroup_Id(groupId)
↓
해당 그룹에 속한 Favorite 목록 조회
↓
FavoriteResponse 변환
↓
응답 반환
```

### 실행 결과

<img width="724" height="685" alt="favorites 운동시설 그룹만 조회" src="https://github.com/user-attachments/assets/4173c1fd-3a9c-4130-a1fd-729fe115a205" />
---

## 5. 즐겨찾기 그룹 이동

즐겨찾기를 다른 그룹으로 이동할 수 있도록 API를 추가했습니다.

```http
PUT /api/favorites/{favoriteId}/group
```

요청 예시

```json
{
  "groupId": 2
}
```

### 처리 흐름

```text
favoriteId 요청
↓
이동 대상 groupId 요청
↓
Favorite 조회
↓
FavoriteGroup 조회
↓
Favorite의 favoriteGroup 변경
↓
트랜잭션 커밋 시 DB 반영
↓
FavoriteResponse 반환
```

### 실행 결과

<img width="730" height="542" alt="favorite-groups 그룹 이동 성공" src="https://github.com/user-attachments/assets/5b343b26-f4fb-434d-ae79-9ad3b0eba139" />

---

## 6. 그룹명 수정

사용자가 생성한 그룹명을 수정할 수 있도록 API를 추가했습니다.

```http
PUT /api/favorite-groups/{groupId}
```

요청 예시

```json
{
  "name": "실내운동"
}
```

### 처리 흐름

```text
groupId 요청
↓
FavoriteGroup 조회
↓
기존 그룹명과 요청 그룹명 비교
↓
이름이 변경되는 경우에만 중복 검사
↓
그룹명 수정
↓
FavoriteGroupResponse 반환
```

### 실행 결과

<img width="715" height="451" alt="favorite group 수정" src="https://github.com/user-attachments/assets/ede1b3c4-f222-4a97-907f-953507618a05" />

---

## 7. 그룹 삭제

그룹 삭제 시 해당 그룹에 포함된 Favorite를 먼저 삭제한 후 FavoriteGroup을 삭제하도록 구현했습니다.

```http
DELETE /api/favorite-groups/{groupId}
```

### 처리 흐름

```text
groupId 요청
↓
FavoriteGroup 존재 여부 확인
↓
해당 그룹에 속한 Favorite 먼저 삭제
↓
FavoriteGroup 삭제
↓
응답 반환
```

Cascade Remove를 사용하지 않고 Service에서 명시적으로 삭제 순서를 제어했습니다.

### 실행 결과

<img width="711" height="405" alt="favorite-groups 삭제 오류 해결" src="https://github.com/user-attachments/assets/ab94f8c1-4101-46e2-99af-ebeecda9beec" />

---

# 트러블 슈팅

## 1. Favorite 조회 시 NullPointerException 발생

### 문제 상황

FavoriteGroup 연관관계를 추가한 뒤 기존 즐겨찾기 목록을 조회했을 때 500 Internal Server Error가 발생했습니다.

문제가 발생한 부분은 FavoriteResponse 생성자였습니다.

```java
this.groupId = favorite.getFavoriteGroup().getId();
this.groupName = favorite.getFavoriteGroup().getName();
```

### 원인

FavoriteGroup 기능을 추가하기 전에는 favorites 테이블에 group_id 컬럼 값이 존재하지 않았습니다.

즉 기존 Favorite 데이터는 다음과 같은 상태였습니다.

```text
favorites

id
user_id
facility_id
group_id = null
```

그런데 응답 DTO에서는 favoriteGroup이 존재한다고 가정하고 getId(), getName()을 호출했습니다.

그 결과 favorite.getFavoriteGroup()이 null인 상태에서 getId()를 호출하게 되어 NullPointerException이 발생했습니다.

### 해결 과정

테스트 단계에서는 기존 Favorite 데이터를 삭제한 후 groupId를 포함하여 다시 등록했습니다.

```json
{
  "userId": 1,
  "facilityId": 9,
  "groupId": 2
}
```

이후 Favorite 조회 시 groupId, groupName이 정상적으로 응답되는 것을 확인했습니다.

### 학습한 점

기존 테이블에 새로운 FK를 추가할 경우 기존 데이터에 대한 처리 전략이 필요하다는 것을 학습했습니다.

운영 환경이라면 단순 삭제가 아니라 다음과 같은 방식이 필요할 수 있습니다.

```text
기본 그룹 생성
기존 Favorite 데이터를 기본 그룹으로 마이그레이션
group_id NOT NULL 제약 적용
```

---

## 2. FavoriteGroup 삭제 시 500 Internal Server Error 발생

### 문제 상황

FavoriteGroup 삭제 API를 실행했을 때 500 Internal Server Error가 발생했습니다.

```http
DELETE /api/favorite-groups/{groupId}
```

초기 삭제 로직은 다음과 같은 흐름이었습니다.

```text
Favorite 삭제
↓
FavoriteGroup 삭제
```

### 원인

그룹 삭제는 하나의 DB 작업이 아니라 두 개의 DB 변경 작업으로 이루어져 있습니다.

```text
1. 해당 그룹에 속한 Favorite 삭제
2. FavoriteGroup 삭제
```

이 두 작업은 하나의 작업 단위로 묶여야 합니다.

하지만 트랜잭션이 적용되지 않은 상태에서는 삭제 작업이 안정적으로 처리되지 않아 오류가 발생했습니다.

### 해결 방법

FavoriteGroupService의 삭제 메서드에 @Transactional을 적용했습니다.

```java
@Transactional
public void deleteFavoriteGroup(Long groupId) {
    favoriteRepository.deleteByFavoriteGroup_Id(groupId);
    favoriteGroupRepository.deleteById(groupId);
}
```

이를 통해 Favorite 삭제와 FavoriteGroup 삭제가 하나의 트랜잭션 안에서 처리되도록 수정했습니다.

### 학습한 점

수정, 삭제처럼 DB 상태를 변경하는 로직에서는 트랜잭션 범위를 고려해야 한다는 것을 학습했습니다.

특히 여러 테이블을 순서대로 변경하는 경우 하나의 작업 단위로 묶지 않으면 데이터 정합성이 깨질 수 있습니다.

---

## 3. 그룹명 수정 시 Self Duplicate 문제

### 문제 상황

그룹명 수정 기능에서 기존 이름과 동일한 이름으로 수정 요청을 보냈을 때 중복 예외가 발생했습니다.

예시

```text
현재 그룹명: 실내운동
수정 요청: 실내운동
```

실제로는 아무 변경도 없는 요청이지만 다음 예외가 발생했습니다.

```text
이미 존재하는 그룹명입니다.
```

### 원인

중복 검사를 위해 다음 Repository 메서드를 사용했습니다.

```java
existsByUser_IdAndName(userId, name)
```

하지만 이 메서드는 현재 수정 대상인 자기 자신도 조회합니다.

즉 현재 DB에 이미 존재하는 자기 자신의 데이터 때문에 중복으로 판단되는 문제가 발생했습니다.

### 해결 방법

기존 그룹명과 요청 그룹명이 다른 경우에만 중복 검사를 수행하도록 수정했습니다.

```java
if (!favoriteGroup.getName().equals(request.getName())
        && favoriteGroupRepository.existsByUser_IdAndName(
                favoriteGroup.getUser().getId(),
                request.getName()
        )) {
    throw new IllegalArgumentException("이미 존재하는 그룹명입니다.");
}
```

### 학습한 점

Update 로직에서는 단순히 중복 여부만 확인하면 안 되고, 자기 자신과의 중복 여부를 구분해야 한다는 것을 학습했습니다.

이를 Self Duplicate 문제로 정리할 수 있습니다.

---

## 4. Cascade Remove 대신 명시적 삭제 선택

### 고민한 부분

FavoriteGroup을 삭제할 때 해당 그룹에 속한 Favorite도 함께 삭제해야 했습니다.

이때 JPA의 Cascade Remove를 사용할 수도 있었습니다.

### Cascade Remove를 사용하지 않은 이유

Cascade Remove는 부모 Entity를 삭제하면 연결된 자식 Entity도 자동으로 삭제합니다.

편리하지만 관계 구조를 정확히 이해하지 못한 상태에서 사용하면 의도하지 않은 데이터까지 삭제될 수 있습니다.

현재 프로젝트에서는 데이터 삭제 흐름을 명확히 이해하고 제어하기 위해 Service에서 직접 삭제 순서를 작성했습니다.

```text
Favorite 삭제
↓
FavoriteGroup 삭제
```

### 학습한 점

Cascade는 편리하지만 위험할 수 있으므로, 테이블 관계와 삭제 범위를 명확히 파악한 뒤 사용하는 것이 적절하다고 판단했습니다.

---

# 학습 내용

## JPA 연관관계

Java Entity에서는 객체 참조를 사용합니다.

```java
private FavoriteGroup favoriteGroup;
```

하지만 DB에는 객체가 저장되는 것이 아니라 FK 값이 저장됩니다.

```text
group_id
```

즉 Java에서는 객체지향적으로 다루고, DB에서는 외래키로 관계를 관리합니다.

---

## FetchType.LAZY

FetchType.LAZY는 단순히 조회 속도를 높이는 옵션이 아니라 연관 객체를 언제 조회할지 결정하는 전략입니다.

```text
EAGER
- Entity 조회 시 연관 Entity도 즉시 조회

LAZY
- Entity 조회 시 연관 Entity는 바로 조회하지 않음
- 실제로 getUser(), getFavoriteGroup() 등을 호출할 때 조회
```

이를 통해 불필요한 연관 Entity 조회를 줄일 수 있습니다.

---

## Optional과 orElseThrow

JPA의 findById()는 Optional을 반환합니다.

Optional은 null을 저장하는 객체가 아니라 값이 존재할 수도 있고 존재하지 않을 수도 있음을 표현하는 객체입니다.

```java
User user = userRepository.findById(id)
        .orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));
```

값이 존재하면 User를 반환하고, 값이 없으면 Optional.empty 상태가 되어 orElseThrow의 람다식이 실행됩니다.

---

## nullable = false 와 @NotNull

두 개념은 비슷해 보이지만 적용 위치가 다릅니다.

```text
@NotNull
- Java Validation
- Controller에서 요청 DTO 검증

nullable = false
- DB 제약조건
- 테이블 컬럼에 NOT NULL 적용
```

따라서 실무에서는 요청 검증과 DB 무결성을 위해 둘 다 사용하는 경우가 많습니다.

---

# 테스트

다음 기능을 Postman으로 테스트했습니다.

```text
FavoriteGroup 생성
FavoriteGroup 조회
FavoriteGroup 수정
FavoriteGroup 삭제
Favorite 등록 시 그룹 지정
그룹별 Favorite 조회
Favorite 그룹 이동
그룹 삭제 시 Favorite 삭제
중복 그룹명 예외 처리
Self Duplicate 검증
@Transactional 적용 후 삭제 정상 처리
```

---

# 향후 개선 예정

```text
Spring Security 적용
BCrypt 비밀번호 암호화
Role 기반 권한 관리
로그인 사용자 기반 API(/me) 적용
시설 검색 성능 개선
EXPLAIN 기반 인덱스 최적화
프론트엔드 연동
```
