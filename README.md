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


## API 설계

[https://www.notion.so/growing-everyday-chan/API-49095a4652ab4ea0bf30d4d8a1939fe5?pvs=4](https://www.notion.so/API-49095a4652ab4ea0bf30d4d8a1939fe5)

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
