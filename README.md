## JAVA Spring Boot Web Service Boilerplate
### Requirement
**기술스택**
- 백엔드 : 
>1. JDK : openjdk 17.0.8 2023-07-18
>2. Database : H2(Spring Boot 내장)
>3. Web F/W : 'org.springframework.boot' version '3.1.2'
- 프런트엔드 :
>1. CSS F/W : Bootstrap v3.4.1
>2. JavaScript : jQuery v1.10.2
>3. HTML Template : Thymeleaf
- 개발도구(IDE) : VScode v1.81.1

### 소셜 로그인, 자체로그인 통합
- 회원관리(도메인객체: src/main/java/com/arkwith/starter/user/Member.java)
- 스프링시큐리티에서 상속받아 구현 사용자 객체(src/main/java/com/arkwith/starter/auth/PrincipalDetails.java)

- 자체 로그인
>1. 로그인폼(제작: src/main/resources/templates/pages/user/login.html) -> 
>2. 스프링시큐리티 Controller(내장) -> 
>3. 스프링 시큐리티 Service(제작: src/main/java/com/arkwith/starter/auth/PrincipalDetailsService.java)  ->
>4. 회원관리 도메인 레파지터리 서비스(제작: src/main/java/com/arkwith/starter/user/UserRepository.java)
>5. 로그인 성공시 Q & A 목록 표시

- 소셜 로그인
>1. 로그인폼(제작: src/main/resources/templates/pages/user/login.html) -> 
>2. 스프링시큐리티 Controller(내장) -> 
>3. 각각의 소셜의 받아온 속성값 적재(제작: src/main/java/com/arkwith/starter/auth/OAuthAttributes.java) ->
>4. 스프링 시큐리티 Service(제작: src/main/java/com/arkwith/starter/auth/PrincipalDetailsService.java)  ->
>5. 회원관리 도메인 레파지터리 서비스(제작: src/main/java/com/arkwith/starter/auth/PrincipalOauth2UserService.java)
>6. 로그인 성공시 Q & A 목록 표시

- 참조 :
1. [최신 Spring Security 사용법 - SecurityFilterChain](https://samori.tistory.com/64)
2. [[Spring Security] 기본 구조 및 구현 방법 간단 요약](https://to-moneyking.tistory.com/78)
3. [Spring Security의 구조(Architecture) 및 처리 과정 알아보기](https://dev-coco.tistory.com/174)
3. [Spring Boot 게시판 OAuth 2.0 구글 로그인 구현](https://dev-coco.tistory.com/128)