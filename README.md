## 작업 내용
- 카테고리 이름 중복 등록 방지 기능 추가
- CategoryRepository에 existsByName 메서드 추가
- 중복 등록 시 IllegalArgumentException 발생 처리
- GlobalExceptionHandler를 통해 400 Bad Request 응답 처리

## 테스트
- Postman으로 동일한 카테고리명을 POST 요청
- 중복 등록 시 "이미 존재하는 카테고리입니다." 메시지 반환 확인
<img width="732" height="437" alt="image" src="https://github.com/user-attachments/assets/3e1bceb1-5e54-4ea6-b20b-2a9eefe1f15b" />
