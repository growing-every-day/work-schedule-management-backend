# 📆 연차/당직 관리 시스템- 🛠️ 백엔드

연차/당직 관리 시스템 - 백엔드 부분을 구현 했습니다🎉 🎉 🎉 

2023년 3월 기준 가장 최신의 스프링 부트와 관련 기술들, 자바 17 기능들, 개발 도구들을 경험할 수 있도록 만들었습니다‼️


## 🧑🏻‍💻 멤버

| 담당 | 총괄&회원정보 관련 API| 연차&당직 API | 로그인 API| 회원가입 API|
| --- | --- | ----------- | ------ | ------ |
| 이름 | 황슬찬 |민장규 |이연희| 김인후 |
|github| [@seulchan](https://github.com/seulchan)|[@MinKevin](https://github.com/MinKevin)|[@lyh951212](https://github.com/lyh951212)|[@itstimi-XD](https://github.com/itstimi-XD)|



## 🧰 기술 세부 스택


### 💻 개발 환경

* ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
* ![Java](https://img.shields.io/badge/java17-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle7.6.1-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![springboot](https://img.shields.io/badge/SpringBoot3.0.4-6DB33F?style=flat-square&logo=Spring&logoColor=white)


### 🕓 Version Control

* ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)


### 💾 Databases

* ![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
* ![Hibernate](https://img.shields.io/badge/H2-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)


### 📚 Frameworks, Platforms and Libraries

* ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
* ![springboot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)
* ![springWeb](https://img.shields.io/badge/SpringWeb-6DB33F?style=flat-square&logo=Spring&logoColor=white)
* ![springDataJpa](https://img.shields.io/badge/SpringDataJPA-6DB33F?style=flat-square&logo=Spring&logoColor=white)
* ![SpringSecurity](https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-square&logo=Spring&logoColor=white)
* ![Spring Boot DevTools](https://img.shields.io/badge/SpringBootDevTools-6DB33F?style=flat-square&logo=Spring&logoColor=white)
* Lombok

### ☁️ Hosting

* ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)


## 연차/당직 관리 시스템 API 설계

### Endpoints

| 종류 | URI | METHOD | 기능 |
| --- | --- | --- | --- |
| VIEW | / | GET | 루트 페이지 |
|  | /error | GET | 에러 페이지 |
|  | /login | GET | 로그인 페이지 |
|  | /signup | GET | 회원가입 페이지 |
|  | /users/{user-id} | GET | 개인정보관리 페이지 |
|  | /schedules/{user-id} | GET | 연차/당직 관리 페이지 |
|  | /schedules | GET | 연차/당직 달력 페이지 |
| API | /api/login | POST | 로그인 요청 |
|  | /api/logout | POST | 로그아웃 요청 |
|  | /api/refresh | POST | 토큰 재발급 요청 |
|  | /api/users | GET | 전체 유저 조회 |
|  | /api/users/{user-id} | GET | 단일 유저 조회 |
|  | /api/users/{user-id}/update | POST | 유저 정보 수정 |
|  | /api/users/{user-id}/delete | POST | 유저 삭제 |
|  | /api/users/signup | POST | 회원가입 |
|  | /api/schedules | POST | 전체 연차/당직 조회 |
|  | /api/schedules/{user-id} | POST | 개인별 연차/당직 조회 |
|  | /api/schedules/{user-id}/create | POST | 연차/당직 등록 |
|  | /api/schedules/{user-id}/update | POST | 연차/당직 수정 |
|  | /api/schedules/{user-id}/delete | POST | 연차/당직 삭제 |

### API Spec
https://www.notion.so/API-49095a4652ab4ea0bf30d4d8a1939fe5?pvs=4#566dad8093ed45ca8aa7d662712f596f

### REST-API 응답 코드 참고

- 200 OK: 클라이언트 요청을 성공적으로 처리했을 때 사용하는 상태 코드
- 201 Created: 클라이언트 요청으로 리소스에 데이터를 성공적으로 생성했을 때 사용하는 상태 코드
- 400 Bad Request: 클라이언트 요청이 유효하지 않음을 의미하는 상태 코드
- 401 Unauthorized: 인증받지 않은 클라이언트가 요청할 때 응답하는 상태 코드
- 403 Forbidden: 인가받지 않은 클라이언트가 요청할 때 응답하는 상태 코드
- 500 Error: 서버 오류로 정상적으로 클라이언트 요청을 처리할 수 없을 때 사용하는 상태 코드

### 에러 처리

| 에러 Class | Http Status code | 에러 코드 | 에러 메시지 | 에러 설명 |
| --- | --- | --- | --- | --- |
| ExpiredJwtException | 400 | 1 | 토큰이 만료됐습니다. |  |
| JwtException | 400 | 2 | 토큰값이 올바르지 않습니다. |  |
| BadRequestException | 400 | 3 | 다른 회원을 삭제할 권한이 없습니다. | 어드민이 아닐 때, 삭제할 때 발생 |
| BadRequestException | 400 | 4 | 회원 번호는 null 일 수 없습니다. | 회원 번호가 null일 때 |
| BadRequestException | 400 | 5 | 회원 번호(%d)를 찾을 수 없습니다 | 데이터베이스에서 회원번호가 없을 때 |
| FieldValidationException | 400 | 6 | 입력한 값이 올바르지 않습니다. | 입력한 값이 검증을 통과하지 못할 때 |
| BadRequestException | 400 | 7 | 회원 정보는 null일 수 없습니다. | 업데이트 요청 시, 아무 정보도 들어오지 않았을 때 |
| BadRequestException | 400 | 8 | 회원 조회에 실패했습니다. | 회원을 조회할 때, 에러가 생겼을 때 |
| BadRequestException | 400 | 9 | 회원 정보 수정에 실패했습니다. | 회원 업데이트할 때, 에러가 생겼을 때 |
| BadRequestException | 400 | 10 | 회원 삭제에 실패했습니다. | 회원 삭제 시도 시, 에러 발생 시 |
| BadRequestException | 400 | 11 | 가입하시려는 아이디가 이미 존재합니다 :( 다른 아이디로 가입해주세요! | 회원가입 시 아이디(username)가 중복되었을 때  |
| InsufficientAuthenticationException | 401 | 12 | 인증되지 않은 사용자입니다. |  |
| AccessDeniedException | 403 | 13 | 권한이 없는 사용자입니다. |  |
| BadRequestException | 400 | 14 | eventId 정보를 받아오는데 실패했습니다. | eventId가 null 일때 |
| BadRequestException | 400 | 15 | 스케줄 정보가 존재하지 않습니다. | 특정 스케줄을 조회, 수정, 삭제를 시도할 때, 해당 스케줄이 존재하지 않을 때 |
| BadRequestException | 400 | 16 | 휴가, 당직 중에 일정은 변경할 수 없습니다. | 휴가, 당직 중에 해당 일정을 변경하려 할 때 |
| InvalidRemainedVacationException | 400 | 17 | 사용가능한 휴가일 수가 부족합니다. 사용가능한 휴가일 수는 %d일 입니다. | 휴가 일정 생성, 수정 시 사용가능한 휴가일 수가 부족할  |
| BadRequestException | 400 | 18 | 유효하지 않은 유저 권한 타입입니다. | UserRoleType 변환 시 존재하지 않는 권한일 때 |
| BadRequestException | 400 | 19 | 다른 회원의 스케쥴을 수정할 권한이 없습니다. | 자신의 스케줄이 아니고 관리자 권한이 없고 다른 회원의 스케줄을 수정하려고 시도할 때 |
| BadRequestException | 400 | 20 | 요청한 날짜가 이미 신청한 당직 또는 휴가 날짜와 중복됩니다. | 휴가, 당직 생성 시, 중복되는 날짜가 있을 때 |
| BadCredentialsException | 400 | 21 | Access Token의 잘못된 계정정보입니다. |  |
| InvalidUsernamePasswordException | 400 | 22 | 아이디 또는 비밀번호가 올바르지 않습니다. | 로그인 시 유저네임이 존재하지 않을 때 |
| 그 외 서버 에러 | 500 | 99 | 서버 오류로 정상적으로 요청을 처리할 수 없습니다. | 서버에서 오류가 발생했을 때 |
|  |  |  |  |  |

## ERD 다이어그램
![Untitled](https://user-images.githubusercontent.com/66657988/229699626-12e4d978-0152-40cd-b679-f5dac6fcd619.png)

## 프로젝트 관리 및 Github Flow 브랜칭 전략

예시 참고용: [https://github.com/djkeh/fastcampus-project-board](https://github.com/djkeh/fastcampus-project-board)

### 프로젝트 관리

1. **Issue 생성**
    1. [https://github.com/orgs/growing-every-day/projects/1](https://github.com/orgs/growing-every-day/projects/1) 이용해서 이슈 생성 및 관리 부탁
2. **Branch**

```
feature/#이슈번호-이름

feature/#3-erd-update
```

1. **Commit**

```
{동작} #이슈번호 - 내용

refactor #28 - getPersonID()
feat #411 - Company class and its public methods
docs #58 - erd 작성
```

- `build`: Build related changes (eg: npm related/ adding external dependencies)
- `chore`: A code change that external user won't see (eg: change to .gitignore file or .prettierrc file)
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation related changes
- `refactor`: A code that neither fix bug nor adds a feature. (eg: You can use this when there is semantic changes like renaming a variable/ function name)
- `perf`: A code that improves performance
- `style`: A code that is related to styling
- `test`: Adding new test or making changes to existing test
1. **Pull Request** 
    1. Pull Request 제목은 이슈와 비슷하게 작성
    2. Pull Request 내용에 `close #이슈번호` 달아주기
    3. 리뷰어 팀장으로 설정
