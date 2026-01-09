# 🚀 Security & JWT 구축 로드맵

### 🟢 Phase 1: 기초 설정 및 환경 구축
- [x] `build.gradle` 의존성 추가 (Spring Security, JJWT)
- [x] `application-secret.yml` 설정 (Secret Key & 유효 시간)
- [x] 작업용 Feature 브랜치 생성 및 원격 push (`feat/security-jwt-auth`)

### 🟡 Phase 2: 인증 객체 및 유틸리티 구현
- [x] **CustomUserDetails & Service 구현**
    - [x] `Member` 엔티티를 포함하는 `PrincipalDetails` 클래스 s 반환 로생성 (PK 필드 포함)
    - [x] `UserDetailsService` 구현 (DB에서 회원 조회 및 Detail직)
- [ ] **JwtTokenProvider 구현**
    - [ ] `createToken`: `Authentication` 정보를 기반으로 JWT 발행 (CustomUserDetails 활용)
    - [ ] `validateToken`: 토큰 위변조 및 만료 시간 검증 (Try-Catch 예외 처리)
    - [ ] `getAuthentication`: 토큰을 파싱하여 `Authentication` 객체로 복구

### 🟠 Phase 3: 시큐리티 필터 체인 연결
- [ ] **JwtAuthenticationFilter 구현**
    - [ ] HTTP Header에서 토큰 추출 로직
    - [ ] `SecurityContextHolder`에 인증 정보 저장 로직 구현
- [ ] **SecurityConfig 설정**
    - [ ] PasswordEncoder 빈 등록 (BCrypt)
    - [ ] 필터 체인 구성 (CSRF disable, Session 무상태 설정, Filter 순서 지정)

### 🔴 Phase 4: 테스트 및 검증
- [ ] 로그인 API 테스트 (토큰 발행 확인)
- [ ] 인증이 필요한 API 호출 테스트 (토큰 검증 확인)

---
## 💡 메모 및 주의사항
- `CustomUserDetails`에서 PK를 꺼내 쓸 수 있도록 `getId()` 메서드 정의 필요.
- `JwtTokenProvider`의 secretKey는 반드시 3