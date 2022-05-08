## 스프링 애플리케이션 초기설정

스프링 애플리케이션을 초기 설정하는 방법은 여러가지가 있다. 그 중에서도 `Spring Initializr`를 사용하여 초기설정 하는 것을 추천한다.

> Spring Initializr : REST API를 사용하는 브라우저 기반의 웹 애플리케이션으로, 우리가 원하는 기능을 구현할 수 있는
> 스프링 프로젝트를 자동으로 만들어준다.

`Spring Initializr`는 다음 방법들 중에 한 가지를 골라서 설정하면 되는데, 다음 세가지 방법을 추천한다.

- [스프링 스타터](https://start.spring.io)
- [Intellij IDEA IDE를 사용한 새로운 프로젝트 생성](https://www.jetbrains.com/help/idea/your-first-spring-application.html)
- [Spring Tools Suite IDE를 사용해서 새로운 프로젝트 생성](https://spring.io/tools)

### JAR(Java ARchive)와 WAR(Web application ARchive)의 차이점

#### JAR(Java ARchive)

- 라이브러리와 데스크톱 UI 애플리케이션의 `패키징`에 사용한다.
- **대부분**의 `클라우드 플랫폼`에 호환된다.

#### WAR(Web Application ARchive)

- 기존의 자바 웹 애플리케이션의 `패키징`
- **일부** `클라우드 플랫폼`에 호환된다.

## 스프링 프로젝트 구조

- [mvnw](./mvnw) 와 [mvnw.cmd](mvnw.cmd) : 이 파일들은 메이븐 래퍼 스크립트로, 메이븐이 프로젝트를 빌드하는 데 쓰이는 파일이다.
- [pom.xml](./pom.xml) : 메이븐이 프로젝트를 빌드할 때 필요한 정보(명세)가 들어있는 파일이다.
- [TacoCloudApplication](./src/main/java/sia/tacocloud/TacoCloudApplication.java) : 스프링 부트 메인 클래스
- [application.properties](./src/main/resources/application.properties) : 프로젝트의 세부적인 구성 속성을 정의할 수 있는 파일이다.
- [static](./src/main/resources/static) : 브라우저에 제공할 정적 콘텐츠(img, CSS, JavaScript 등)를 저장해놓는 폴더이다.
- [templates](./src/main/resources/templates) : 브라우저에 콘텐츠를 보여주는 템플릿 파일을 두는 폴더이다.
- [TacoCloudApplicationTests.java](./src/test/java/sia/tacocloud/TacoCloudApplicationTests.java) : 스프링 애플리케이션이 성공적으로 로드되는지 확인하는 간단한 테스트 클래스이다.

### 빌드 명세

[빌드 명세 바로가기](./pom.xml)

> `<parent>` 요소의 `<version>` : 우리 프로젝트가 부모 POM(Project Object Model)으로 spring-boot-starter-parent를
> 갖는다는 것을 지정한다. 이 부모 POM은 스프링 프로젝트의 여러 라이브러리의 의존성 관리를 제공한다. 따라서 스프링 부트의 버전만 기록하면
> 다른 라이브러리들의 버전을 지정할 필요가 없다.(스프링 부트 버전에 의해 의존성 관리를 제공)

빌드 명세서의 제일 끝단의 plugin 태그는 다음과 같은 역할을 한다.
- 메이븐을 사용하는 애플리케이션을 실행
- 의존성이 있는 모든 라이브러리가 JAR파일에 포함되어 있는지 확인
- 런타임 시에 classpath를 찾을 수 있는지 확인
- 메인 부트스트랩 파일(TacoCloudApplication)을 나타내는 부트스트랩 파일을 JAR파일에 생성

### 애플리케이션 부트스트랩(구동)

우리가 Spring Initializr로 스프링 프로젝트를 생성하면 생기는 main 클래스를 `부트스트랩 클래스`라고 한다. 여기서는 TacoCloudApplication.java가 될 것이다.

해당 클래스에는 @SpringBootApplication 이라는 애노테이션이 붙어있다. 이 애노테이션안에는 여러가지 애노테이션들이 존재하는데, 각각에 대한 설명은 다음과 같다.

**@SpringBootApplication**

- @SpringBootConfiguration : 현재 클래스(TacoCloudApplication)를 구성 클래스로 지정한다. @Configuration과 거의 유사한 애노테이션이며, 다른점은 @SpringBootConfiguration은 이 클래스의 패키지와 하위패키지의 모든 @Configuration(구성 클래스)을 탐색해서 빈(객체)로 등록한다.
- @EnableAutoConfiguration : 스프링 부트 자동-구성을 활성화한다. 즉, @ComponentScan으로 빈이 등록된 이후, 추가적인 Bean들을 읽어 등록하는 애노테이션이다. 우리가 커스텀하게 등록한 빈 이외에
  스프링이 필요로 하는 빈들을 자동으로 등록한다.
- @ComponentScan : 컴포넌트 검색을 활성화한다. @Component, @Controller, @Repository 등의 애노테이션이 붙어있는 클래스들을 자동으로 찾아 스프링 컨테이너에 컴포넌트로 등록한다.
  따로 기본 패키지를 설정하지 않는다면, 현재 부트스트랩 패키지를 포함한 하위 패키지에 있는 컴포넌트들을 모두 빈으로 등록한다. 또한, 컴포넌트(빈) 객체로 등록하지 않을 애노테이션이나 추가적으로 등록할 커스텀 애노테이션을 추가할 수 있다.

[부트스트랩 클래스 바로가기](./src/main/java/sia/tacocloud/TacoCloudApplication.java)

> 대부분의 애플리케이션에서는 자동-구성되지 않는 것을 고려하여 TacoCloudApplication 과 같은 구성클래스 외에
> 별도의 구성클래스를 하나 이상 생성하는 것이 좋다.