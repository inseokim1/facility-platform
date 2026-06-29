## 작업 내용
- 카테고리 단건 조회 API 추가
- 카테고리 수정 API 추가
- 카테고리 삭제 API 추가
- 존재하지 않는 카테고리 요청 시 예외 처리 적용

## 구현 API
- GET /api/categories/{id}
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

## 테스트
- Postman으로 카테고리 단건 조회 확인
- Postman으로 카테고리 이름 수정 확인
- Postman으로 카테고리 삭제 확인
- 존재하지 않는 id 요청 시 "존재하지 않는 카테고리입니다." 메시지 반환 확인
<img width="733" height="633" alt="image" src="https://github.com/user-attachments/assets/bc999598-fc14-44cd-ac5f-49dc8a7c5c4f" />
<img width="744" height="535" alt="image" src="https://github.com/user-attachments/assets/f8f3a399-2931-4f3d-a0b4-e336b96daf67" />

