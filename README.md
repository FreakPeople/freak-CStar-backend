# 🍭 CStar 백엔드 레포지토리

<div align='center'>

  <img src = "https://github.com/user-attachments/assets/b9fb33d8-d512-4818-a344-780cc0405efb" width="75%">

  <h3>컴퓨터 공학 퀴즈 웹 애플리케이션 프로젝트 입니다.</h3>

  <a href='https://topaz-raincoat-203.notion.site/CStar-febbaabc63204da28f4beb91346c1814'>📒 팀 노션</a> |
  <a href='https://github.com/FreakPeople/freak-CStar-frontend'>🎨 프론트엔드 레포지토리</a> |
  <a href='https://github.com/FreakPeople/freak-CStar-backend/pulls?page=2&q=is%3Apr+is%3Aclosed'>🎨 이슈 & PR 관리</a>
</div>

<br><br>

## 1. 기술 스택
- 백엔드
  - Kotlin 1.9.24
  - SpringBoot 3.3.2
  - JUnit
  - MySQL 8.0.35
  - Redis 7.2
  - WebSocket
  - Docker
  - GitHub Actions
  - AWS

<br><br>

## 2. 프로젝트 구조

### 1. 아키텍처

<kbd><img src = "https://github.com/user-attachments/assets/8ec2219a-f4d3-4e52-ab64-102835e341a8" style="border-radius: 10px;"></kbd>
<br><br>
### 2. 패키지 구조

```
기본경로 : main ──> kotlin ──> yjh ──> cstar

main...
├── cstar
│    ├── auth
│    ├── category
│    ├── common
│    ├── config
│    ├── play
│    ├── game
│         ├── application
│         ├── domain
│         ├── infrastructure
│         ├── presentation
│    ├── member
│    ├── quiz
│    ├── room
│    ├── util
│    ├── chat
│    │
│    ├── CstarApplication.kt
│
test...
├── cstar
     ├── play
     ├── game
     ├── jpa
     ├── member
     ├── quiz
     ├── redis
     ├── room
     ├── chat
     ├── IntegrationTest.kt
```
### main
- auth : 인증 도메인을 구현한다
- category : 퀴즈의 카테고리 도메인을 구현한다
- common : 모든 도메인에서 공통적으로 사용하는 기능(공통 예외, 공통 응답, 예외 핸들러 등)을 포함한다
- config : 설정 파일을 정의한다
- play : 실시간 게임 로직을 구현한다
- member : 회원 도메인을 구현한다
- quiz : 퀴즈 도메인을 구현한다
- room : 게임방 도메인을 구현한다
- util : 공통 유틸 클래스를 정의한다(redis util)
- chat : 웹소켓 핸들러 및 Stomp Message 컨트롤러를 정의한다

### test
- 도메인별로 테스트가 정의되어 있다
  - application : 데이터베이스와 연동된 서비스 계층 통합 테스트 작성
  - concurrency : 동시성 문제 테스트 작성
  - domain : 도메인별 핵심 비즈니스 로직 단위 테스트 작성
  - IntegrationTest.kt 통합 테스트 관련 공통 추상 클래스

<br><br>


## 3. 개발 환경 구축
- back-end 와 front-end 서버를 로컬환경에서 실행시키고 테스트할 수 있습니다.
- 아래의 단계에 따라 로컬환경에서 순차적으로 실행하면 됩니다.
- docker를 컨테이너로 애플리케이션을 실행시키기 때문에 docker가 설치되어 있어야 합니다.

### 1. 프로젝트 클론
```
git clone https://github.com/FreakPeople/freak-CStar-backend.git
```

### 2. 도커 컴포즈 명령어 실행
- 터미널의 프로젝트 최상위 디렉토리에서 아래의 명령어를 실행합니다.
```
cd docker
docker-compose up -d
```

### 3. 테스트 실행
- mac os 환경
```
./gradlew clean test
```
- window 환경
```
gradlew clean test
```

<br><br>


## 4. ERD 다이어그램
<div align='center'>
    <kbd><img width="928" alt="erd" src="https://github.com/user-attachments/assets/164ee44b-33fe-466c-8053-ff193e5176ec" style="border-radius: 10px;"></kbd>
</div>

<br><br>

## 5. API 명세서

### 인증 API
<details>
<summary>로그인</summary>

`POST /v1/authenticate`
```
Request
{
  "email" : "string",
  "password" : "string"
}
```

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
    "string"
  }
}
```
---
</details>

### 회원 API
<details>
<summary>회원 등록</summary>

`POST /v1/members`

```
Request
{
  "name" : "string",
  "password" : "string",
  "nickname" : "string"
}
```

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : int
}
```
---
</details>
<details>
<summary>내 정보 조회</summary>

`GET /v1/members/me`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
    "id": int,
    "email": "string",
    "nickname": "string",
    "createdAt": "YYYY-MM-DDTHH:MM:SS",
    "updatedAt": "YYYY-MM-DDTHH:MM:SS"
  }
}
```
---
</details>

### 퀴즈 API
<details>
<summary>퀴즈 등록</summary>

`POST /v1/quizzes`

```
Request
{
  "question" : "string",
   "answer" : "string",
   "categoryId" : int
}
```

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : int
}
```
---
</details>
<details>
<summary>카테고리별 퀴즈 조회</summary>

`GET /v1/quizzes`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
      "totalPages": int,
      "totalElements": int,
      "first": boolean,
      "last": boolean,
      "size": int,
      "content": [
          {
            "writerId": int,
            "question": "strign",
            "answer": "string",
            "categoryId": int
          },
          ....
}
```
---
</details>
<details>
<summary>회원이 생성한 퀴즈 조회</summary>

`GET /v1/quizzes/filter?quizFilterType=created`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
      "totalPages": int,
      "totalElements": int,
      "first": boolean,
      "last": boolean,
      "size": int,
      "content": [
          {
            "writerId": int,
            "question": "string",
            "answer": "string",
            "categoryId": int
          }
      ],
      ...
}
```
---
</details>
<details>
<summary>회원이 시도한 퀴즈 조회 (푼 문제 + 풀지 못한 문제)</summary>

`GET /v1/quizzes/filter?quizFilterType=attempted`

```
Response / 200 OK
{
 "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
      "totalPages": int,
      "totalElements": int,
      "first": boolean,
      "last": boolean,
      "size": int,
      "content": [
          {
            "writerId": int,
            "question": "string",
            "answer": "string",
            "categoryId": int
          }
      ],
      ...
}
```
---
</details>

### 게임 API
<details>
<summary>게임 생성</summary>

`POST /v1/games`

```
Request
{
   "roomId": int,
    "quizCategoryId" : int,
    "totalQuestions" : int
}
```

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : null
}
```
---
</details>

### 게임방 API
<details>
<summary>게임방 생성</summary>

`POST /v1/rooms`

```
Request
{
   "maxCapacity" : int
}
```

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : int
}
```
---
</details>
<details>
<summary>게임방 단건 조회</summary>

`GET /v1/rooms/{id}`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
      "id": int,
      "maxCapacity": int,
      "currCapacity": int,
      "status": "string",
      "createdAt": "YYYY-MM-DDTHH:MM:SS",
      "updatedAt": "YYYY-MM-DDTHH:MM:SS",
      "deletedAt": null
    }
}
```
---
</details>
<details>
<summary>게임방 전체 조회</summary>

`GET /v1/rooms`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : {
      "totalPages": int,
      "totalElements": int,
      "first": boolean,
      "last": boolean,
      "size": int,
      "content": [
          {
            "id": int,
            "maxCapacity": int,
            "currCapacity": int,
            "status": "string",
            "createdAt": "YYYY-MM-DDTHH:MM:SS",
            "updatedAt": "YYYY-MM-DDTHH:MM:SS",
            "deletedAt": null
          },
          ...
}
```
---
</details>
<details>
<summary>게임방 참여 요청</summary>

`POST /v1/rooms/{id}`

```
Response / 200 OK
{
  "status" : "string",
  "code" : int
  "message" : "string",
  "data" : int
}
```

---
</details>

<br><br>

## 6. 화면 구성

https://github.com/user-attachments/assets/9be8b1de-274b-4e2d-999c-ae23b4056851

<br><br>


## 7. 팀원
|                                   BackEnd                                    |                                    BackEnd                                    |
|:----------------------------------------------------------------------------:|:-----------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/78125105?v=4" width="100"> | <img src="https://avatars.githubusercontent.com/u/87998104?v=4" width="100"> |
|                         [황유정](https://github.com/youjungHwang)                          |                      [정지훈](https://github.com/Jeongjjuna)                      |

