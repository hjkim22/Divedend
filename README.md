#  📈 Dividend - 주식 배당금 프로젝트
> 웹 페이지 분석과 스크래핑을 통해 데이터를 추출·저장하며, 사용자별 데이터 관리와 배당금 계산, 캐시 서버 구성을 지원하는 프로젝트

## 🛠️ Tech Stack

- **Java**: `17`
- **Spring Boot**: `3.3.3`
- **Gradle**: `8.8`
- **Spring Data JPA**: `3.3.3`
- **Spring Data Redis**: `3.3.3`
- **Spring Security**: `3.3.3`
- **Jsoup**: `1.17.2`
- **Docker**: `27.1.1`
- **Libraries**:
    - `Jsoup`
    - `JWT (JJWT)`
    - `Apache Commons Collections`
    - `JAXB API`
    - `JAXB Runtime`
    - `Lombok`


## ⛓️ 프로젝트 기능
#### **GET** `/finance/dividend/{companyName}`
- 회사 이름을 입력으로 받아 해당 회사의 메타 정보와 배당금 정보를 반환

    - **200 OK**: 회사의 메타 정보와 배당금 정보를 포함하는 JSON 객체가 반환
    - **400 Bad Request**: 잘못된 회사명이 입력된 경우 에러 메시지와 함께 반환

#### **GET** `/company/autocomplete`
- 자동완성 기능을 위한 API. 검색하고자 하는 prefix를 입력으로 받고, 해당 prefix로 검색되는 회사명 리스트 중 10개를 반환
    - **200 OK**: prefix로 검색된 회사명 리스트 중 10개가 포함된 JSON 배열이 반환

#### **GET** `/company`
- 서비스에서 관리하고 있는 모든 회사 목록을 반환
    - **200 OK**: 회사 목록을 포함하는 JSON 객체가 반환. 결과는 `Page` 인터페이스 형태

#### **POST** `/company`
- 새로운 회사 정보를 추가. 추가하고자 하는 회사의 ticker를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
    - **201 Created**: 회사 정보가 성공적으로 추가된 경우.
    - **400 Bad Request**:
        - 이미 보유한 회사의 정보일 경우: 에러 메시지와 함께 반환
        - 존재하지 않는 회사 ticker일 경우: 에러 메시지와 함께 반환

#### **DELETE** `/company/{ticker}`
- ticker에 해당하는 회사 정보를 삭제. 삭제 시 회사의 배당금 정보와 캐시도 모두 삭제
    - **200 OK**: 회사 정보와 관련된 배당금 정보 및 캐시가 삭제된 경우.

#### **POST** `/auth/signup`
- 회원가입 API. 중복 ID는 허용하지 않으며, 패스워드는 암호화된 형태로 저장
    - **201 Created**: 회원가입이 성공적으로 완료된 경우.
    - **400 Bad Request**: 중복 ID인 경우 에러 메시지와 함께 반환

#### **POST** `/auth/signin`
- 로그인 API. 회원가입이 되어 있고 아이디/패스워드 정보가 올바른 경우 JWT 발급
    - **200 OK**: 올바른 아이디/패스워드 정보가 제공된 경우 JWT 반환
    - **401 Unauthorized**: 아이디/패스워드 정보가 올바르지 않은 경우 에러 메시지와 함께 반환

