# 5.3 프로파일 사용해서 구성하기

애플리케이션이 서로 다른 런타임 환경에 배포, 설치될 때는 대개 구성 명세가 달라진다. 데이터베이스 연결 명세가 개발 환경과 다를 것이고, 프로덕션 환경도 여전히 다를 것이다. 이 때는 `application.preperties`나 `application.yml`에 정의하는 것보다 `운영체제의 환경변수`를 사용해서 구성하는 것이 한 가지 방법이다.

 예를들어, **개발 시점**에는 자동-구성된 내장 H2 데이터베이스를 사용할 수 있다. 그러나 **프로덕션 환경**에서는 다음과 같이 환경 변수로 데이터베이스 구성 속성을 설정해야 한다.
 
```shell
% export SPRING_DATASOURCE_URL=jdbc:mysql://localhost/tacocloud
% export SPRING_DATASOURCE_UUSERNAME=tacouser
% export SPRING_DATASOURCE_PASSWORD=tacopassword
```

그러나 이렇게 개발 프로세스의 각 단계마다 환경변수를 바꾸거나 여러가지 구성 속성을 환경변수로 지정하는 것은 번거롭고, **확장과 변경에 유연하지 않은 것**이 단점이다.

그래서 보통 스프링 개발자들은 운영체제의 환경변수에 구성속성을 지정하는 것 대신에 스프링이 제공하는 `스프링 프로파일` 방식을 선호한다.

## 5.3.1 프로파일 특정 속성 정의하기

이제 스프링 프로파일에 구성 속성을 설정하는 방법을 알아보자. 

프로파일에 특정 속성을 정의하는 방법은 크게 두 가지가 존재한다.

1. `application-[프로파일 이름].yml` 또는 `application=[프로파일 이름].properties` 를 각각 생성
2. `application.yml `파일 안에 **공통속성 + 각각의 프로파일의 속성**을 나누어 정의

위 두 가지 방법으로 프로파일 구성 속성을 나누어 정의하기 전에 예시를 들 것이다. 예를들어, 개발과 디버깅 목적으로 내장 H2 데이터베이스를 사용하고, 타코 클라우드 코드의 로깅 수준을 **DEBUG**로 설정한다. 그리고 프로덕션 환경에서는 외부의 MySQL 데이터베이스를 사용하고 로깅 수준은 **WARN**으로 설정한다.

이 때, 첫 번째 방법에서 프로덕션 환경의 구성속성은 다음과 같이 파일을 생성해서 작성할 수 있다.

**application-prod.yml**

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost/tacocloud
    username: tacouser
    password: tacopassword
logging:
  level:
    tacos: WARN
```

두 번째 방법에서의 프로파일 구성 속성 적용방법은 다음과 같이 한 yml 파일에 공통 속성과 함께 작성할 수 있다. 이 방법에서는 프로덕션 환경이 아닌 다른 환경의 프로파일에서는 기본적으로 **하이픈(---)** 위쪽 부분의 구성 속성이 공통적으로 적용된다. 그리고 prod 프로파일이 활성화되면 `logging.level.tacos` 속성은 **WARN**으로 변경되고 **하이픈(---)** 밑의 구성속성이 활성화된다.

**application.yml**

```yml
logging:
  level:
    tacos: DEBUG
---
spring:
  profiles: prod
  datasource:
    url: jdbc:mysql://localhost/tacocloud
    username: tacouser
    password: tacopassword
logging:
  level:
    tacos: WARN
```

그런데 현재의 스프링 부트 버전에서 profiles 에 직접 프로파일명을 주입하는 방식은 없어졌다고 한다. 그래서 디폴트는 application.yml로 하고 프로덕션 환경의 구성 속성 파일은 application-prod.yml로 따로 구성속성 파일을 만들어서 하였다.

## 5.3.3 프로파일을 사용해서 조건별로 빈 생성하기

서로 다른 프로파일 각각에 적합한 빈들을 제공하는 것이 유용할 때가 있다. 일반적을 자바 빈들은 프로파일과 무관하게 생성된다. 

그러나 특정 프로파일에 따라서 생성되어야 하는 파일이 있고 아닌 경우가 필요하다고 생각해보자. 이 때, `@Profile` 애노테이션을 지정해서 프로파일명을 지정하면 특정 프로파일에만 빈들이 등록된다.

우리는 타코 클라우드 애플리케이션에서 식자재 데이터를 런타임시에 로드하기 위해서 CommandLineRunner 빈이 사용된다. 이렇게 식자재 데이터를 로드하는 것은 개발시점에는 개발하는데 유용하게 쓰일 것이다. 하지만 프로덕션 환경에서 애플리케이션을 사용할 때는 불필요한 빈이 생성되는 것이 될 것이다.

이런 경우 다음과같이 CommandLineRunner 빈 메서드에 `@Profile`을 지정하고 프로파일명을 작성하면 프로덕션 환경에서 런타임시에 식자재 데이터가 로드되는 것을 방지할 수 있다.

```java
@Bean
@Profile("!prod")
public CommandLineRunner dataLoader(IngredientRepository repo){}
```

이렇게 작성하면 prod 프로파일을 제외한 모든 프로파일에서 빈이 생성되게 된다. 반대로 @Profile("prod") 로 바꾸면 프로덕션 환경에서만 빈이 생성되게 할 수도 있다.

또한 다음과 같이 여러가지 프로파일을 지정할 수도 있다.

```java
@Profile({"dev", "qa"})
```