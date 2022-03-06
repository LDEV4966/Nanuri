# Nanuri Back-end

### 해당 Repository는 Covid-19 SW 해커톤의 나누리 서비스의 백엔드를 구현한 프로젝트 입니다.

## Summary 

지속되는 코로나 19 상황으로 인해 인적 네트워크 형성이 어려운 상황에서, 교육 종사자 및 관련 업계 종사자들의 퇴직 후 사회활동이 줄어든 사람들에게

자신의 가진 재능을 교육 취약계층에게 나누고 사회활동을 할 수 있도록 하는 매칭 커뮤니티 서비스 입니다.

---

## Requirement Specification

### 1. 회원가입 정보  :

- 이름,이메일, 닉네임, 등

    - 인증 방법 : social login (네이버, 카카오)

    - 프로필 이미지, 참여하는 클래스 , 생성한 클래스 목록

### 2. 게시물 (재능 나누기 위한 글)

- 나눔시 필요정보
    - 클래스 이름, 시간, 총 인원 수, 지역 
    - 게시글 내용 ( 한줄 소개, 필요 물품, 등 )
    - 내 게시물일때 신청(수락,거절 가능) 및 참여자 목록 확인 
    - 게시글 상태 (모집중, 모집완료)

---

## DB Architecture

- Conceptual Design : ER Diagram

    -  <div><img width = "700px" height= "600px" src ="https://user-images.githubusercontent.com/40168455/156935589-ab7c3fce-c066-4f0c-a14f-410d2e249360.png">
</div>  

- Logical Design : ER-to-Relational Mapping (Relational Schema)

    -  <div><img width = "700px" height= "600px" src ="https://user-images.githubusercontent.com/40168455/156935738-64309d08-7fd2-45e0-b8bd-523c2aa7c8af.png">
</div>  

      Relational Schema는 The ER-Relational Mapping Algorithm을 기반으로 작성하였으며, 다음을 목표로 하였습니다. 

        1) Preserve all information       
        2) Maintain the constraints to the extent possible 
        3) Minimize null values.

---

## Authentication Flow

    OAuth2 와 JWT 기반 SpringSecurity를 통해 인증 구조를 통해 사용자의 접근을 제어했습니다.
    
- <img width = "600px" height= "450px"  alt="Untitled-3" src="https://user-images.githubusercontent.com/40168455/156935975-3c907d6b-4dcd-46bc-8fa9-de4f4b4cd155.png">

    - #### 프론트 엔드 역할
   
        - Third party authentication provider의 OAuth 서버로 로그인 요청후 Thrid Party 의 토큰을 발급 받아, 백엔드에 전달 (서비스 로그인 요청)
        - 백엔드에서 응답 받은 서비스의 토큰 (JWT -  AccessToken, RefreshToken) 을 저장하여 토큰관리
        - 권한이 필요한 요청마다 adapter interceptor 가 AccessToken을 요청 헤더에 Bearer 토큰 타입으로 함께 보내준다.
        - 서버 Reqest 시 Token Expired 에러 메시지를 받으면 retry interceptor 가 토큰 재발급을 요청하고 이후 응답받은 AccessToken 을 헤더 정보에 담아 다시 요청을 보낸다.
        - RefreshToken이 만료 되었다는 에러 메세지를 받으면 재 로그인 시도를 하여 새로운 토큰을 발급 받는다

    - #### 벡엔드 역할

        - 프론트에서  부터 OAuth의 AccessToken을 전달 받는다.
        - AccessToken으로 사용자 정보를 요청한다.
        - 사용자의 User Id를 PK로 두어 사용자 정보를 데이터 베이스에 저장한다.(이미 존재하는 유저는 업데이트)
        - 사용자 User Id를 기반으로 JWT 토큰을 발급하여 프론트에 AccessToken, RefreshToken 정보를 준다
        - RefreshToken은 사용자 OAuth Id와 함께 DB에 저장 (Redis가 없어 AWS RDS에 저장한다)
        - 사용자가 api 를 요청시 JWT 토큰을 springsecurity filter를 통해 검증한다.
        - 사용자가 AccessToken 재 발급을 요청하면 DB에 저장된 RefreshToken 만료 유/무에 따라 재발급을 해준다. 만약 기한이 다 된 RefreshToken일 경우 에러를 던져주어 재 로그인을 유도한다.
    
---

## Deployment Flow

<img width = "700px" height= "500px"  alt="Untitled-3" src="https://user-images.githubusercontent.com/40168455/156936294-b34d617e-a0a2-4799-991d-47887b13de6d.png">

    서버 개발자는 InteliJ 를 사용해 개발을 진행하며, GitHub에서 코드가 관리 됩니다. 

    GitHub master branch에 푸쉬가 이뤄지면 TravisCI를 통한 테스팅 과정 및 빌드가 진행되고 jar 파일을 AWS S3에 저장합니다.(CodeDeploy에 저장 기능이 없기때문) 

    이 jar 파일을 통해 AWS CodeDeploy에서는 EC2로 배포를 진행합니다.

     EC2 에서 설계된 scprit가 실행되며 nginx가 바라보고 있지 않은 포트를 찾고 새로운 버젼의 배포가 실행됩니다. 

    배포가 정상적으로 실행되었으면 nginx가 바라보는 포트를 새로운 버젼의 포트를 바라보게 스위치 시킵니다.
    
---

## Client (IOS) GitHub 링크 :

[IOS GitHub 링크](https://github.com/ParkGyurim99/Nanuri-iOS)
        
## 📖 Notion 
[프로젝트 소개 링크](https://www.notion.so/Nanuri-34de1123979a49c29524b515dcbed3d4)

## 💁 공모전 
[공모전 링크](https://www.campuspick.com/contest/view?id=16076)


