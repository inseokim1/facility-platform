<html>
<body>
<!--StartFragment--><html><head></head><body><h1>Spring Security 기반 JWT 인증 및 Role 권한 관리 구현</h1><h2>구현 배경</h2><p>기존 프로젝트는 회원가입과 로그인 기능은 있었지만, 로그인 이후 사용자의 인증 상태를 유지하거나 사용자 권한에 따라 API 접근을 제한하는 기능은 없었습니다.</p><p>또한 비밀번호가 평문으로 저장될 수 있는 구조였고, 예외 응답 형식도 일관되지 않아 프론트엔드 연동 시 오류 처리 기준이 명확하지 않았습니다.</p><p>이를 개선하기 위해 Spring Security 기반 인증/인가 구조를 적용하고, BCrypt 비밀번호 암호화, JWT 기반 Stateless 인증, Role 기반 권한 제어, Global Exception Handler를 구현했습니다.</p><hr><h2>전체 인증 구조</h2><p>기존 구조는 다음과 같았습니다.</p><pre><code class="language-text">회원가입
↓
DB 저장
↓
로그인
↓
응답 반환
</code></pre><p>로그인 성공 이후 사용자의 인증 상태를 증명할 방법이 없었습니다.</p><p>JWT 적용 후 구조는 다음과 같습니다.</p><pre><code class="language-text">회원가입
↓
BCrypt 비밀번호 암호화
↓
DB 저장
↓
로그인
↓
BCrypt 비밀번호 검증
↓
JWT 발급
↓
클라이언트가 JWT 저장
↓
Authorization Header에 Bearer Token 추가
↓
JwtAuthenticationFilter
↓
JWT 검증
↓
Authentication 생성
↓
SecurityContext 저장
↓
Controller
↓
Service
↓
Repository
</code></pre><hr><h2>구현 내용</h2><h2>1. Spring Security 설정</h2><p>SecurityConfig에서 URL별 접근 권한을 분리했습니다.</p><pre><code class="language-text">공개 API
- 회원가입
- 로그인
- 시설 조회
- 카테고리 조회

인증 필요 API
- 사용자 조회/수정/삭제
- 즐겨찾기
- 즐겨찾기 그룹

관리자 전용 API
- 시설 등록/수정/삭제
- 카테고리 등록/수정/삭제
</code></pre><p>이를 통해 공공시설 조회는 누구나 가능하게 두고, 개인화 기능은 로그인 사용자만 접근 가능하도록 구성했습니다.</p><p>시설 및 카테고리 데이터 관리 기능은 ADMIN 권한 사용자만 접근 가능하도록 제한했습니다.</p><h3>실행 결과</h3>
<p>스크린샷 첨부: SecurityConfig 권한 설정 코드</p>
<img width="698" height="465" alt="ADMIN 권한 테스트" src="https://github.com/user-attachments/assets/5661bfaa-9b3a-4690-bfa2-c6c8645ee89f" />

<hr><h2>2. BCrypt 비밀번호 암호화 적용</h2><p>회원가입 및 사용자 수정 시 비밀번호를 평문으로 저장하지 않고 BCrypt로 암호화하여 저장하도록 변경했습니다.</p><p>처리 흐름은 다음과 같습니다.</p><pre><code class="language-text">회원가입 요청
↓
PasswordEncoder.encode()
↓
BCrypt Hash 생성
↓
DB 저장
</code></pre><p>로그인 시에는 입력 비밀번호를 복호화하지 않고 <code inline="">PasswordEncoder.matches()</code>를 사용하여 검증했습니다.</p><pre><code class="language-text">로그인 요청
↓
이메일로 사용자 조회
↓
입력 비밀번호 + 저장된 BCrypt Hash 비교
↓
matches() 결과에 따라 로그인 성공/실패 처리
</code></pre><h3>실행 결과</h3>
<img width="727" height="506" alt="login 성공" src="https://github.com/user-attachments/assets/d9e11e53-7e9a-4d66-a0c4-a87e7b01d3d9" />
<img width="720" height="399" alt="login 실패" src="https://github.com/user-attachments/assets/af8be71f-596b-4f07-a79e-fae29fc80873" />


<hr><h2>3. 로그인 API 및 JWT 발급 구현</h2><p>로그인 성공 시 JWT를 발급하도록 구현했습니다.</p><p>JWT에는 다음 정보를 포함했습니다.</p><pre><code class="language-text">subject: email
claim: userId
claim: role
issuedAt
expiration
signature
</code></pre><p>처리 흐름은 다음과 같습니다.</p><pre><code class="language-text">POST /api/login
↓
LoginRequest
↓
UserRepository.findByEmail()
↓
PasswordEncoder.matches()
↓
JwtTokenProvider.createToken()
↓
LoginResponse에 token 포함하여 반환
</code></pre><h3>실행 결과</h3>
<p>스크린샷 첨부: 로그인 성공 시 JWT가 응답되는 Postman 화면</p>
<img width="727" height="506" alt="login 성공" src="https://github.com/user-attachments/assets/e68a9916-abfd-4b86-a82b-0a7cf28a1f34" />

<hr><h2>4. JwtTokenProvider 구현</h2><p>JWT 생성과 검증을 담당하는 JwtTokenProvider를 구현했습니다.</p><p>담당 역할은 다음과 같습니다.</p><pre><code class="language-text">createToken()
- 로그인 성공 시 JWT 생성

validateToken()
- JWT 서명 및 만료 여부 검증

getEmailFromToken()
- JWT subject에서 email 추출

getRoleFromToken()
- JWT claim에서 role 추출
</code></pre><p>JWT는 암호화된 값이 아니라 서명된 토큰이기 때문에, Payload 내용은 확인할 수 있지만 Secret Key 없이 위조할 수 없도록 구성했습니다.</p><hr><h2>5. JwtAuthenticationFilter 구현</h2><p>JWT 인증은 Controller나 Service에서 처리하지 않고, Controller 실행 이전에 Filter에서 처리하도록 구현했습니다.</p><p>처리 흐름은 다음과 같습니다.</p><pre><code class="language-text">Request
↓
Authorization Header 조회
↓
Bearer Token 확인
↓
Bearer 제거 후 JWT 추출
↓
JwtTokenProvider.validateToken()
↓
email, role 추출
↓
SimpleGrantedAuthority 생성
↓
UsernamePasswordAuthenticationToken 생성
↓
SecurityContextHolder에 Authentication 저장
↓
Controller 실행
</code></pre><p>이를 통해 Controller와 Service는 인증 검증 코드를 직접 작성하지 않아도 되고, Spring Security가 공통적으로 인증을 처리할 수 있게 되었습니다.</p><h3>실행 결과</h3>
<p>스크린샷 첨부: Postman Authorization 탭에 Bearer Token 설정한 화면</p>
<img width="725" height="574" alt="로그인 Token 테스트" src="https://github.com/user-attachments/assets/f60f8239-f35b-429e-bd99-9c4d2daa726f" />

<p>스크린샷 첨부: JWT 인증 후 즐겨찾기 조회 성공 화면</p>
<img width="723" height="689" alt="securityFilterChain JWT 필터 등록 테스트 성공" src="https://github.com/user-attachments/assets/3e80f601-8cbe-40e6-9295-a247df616905" />

<hr><h2>6. Role 기반 권한 제어</h2><p>USER와 ADMIN 권한을 구분하여 API 접근을 제어했습니다.</p><p>권한 처리 흐름은 다음과 같습니다.</p><pre><code class="language-text">USER 로그인
↓
JWT 발급
↓
POST /api/facilities 요청
↓
hasRole("ADMIN") 검증 실패
↓
403 Forbidden
</code></pre><pre><code class="language-text">ADMIN 로그인
↓
JWT 발급
↓
POST /api/facilities 요청
↓
ROLE_ADMIN 확인
↓
시설 등록 성공
</code></pre><h3>실행 결과</h3><p>스크린샷 첨부: USER 권한으로 시설 등록 시 403 Forbidden 화면</p>
<img width="727" height="441" alt="user 권한 테스트 403 오류 발생" src="https://github.com/user-attachments/assets/8a5dd930-d414-43ef-8634-30ba10bab2ea" />

<p>스크린샷 첨부: ADMIN 권한으로 시설 등록 
<img width="724" height="568" alt="ADMIN 권한 테스트 시설 등록 성공 200 OK" src="https://github.com/user-attachments/assets/5efe923c-cf89-48c0-a9a7-1092ba31eee0" />
시 200 OK 화면</p>

<hr><h2>7. Global Exception Handler 적용</h2><p>프로젝트 전체에서 발생하는 예외 응답 형식을 통일하기 위해 GlobalExceptionHandler와 ErrorResponse를 적용했습니다.</p><p>기존에는 예외 발생 시 Spring 기본 에러 응답이 반환되었지만, 수정 후 다음 형식으로 통일했습니다.</p><pre><code class="language-json">{
  "status": 400,
  "message": "이메일 또는 비밀번호가 일치하지 않습니다.",
  "time": "2026-06-27T20:55:13.8687687"
}
</code></pre><p>처리 흐름은 다음과 같습니다.</p><pre><code class="language-text">Service에서 예외 발생
↓
IllegalArgumentException
↓
GlobalExceptionHandler
↓
ErrorResponse 생성
↓
공통 JSON 응답 반환
</code></pre><h3>실행 결과</h3>
<p>스크린샷 첨부: 로그인 실패 시 ErrorResponse 형식으로 반환되는 화면</p>
<img width="710" height="468" alt="로그인 예외 처리 추가" src="https://github.com/user-attachments/assets/8e722485-8309-43f8-84d4-8c82efd5b9ce" />

<hr><h1>트러블 슈팅</h1><h2>1. ADMIN 권한으로 로그인했지만 403 Forbidden 발생</h2><h3>문제 상황</h3><p>ADMIN 계정으로 로그인한 뒤 JWT를 Authorization Header에 담아 시설 등록 API를 호출했지만 403 Forbidden이 발생했습니다.</p><pre><code class="language-text">ADMIN 로그인
↓
JWT 발급
↓
POST /api/facilities
↓
403 Forbidden
</code></pre><h3>원인</h3><p>JWT 생성 시 role Claim이 Spring Security가 기대하는 권한 형식과 다르게 저장되었습니다.</p><p>Spring Security의 <code inline="">hasRole("ADMIN")</code>은 내부적으로 <code inline="">ROLE_ADMIN</code> 권한을 확인합니다.</p><p>하지만 JWT에서 role 값을 꺼내 권한 객체로 변환하는 과정에서 role 값이 정상적으로 문자열로 처리되지 않아 <code inline="">ROLE_ADMIN</code>으로 인식되지 않았습니다.</p><h3>해결 방법</h3><p>JWT 생성 시 role 값을 문자열로 변환하여 저장했습니다.</p><pre><code class="language-java">.claim("role", user.getRole().toString())
</code></pre><p>그리고 JwtAuthenticationFilter에서 Spring Security 권한 형식에 맞게 <code inline="">ROLE_</code> 접두사를 추가했습니다.</p><pre><code class="language-java">SimpleGrantedAuthority authority =
        new SimpleGrantedAuthority("ROLE_" + role);
</code></pre><h3>결과</h3><pre><code class="language-text">USER 토큰
↓
POST /api/facilities
↓
403 Forbidden
</code></pre><pre><code class="language-text">ADMIN 토큰
↓
POST /api/facilities
↓
200 OK
</code></pre><h3>학습한 점</h3><p>JWT Claim에 저장되는 값의 형식과 Spring Security의 권한 체계가 일치해야 한다는 점을 학습했습니다.</p><p>특히 <code inline="">hasRole("ADMIN")</code>은 내부적으로 <code inline="">ROLE_ADMIN</code>을 검사한다는 점을 이해했습니다.</p><hr><h2>2. GlobalExceptionHandler Bean 충돌 발생</h2><h3>문제 상황</h3><p>GlobalExceptionHandler를 새로 생성한 뒤 애플리케이션 실행 시 BeanDefinitionStoreException이 발생했습니다.</p><h3>원인</h3><p>기존에 <code inline="">global.GlobalExceptionHandler</code>가 이미 존재했는데, 새로 <code inline="">exception.GlobalExceptionHandler</code>를 추가하면서 동일한 Bean 이름이 중복 등록되었습니다.</p><p>Spring은 기본적으로 클래스명을 기반으로 Bean 이름을 생성하기 때문에 두 클래스 모두 <code inline="">globalExceptionHandler</code>라는 Bean 이름으로 등록되면서 충돌이 발생했습니다.</p><h3>해결 방법</h3><p>기존 GlobalExceptionHandler를 유지하고, 새로 만든 ErrorResponse를 기존 Handler에 적용하는 방식으로 구조를 통합했습니다.</p><p>즉 GlobalExceptionHandler는 하나만 유지하고, 응답 형식만 ErrorResponse 기반으로 수정했습니다.</p><h3>학습한 점</h3><p>공통 기능을 담당하는 Bean은 중복 생성하지 않고 하나의 책임으로 관리해야 한다는 점을 학습했습니다.</p><p>또한 Spring Bean 이름 충돌이 발생할 수 있으므로 패키지만 다르더라도 같은 클래스명을 가진 컴포넌트가 여러 개 존재하는 경우 주의해야 한다는 점을 알게 되었습니다.</p><hr><h2>3. 로그인 API가 403 Forbidden으로 차단됨</h2><h3>문제 상황</h3><p>SecurityConfig에서 대부분의 API를 인증 필요로 변경한 뒤 로그인 API 호출 시 403 Forbidden이 발생했습니다.</p><h3>원인</h3><p>로그인 API는 인증 전 사용자가 접근해야 하는 API인데, <code inline="">/api/login</code>을 permitAll() 대상으로 등록하지 않아 Spring Security가 로그인 요청 자체를 차단했습니다.</p><h3>해결 방법</h3><p>SecurityConfig에 로그인 API를 공개 API로 추가했습니다.</p><pre><code class="language-java">.requestMatchers(HttpMethod.POST, "/api/login").permitAll()
</code></pre><h3>학습한 점</h3><p>인증을 수행하는 API는 인증이 없어도 접근 가능해야 하므로 Security 설정에서 반드시 예외적으로 허용해야 한다는 점을 학습했습니다.</p><hr><h1>테스트</h1><p>다음 항목을 Postman과 MySQL을 통해 검증했습니다.</p>
테스트 항목 | 결과
-- | --
회원가입 시 BCrypt 암호화 저장 | 성공
로그인 성공 시 JWT 발급 | 성공
잘못된 비밀번호 로그인 실패 | 성공
존재하지 않는 이메일 로그인 실패 | 성공
Authorization Header에 Bearer Token 전달 | 성공
JWT 인증 후 즐겨찾기 조회 | 성공
USER 권한으로 시설 등록 차단 | 성공
ADMIN 권한으로 시설 등록 성공 | 성공
Global Exception 공통 응답 확인 | 성공

<hr><h1>학습 내용</h1><h2>BCrypt</h2><p>BCrypt는 비밀번호 저장을 위한 단방향 해시 방식입니다.</p><p>동일한 비밀번호라도 Salt가 다르기 때문에 서로 다른 해시값이 생성됩니다.</p><p>로그인 시에는 복호화하지 않고 <code inline="">PasswordEncoder.matches()</code>를 이용해 입력 비밀번호와 저장된 해시값을 비교합니다.</p><hr><h2>JWT</h2><p>JWT는 로그인 성공 후 발급되는 인증 토큰입니다.</p><p>서버가 세션을 저장하지 않고, 클라이언트가 요청마다 JWT를 전달하여 인증 상태를 증명합니다.</p><p>JWT는 암호화가 아니라 서명 기반 토큰이므로 Payload 내용은 확인할 수 있지만 Secret Key 없이 위조할 수 없습니다.</p><hr><h2>Authorization Header</h2><p>JWT는 다음 형식으로 전달합니다.</p><pre><code class="language-text">Authorization: Bearer {token}
</code></pre><p>URL은 로그나 브라우저 기록에 남을 수 있고, Body는 GET 요청에서 사용하기 어렵기 때문에 HTTP 인증 표준인 Authorization Header를 사용했습니다.</p><p>실제 운영 환경에서는 HTTPS를 통해 Header와 Body가 TLS로 암호화되어 전송되어야 합니다.</p><hr><h2>JwtAuthenticationFilter</h2><p>JwtAuthenticationFilter는 Controller 실행 전에 JWT를 검증하는 역할을 합니다.</p><p>이를 통해 Controller나 Service마다 인증 검증 코드를 작성하지 않고, Spring Security Filter Chain에서 공통적으로 인증을 처리할 수 있습니다.</p><hr><h2>SecurityContext</h2><p>SecurityContext는 현재 요청의 인증 정보를 저장하는 공간입니다.</p><p>JwtAuthenticationFilter에서 Authentication 객체를 생성해 SecurityContext에 저장하면, 이후 Spring Security는 해당 요청을 인증된 사용자 요청으로 판단합니다.</p><hr><h2>인증과 인가</h2><p>인증(Authentication)은 사용자가 누구인지 확인하는 과정입니다.</p><p>인가(Authorization)는 인증된 사용자가 특정 기능에 접근할 권한이 있는지 확인하는 과정입니다.</p><p>이번 구현에서는 JWT를 통해 인증을 처리하고, Role을 통해 인가를 처리했습니다.</p><hr><h1>회고</h1><p>이번 기능을 구현하면서 Spring Security의 인증과 인가 흐름을 실제 코드로 이해할 수 있었습니다.</p><p>처음에는 JWT를 단순히 로그인 토큰이라고 생각했지만, JwtAuthenticationFilter를 구현하면서 JWT가 SecurityContext에 Authentication을 등록하기 위한 인증 수단이라는 것을 이해했습니다.</p><p>또한 BCrypt의 단방향 해시 구조와 PasswordEncoder.matches()의 동작 방식을 학습하면서 비밀번호를 복호화하지 않고 검증하는 방식을 이해했습니다.</p><p>Role 기반 권한 제어 과정에서 Spring Security의 hasRole()이 내부적으로 ROLE_ 접두사를 사용하는 구조도 확인했습니다.</p><p>마지막으로 Global Exception Handler를 적용하며 API 응답 형식을 통일하는 것이 프론트엔드 연동과 유지보수에 중요하다는 점을 학습했습니다.</p><hr><h1>향후 개선 예정</h1><ul><li><p>Refresh Token 적용</p></li><li><p>Access Token 재발급 기능 구현</p></li><li><p>Redis를 이용한 Refresh Token 관리</p></li><li><p>OAuth2(Google/Kakao) 로그인 연동</p></li><li><p>로그인 사용자 기반 API(<code inline="">/me</code>) 구현</p></li><li><p>시설 검색 성능 개선(Index + EXPLAIN)</p></li><li><p>프론트엔드 연동</p></li></ul></body></html><!--EndFragment-->
</body>
</html>
