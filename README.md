# 시설 등록 요청 Validation 적용

## 작업 내용

* FacilityCreateRequest DTO에 Validation 적용
* @NotBlank를 사용하여 문자열 필수 입력 검증
* @NotNull을 사용하여 필수 값 검증
* Controller에 @Valid 적용
* GlobalExceptionHandler에 MethodArgumentNotValidException 처리 추가

## Validation 처리 흐름

```text
Postman
↓
JSON 요청
↓
@RequestBody
↓
FacilityCreateRequest DTO 생성
↓
@Valid 검증 실행
↓
@NotBlank / @NotNull 검사
↓
MethodArgumentNotValidException 발생
↓
GlobalExceptionHandler 처리
↓
400 Bad Request 반환
↓
에러 메시지 응답
```

## 테스트

### 1. 시설명 공백 입력

#### 요청

```json
{
  "name": ""
}
```

#### 결과

* 400 Bad Request 반환
* "시설명은 필수입니다." 메시지 확인

<img width="733" height="522" alt="facility validation 예외처리 시설명" src="https://github.com/user-attachments/assets/1f2a617c-b206-42b1-adcc-8bacee5d2b58" />

---

### 2. 카테고리 ID null 입력

#### 요청

```json
{
  "categoryId": null
}
```

#### 결과

* 400 Bad Request 반환
* "카테고리ID는필수입니다." 메시지 확인

<img width="718" height="500" alt="facility validation 예외처리 카테고리ID 예외처리" src="https://github.com/user-attachments/assets/71c6c8e0-9f7c-4a51-bcf6-0f509ffe5c2c" />
