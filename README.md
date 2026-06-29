# 사용자(User) CRUD 및 Validation 기능 구현

## 작업 내용

### User CRUD 구현

* User Entity 생성
* UserRepository 생성
* UserService 생성
* UserController 생성

### 사용자 기능 구현

* 사용자 등록
* 사용자 전체 조회
* 사용자 단건 조회
* 사용자 수정
* 사용자 삭제

### Validation 적용

* @NotBlank를 사용하여 필수 입력값 검증
* @Valid를 사용하여 Controller 진입 시 DTO 검증 수행
* GlobalExceptionHandler를 통한 Validation 예외 처리

### 중복 이메일 방지

* existsByEmail() 메서드를 활용한 이메일 중복 검사
* 중복 이메일 등록 시 예외 발생

---

## User CRUD 동작 흐름

```text
Postman
↓
JSON 요청
↓
@RequestBody
↓
UserCreateRequest DTO
↓
Service
↓
User Entity
↓
Repository
↓
Database
```

---

## Validation 처리 흐름

```text
Postman
↓
JSON 요청
↓
@RequestBody
↓
UserCreateRequest DTO 생성
↓
@Valid 검증 실행
↓
@NotBlank 검사
↓
MethodArgumentNotValidException 발생
↓
GlobalExceptionHandler 처리
↓
400 Bad Request 반환
↓
에러 메시지 응답
```

---

## 테스트

### 사용자 등록

요청

```json
{
  "email": "admin@test.com",
  "password": "1234",
  "name": "관리자",
  "role": "ADMIN"
}
```

결과
<img width="728" height="616" alt="user Post api" src="https://github.com/user-attachments/assets/283d25aa-70ab-420c-b581-93e8b0e74ee4" />

* 사용자 등록 성공
* 비밀번호는 응답 DTO에서 제외

---

### 사용자 전체 조회

결과
<img width="710" height="547" alt="user 전체 조회" src="https://github.com/user-attachments/assets/b8167d3e-c0b7-46ea-8192-2e3a899b7a99" />

* 등록된 사용자 목록 조회 성공

---

### 사용자 단건 조회

결과
<img width="726" height="552" alt="user 단건 조회" src="https://github.com/user-attachments/assets/e8051136-9bc5-441a-a33b-18ab9582678d" />


* 특정 사용자 조회 성공

---

### 사용자 수정

결과
<img width="732" height="523" alt="user 수정" src="https://github.com/user-attachments/assets/bc8dec4e-e96c-4cad-aa9d-14ea6b15f152" />

* 사용자 정보 수정 성공

---

### 사용자 삭제

결과
<img width="722" height="478" alt="user 삭제" src="https://github.com/user-attachments/assets/bbb6f012-8b1d-47d7-a55d-834f69f378ea" />

* 사용자 삭제 성공
<img width="718" height="462" alt="user 삭제후" src="https://github.com/user-attachments/assets/5b109367-0166-43a4-96fb-ee9c31400ae2" />



---

### Validation 테스트

요청

```json
{
  "email": "",
  "password": "1234",
  "name": "테스트",
  "role": "USER"
}
```

결과
<img width="721" height="443" alt="user valid 이메일 없음 예외 확인" src="https://github.com/user-attachments/assets/6ad5d1b6-6215-4ce2-9784-caf72c2ad293" />

* 400 Bad Request 반환
* "이메일은 필수입니다." 메시지 확인


---

## 학습 내용

- DTO와 Entity를 분리하여 민감 정보(password) 노출을 방지하고 응답 데이터를 제어하는 방법을 학습
- JpaRepository 상속을 통해 기본 CRUD 메서드(save, findAll, findById, deleteById)를 활용하는 방법을 학습
- existsByEmail()을 구현하며 Spring Data JPA의 메서드 이름 기반 Query 생성 기능을 학습
- @Valid와 @NotBlank를 활용한 입력값 검증 방법을 학습
- GlobalExceptionHandler를 활용한 전역 예외 처리 방식을 학습
- Controller → Service → Repository → DB로 이어지는 계층형 아키텍처 구조를 학습
