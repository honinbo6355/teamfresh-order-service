# WMS스쿼드 백엔드(풀스택) 개발자 과제 

## 사용한 기술 스택

- **백엔드**: Java 17, Spring Boot 3.3.3, Gradle 8.10, JPA (Hibernate)
- **데이터베이스**: H2
- **외부 라이브러리**: p6spy(쿼리 파라미터를 로그로 확인하는 용도), lombok(getter, setter 등 반복적인 코드 자동화 용도)

## ERD
https://www.erdcloud.com/d/AEhPphNSufbDzi5ym

## 실행 방법

```bash
./gradlew bootRun
```

## 구현 기능

- 요구사항 기능 개발 완료
- 유닛 테스트, 통합 테스트 작성
- 비관적락을 통한 동시성 대응 작업 및 테스트 완료

## 작업 핵심 내용
- 유저 회원가입/로그인은 없다 가정하고 작업했습니다.
- 상품명, 주문자 주소에 대한 문자열은 정상적으로 입력한다 가정하고 작업했습니다.
- domain과 entity를 분리했습니다. 실무에서는 entity 하나로만 관리해서 JPA 특성을 적극 활용하는 편이지만, 과제 전형 특성상 확장성에 좀 더 무게를 뒀습니다. 영속 계층의 변경시(ex: h2 => redis)에도 domain 객체에는 의존하지 않는 장점이 있다고 판단했습니다.
- dto는 불변객체이기 때문에 record 타입을 활용했습니다.
- DB는 하나라고 가정하고 작업했고, 비관적락으로 재고 동시성을 처리했습니다.
- product 테이블과 product_stock 테이블을 분리했습니다. 주문시 product_stock 레코드에만 비관적락을 잡음으로써, product 테이블 관련한 트랜잭션은 영향을 미치지 않기 위함입니다.
- N건 주문일 경우, Excel 주문 정보를 json으로 파싱한 데이터를 활용해 주문 등록 한다는 가정으로 작업했습니다.
- 주문은 pk외에 orderNum라는 컬럼을 고유한 값으로 제공합니다.

## 기타
- 스프링부트 실행시, data.sql을 통해 product, product_stock 초기 데이터를 생성합니다.
