### goal

> Spring 이해하기

### 목차

> Spring application context (+ container, bean) 자바 기반의 구성 Spring(boot) 자동-구성 Spring Initializr(RESTful)
>
> jar와 war의 개념
>
> 스프링 부트 구조 살펴보기 애플리케이션의 부트스트랩 애플리케이션 테스트 클래스

# 1 ] Spring application context

## 1. 개념

- Spring application context는 Spring의 container이다.

  - container

     

    : 라이브러리, 종속 항목들이 패키징된 것이다.

    - 패키징된 container는 실행환경(서버, 데스크탑, 클라우드 등) 어디서나 실행될 수 있도록 돕는다.
    - 소프트웨어 실행 유닛이다.

## 2. 특징

- Spring application context에서는 container를 제공한다.
- container는 애플리케이션 컴포넌트들을 생성하고 관리한다.
- component :: 프로그래밍에 있어 재사용이 가능한 각각의 독립된 모듈
- 컴포넌트들이 모여서 하나의 프로그램을 이룬다.
- Spring application context 내부에서 component와 Bean(빈)들을 연결하여 완전한 애플리케이션을 만든다.

| Bean                | 설 명                                                        |
| ------------------- | ------------------------------------------------------------ |
| 개 념               | Spring IoC(**제어의 역전**, Inversion of Control ) 컨테이너가 관리하는 자바 객체를 bean이라고 한다. - IoC, **제어의 역전** 개념[![image-20220419000016527]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md) |
| 상호 연결 동작 방식 | bean의 상호 연결은 **의존성 주입**(DI, Dependency Injection)**패턴** 기반으로 수행한다. |
| 추가 설명           | - Spring 애플리케이션을 구성하는 핵심 객체 - Spring Ioc 컨테이너에 의해 생성 및 관리되어진다. - class, id, scope, constructor-arg 등을 주요 속성으로 지닌다. |
| 구성 요소           | - class : Bean으로 등록할 Java 클래스 - id : Bean의 고유 식별자 - scope : Bean을 생성하기 위한 방법 - constructor-arg : Bean 생성시 생성자에 전달할 파라미터 - property : Bean 생성시 setter에 전달할 인수 |
| 참고 자료           | - 스프링 빈(Spring Bean)이란? 개념 정리, http://melonicedlatte.com/2021/07/11/232800.html, Easy is Perfect - Bean 정리, [https://velog.io/@gillog/Spring-Bean-%EC%A0%95%EB%A6%AC](https://velog.io/@gillog/Spring-Bean-정리), gillog -[Spring] 빈 등록을 위한 어노테이션 @Bean, @Configuration, @Component 차이 및 비교 , https://mangkyu.tistory.com/75, 망나니개발자 |

- 애플리케이션 component에서 의존(사용)하는 다른 bean(쉽게, **자바의 객체**를)의 생성과 관리를 자체적으로 하지 않고, 별도의 개체(컨테이너)가 해준다. 이 개체에서는 모든 컴포넌트를 생성, 관리하며 해당 컴포넌트를 필요로 하는 bean에게 주입(연결)한다. 일반적으로, 이것은 생성자 인자 또는 속성의 접근자 메서드를 통해 처리된다.

# 2 ] xml 기반의 설정이 아닌, 자바 기반의 구성

- 자바 기반의 구성이 나오기 전까지는 컴포넌트 및 다른 컴포넌트와의 관계를 하나 이상의 xml 파일을 사용해서 상호 연결하도록 스프링 애플리케이션 컨텍스트에 알려주었다.
- 자바 기반의 구성이 나온 스프링 시점부터, xml 기반의 설정이 아닌 자바 기반의 구성(configuration)이 많이 사용된다.
- 자바 기반 구성은 더 강화된 타입 안정과 향상된 리팩토링(refactoring)기능을 제공한다.
  - 나의 생각 : java로 객체지향설계를 함으로써 유지보수 대응이 매우 빠를 것으로 생각한다.

※ xml 기반 관계설정과 자바 기반 구성의 관계설정의 비교

\- 아래의 두 설정은 완전히 동일하다.

- xml설정

```
<bean id = "inventoryService"
	  class = "com.example.InventoryService" />

<bean id = "ProductService"
	  class = "com.example.ProductService" />
	<constructor-arg ref = "inventoryService" />
</bean>

* 넘긴부분
```

- 자바 기반 구성 설정

```
@Configuration
public class ServiceConfiguration {
    @Bean
    public InventoryService inventoryService(){
        return new InventoryService();
    }
    @Bean
    public ProductService productService(){
        return new ProductService(inventoryService());
    }
}
```

## 1. 위의 자바 기반 구성 클래스 코드 이해하기

```
@Configuration
public class ServiceConfiguration {
    @Bean
    public InventoryService inventoryService(){
        return new InventoryService();
    }
    @Bean
    public ProductService productService(){
        return new ProductService(inventoryService());
    }
}
```

- @Configuration

   

  : 설정 파일을 만들기 위한 작업의 annotation (또는, bean을 등록하기 위한 annotation)

  - @Configuration이 붙은 클래스를 자동으로 bean으로 등록하고, 해당 클래스를 parsing해서 @Bean이 있는 메소드를 찾아서 빈을 생성해준다.

- @Bean

   

  : 수동으로 직접 빈을 등록해줘야하는 상황에 사용한다

  - @Configuration이 붙지 않은 클래스에서 @Bean을 이용해 등록해도 동작은 하지만,

  - 싱글톤을 보장 받기 위해

    서는 @Bean 어노테이션은

     

    @Configuration과 같이 사용

    해주자

    - Singleton pattern : 어플리케이션이 시작될 때 어떤 클래스가 최초 한번만 메모리를 할당하고 그 메모리에 인스턴스를 만들어 사용하는 디자인 패턴이다.
    - 참고 자료 : 싱글톤 패턴 정리 및 예제 - 생성 패턴, https://devmoony.tistory.com/43, 코딩무니

  - 주로 상황하는 상황은 아래와 같다.

    - 개발자가 직접 제어가 불가능한 라이브러리를 사용할 때
    - 애플리케이션 전범위적으로 사용되는 클래스를 등록할 때
    - 다형성을 활용하여 여러 구현체를 등록해주어야 할 때

  - 사용의 예시는 아래와 같다

    - [![image-20220419020533791]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)
    - 참고 자료 : [Spring] 빈 등록을 위한 어노테이션 @Bean, @Configuration, @Component 차이 및 비교 - (1/2), https://mangkyu.tistory.com/75, 망나니 개발자

- @Component

   

  : 아직 진도에 나오지 않았기 때문에 생략. 궁금하시다면 아래의 링크를 참고해주세요.

  - 참고 자료 : [Spring] 빈 등록을 위한 어노테이션 @Bean, @Configuration, @Component 차이 및 비교 - (1/2), https://mangkyu.tistory.com/75, 망나니 개발자

# 3 ] 자바 기반의 구성? 아니, 자동-구성 기능!

- Spring은 자동으로 컴포넌트들을 구성할 수 있는 자동-구성 기능을 제공한다.
  - 따라서, xml의 구성이나 자바 구성이 없어도 된다.
  - 자바 구성 설정이나, xml의 구성은 자동-구성을 할 수 없을 경우에 사용한다.

## 1. 자동-구성 기능을 가능하게 하는 스프링의 기법

- 자동-구성 기능은 자동연결(autowiring)과 컴포넌트 검색(component scanning) 기법을 이용한다.
  - **component scanning** : 해당 기법을 사용해 spring은 자동으로 애플리케이션의 classpath에 지정된 컴포넌트를 찾은 후, 스프링 애플리케이션 컨텍스트의 빈으로 생성할 수 있다. 또한, 스프링은 자동 연결을 사용하여 의존 관계가 있는 컴포넌트를 자동으로 다른 빈에 주입(연결)한다.

※ 자동-구성 기능은 Spring boot의 확장으로 자동-구성 기능이 더욱 향상 (생산성 증대)

## 2. 이점

- 애플리케이션을 빌드하는 데 필요한 구성 코드(xml, java)가 현격히 줄어든다.

# 4 ] Spring Initializr(이니셜라이저)

- REST API(RESTful API)를 사용하는 브라우저 기반의 웹 애플리케이션이다.
  - REST(Representational State Transfer) :프로토콜이나 표준이 아닌 아키텍처 원칙 세트
  - API : 애플리케이션 소프트웨어를 구축하고 통합하는 정의 및 프로토콜세트
  - RESTful API로 간주 되기 위한 기준
    - 클라이언트, 서버 및 리소스로 구성되었으며 요청이 HTTP를 통해 관리되는 클라이언트-서버 아키텍처
    - **stateless**(스테이트리스) 클라이언트 - 서버 커뮤니케이션 : 요청 간에 클라이언트 정보가 저장되지 않으며, 각 요청이 분리되어 있고 서로 연결되어 있지 않음을 의미
    - 클라이언트-서버 상호 작용을 간소화하는 캐시 기능 데이터
    - 정보가 표준 형식으로 전송되도록 하기 위한 구성 요소 간 통합 인터페이스
      - 요청된 리소스가 식별 가능하며 클라이언트에 전송된 표현과 분리되어야 한다.
      - 수신한 표현을 통해 클라이언트가 리소스를 조작할 수 있어야 한다.
      - 클라이언트에 반환되는 것을 클라이언트가 정보를 어떻게 처리해야 할지 설명하는 정보가 충분히 포함되어야 한다.
      - 하이퍼미디어 : 클라이언트가 리소스에 액세스한 후, 하이퍼링크를 사용해 현재 수행 가능한 기타 모든 작업을 찾을 수 있어야 한다.
    - 요청된 정보를 검색하는 데 관련된 서버(보안, 로드 밸런싱 등을 담당)의 각 유형을 클라이언트가 볼 수 없는 계층 구조로 체계화하는 계층화된 시스템
    - 코드 온디맨드(선택사항) : 요청을 받으면 서버에서 클라이언트로 실행 가능한 코드를 전송하여 클라이언트 기능을 확장할 수 있는 기능

# 5 ] jar와 war

# 6 ] 스프링 프로젝트 구조 살펴보기

[![image-20220420220926736]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. 가장 기본적인 구조

- src/main/java : 애플리케이션 소스 코드가 저장
- src/test/java : 테스트 코드가 저장
- src/main/resources : 자바 리소스가 아닌 것이 저장

## 2. 주요 항목

| 항목                           | 설명                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| mvnw와 mvnw.cmd                | 스크립트 파일, 즉 maven의 설치가 없더라도 메이븐을 사용할 수 있게 도움 - mvnw :: 유닉스 쉘 스크립트 - mvnw :: 스크립트 배치 |
| pom.xml                        | 메이븐 빌드 명세 (우리 프로젝트에 필요한 정보)를 지정한 파일이다. - 예로, lombok을 클릭하면 lombok과 관련한 정보가 pom.xml에 기입된다. |
| TacoCloudApplication.java      | 스프링 부트 메인 클래스                                      |
| application.properties         | 구성 속성을 지정할 수 있다. - [![image-20220420224449153]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md) |
| static                         | 브라우저에 제공할 정적인 콘텐츠를 둘 수 있는 폴더이다.       |
| templates                      | 브라우저 콘텐츠를 보여주는 템플릿 파일을 두는 폴더이다. - Thymeleaf 템플릿도 이 위치에 저장한다. |
| TacoCloudApplicationTests.java | 스프링 애플리케이션이 성공적으로 로드되는지 확인하는 간단한 테스트 클래스이다. |

# 7 ] 애플리케이션의 부트스트랩(구동)

[![image-20220420230830499]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. @SpringbootApplication

\- @SpringbootApplication의 코드가 스프링 부트 애플리케이션임을 나타낸다.

\- @SpringBootApplication은 아래의 3가지 어노테이션을 결합한 것이다.

| 어노테이션               | 설명                                                         |
| ------------------------ | ------------------------------------------------------------ |
| @SpringBootConfiguration | 현재 클래스를 구성 클래스로 지정한다. - 필요하다면 스프링 프레임워크 구성을 현재 클래스에 추가할 수 있다. |
| @EnableAutoConfiguration | 스프링 부트 자동-구성을 활성화한다. _즉, 우리가 필요로 하는 컴포넌트들을 자동으로 구성하도록 스프링 부트에게 알린다. |
| @ComponentsScan          | 컴포넌트 검색을 활성화한다. - 이것은 @Component, @Controller, @Service 등의 애노테이션과 함께 클래스를 선언할 수 있게 해준다. - 그러면 스프링은 자동으로 그런 클래스를 찾아 스프링 애플리케이션 컨텍스트에 컴포넌트로 등록한다. |

## 2. main()

```
@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

}
```

- 애플리케이션을 시작시키고 스프링 애플리케이션 컨텍스트를 생성하는 SpringApplication 클래스의 run() 메소드를 호출한다.
- main()의 두 개의 매개변수는 구성 클래스와 명령행 인자다.
- 구성 클래스가 부트스트랩 클래스와 반드시 같아야 하는 것은 아니지만 일반적으로 동일하게 지정한다.

## 3. 애플리케이션의 부트스트랩 일반적인 사용

- 간단한 애플리케이션의 경우는 하나 또는 두 개의 다르컴포넌트를 부트스트랩 클래스에 구성하는 것이 편리하다.
- 하지만, 대부분의 애플리케이션에서는 자동-구성되지 않는 것들을 고려하여 별도의 구성 클래스 하나를 생성하는 것이 좋다.

 ※ 구성 클래스란 ? : 잘모르겠음-

# 8 ] 애플리케이션 테스트 클래스

\- **소프트웨어 개발에 있어서 가장 중요한 부분**

[![image-20220420230719093]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

- 의미
  - 이 클래스에는 하나의 테스트 메소드가 있으며, 실행 코드는 없다.
  - **실행코드가 없더라도** 이 테스트 클래스는 Spring application context가 성공적으로 로드될 수 있는지 기본적인 검사를 수행한다.
  - 즉, Spring application context의 생성을 저해하는 코드가 있다면 이 테스트가 실패하게 되므로 문제점을 찾아 해결할 수 있다.

## 1. @SpringbootTest

- @SpringbootTest는 스프링 부트의 기능으로, 테스트를 시작하라는 것을 JUnit에게 알려준다.
  - 즉, main()메소드의 SpringApplication.run()호출에 부합되는 테스트 클래스를 나타내는 것
  - 더 깊은 내용이 있으나 아직 진도에 나오지 않았다.

## 2. @Test 메소드

- @SpringbootTest가 테스트의 **Spring application context를 로드하는 작업**을 수행하더라도 테스트 메소드가 없다면 아무 일도 하지 않는다.
- 하지만, 위 사진처럼 contextLoads()처럼 실행코드는 없더라도 테스트 메소드가 존재할 경우, @SpringBootTest annotation이 작업을 수행하게 되어 Spring application context가 로드된다. 로드 후에, **문제가 없는지 테스트하며, 만일 어떤 문제가 생기면 해당 테스트는 실패**한다.

# 9 ] 웹 요청의 처리 이해 (MVC패턴, @Cotroller, @GetMapping)

[![image-20220420234825928]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. MVC 패턴의 이해

## 2. @Controller 클래스명

- 홈페이지의 **웹 요청(request)을 처리**하는 컨트롤러 클래스
- @Controller 어노테이션을 선언해줌으로써 컴포넌트 검색시, Controller 클래스가 식별되게 하는 것이 주 목적
- 따라서, 스프링의 컴포넌트 검색에서는 자동으로 HomeController 클래스를 찾은 후, **스프링 애플리케이션 컨텍스트의 bean으로 HomeController의 인스턴스를 생성한다.**

※ @Controller와 동일한 기능을 하는 어노테이션

- @Component, @Service, @Repository
  - 해당 어노테이션들도 동일한 기능을 제공하나, @Controller가 애플리케이션에서 컴포넌트 역할을 더 잘 설명해 준다.

## 3. @GetMapping

- root의 경로 (/)로 HTTP GET 요청이 수신되면 해당 메소드에서 처리가 이루어진다.
- 위 사진에서는 String값을 반환하고 다른 일은 하지 않는다. -> view의 생성
- view는 여러 방법으로 구현될 수 있지만, Thymeleaf가 classpath에 지정되어 있으므로, 여기서는 Thymeleaf를 사용해서 뷰 템플릿을 정의할 수 있다.

※ Spring에서 classpath 지정하기 : *알아봐야한다.*

# 10 ] view 정의 실습하기

- html 파일 생성하기
  - src/main/resoureces/templates 디렉토리 -> new -> other -> web확장 -> html file선택
  - 파일명은 home.html
- 생성하고, 실습을 위해 아래처럼 html 파일의 내용을 수정한다.
  - [![image-20220421152056457]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. 코드설명

```
<!DOCTYPE html>
<html xmlns = "http://www.w3.org/1999/xhtml"
      xmlns:th = "http://www.thymeleaf.org">
    <head>
        <meta charset="EUC-KR">
        <title>Taco Cloud</title>
    </head>
    <body>
        <h1>Welcome to...</h1>
        <img th:src="@{/images/TacoCloud.png}"> /* This is Taco-image */
    </body>
</html>
```

| 태그명                                  | 설명                                                         |
| --------------------------------------- | ------------------------------------------------------------ |
| <img th:src="@{/images/TacoCloud.png}"> | context의 상대적인 경로에 위치하는 이미지를 참조하는 @{...} 표현식을 사용해서 Thymeleaf의 th:src 속성을 지정한다. |

## 2. static 폴더에 위치해야하는 것

- 정적인 이미지는 satic폴더에 위치해야한다.
- 따라서, 타코 클라우드 로고 이미지도 static에 위치해야하며 경로 및 파일명은 아래와 같아야한다.
  - src/main/resourecs/satic/images/TacoCloud.png

# 11 ] Controller Test하기

- src/main/test/HomeControllerTest.java

[![image-20220421161710204]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. 코드설명

- src/main/test/HomeControllerTest.java

```
package tacos;

import static org.hamcrest.Matchers.containsString;
import static
	org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static
	org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static
	org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static
	org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"))
		.andExpect(content().string(
				containsString("Welcome to...")));
	}
}
```

| 코드내용              | 설명                                                         |
| --------------------- | ------------------------------------------------------------ |
| @WebMvcTest           | Spring boot에서 제공하는 특별한 어노테이션 - **MVC 애플리케이션 형태**로 테스트가 실행되도록 한다. - 즉, HomeController가 스프링 MVC에 등록되므로 우리가 스프링 MVC에 웹 요청을 보낼 수 있다. - MVC를 테스트하기 위한 스프링 자원을 설정한다. - 테스트에서는 실제 서버를 시작하는 대신 스프링 MVC의 모의 메커니즘을 사용한다. - 모의 테스트를 하기 위해 우리 테스트 클래스에 **MockMvc 객체를 주입(연결)한다.** |
| testHomePage() 메소드 | 홈페이지에 대해 수행하고자 하는 테스트를 정의한다. - 실행코드를 입력한다. |

※ 테스트 성공화면

[![image](https://user-images.githubusercontent.com/72078208/166888637-21f9cbbe-5438-49c2-a9fa-d081b3750970.png)](https://user-images.githubusercontent.com/72078208/166888637-21f9cbbe-5438-49c2-a9fa-d081b3750970.png)

※ 테스트 실패화면

[![image](https://user-images.githubusercontent.com/72078208/166888778-af620b88-10d1-4240-a33a-8213eaa8a65b.png)](https://user-images.githubusercontent.com/72078208/166888778-af620b88-10d1-4240-a33a-8213eaa8a65b.png)

[![image](https://user-images.githubusercontent.com/72078208/166889308-9789478d-6ec3-4f7c-bda7-47b78aa1f582.png)](https://user-images.githubusercontent.com/72078208/166889308-9789478d-6ec3-4f7c-bda7-47b78aa1f582.png)

- 해당 오류는, 테스트의 코드에 내용에 의거해서 요청시 성공적으로 수행(200) 되어야하는데, 요청한 페이지가 없는 상태의 오류(404)

## 1. testHomePage() 메소드 내용

### [1] tesHomePage() 실행코드 내용

\- 세 가지 기대를 가지며 그 세 가지는 아래와같다.

\- 만약, 세 가지 기대 중 어느 하나라도 충족하지 않으면 테스트는 실패한다.

\- 하지만 여기서는 controller와 view template이 모든 기대를 충족하도록 작성이 되었으므로 테스트를 통과한다.

- 응답은 반드시 HTTP200 (OK) 상태가 되어야한다.

  - HTTP 상태코드 종류

    - [![image-20220421164431080]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

  - HTTP 메소드 종류

    - | HTTP 메소드 | 설명                                                         |
      | ----------- | ------------------------------------------------------------ |
      | GET         | GET 요청 방식은 URI(URL)가 가진 정보를 검색하기 위해 서버측에 요청하는 형태 |
      | POST        | POST 요청 방식은 요청 URI(URL)에 폼 입력을 처리하기 위해 구성한 서버 측 스크립트(ASP, PHP, JSP 등) 혹은 CGI 프로그램으로 구성되고 Form Action과 함께 전송되는데, 이때 헤더 정보에 포함되지 않고 데이터 부분에 요청 정보가 들어가게 된다. |
      | PUT         | POST와 유사한 전송 구조를 가지기 때문에 헤더 이외에 메시지(데이터)가 함께 전송 |
      | DELETE      | 원격지 웹 서버에 파일을 삭제하기 위해 사용되며 PUT과는 반대 개념의 메소드이다. |
      | HEAD        | HEAD 요청 방식은 GET과 유사한 방식이나 웹 서버에 헤더 정보 이외에는 어떤 데이터도 보내지 않는다. - 웹 서버의 다운 여부 점검(Health Check)이나 웹 서버 정보(버전 등)등을 얻기 위해 사용될 수 있다. |
      | OPTIONS     | 해당 메소드를 통해 시스템에서 지원되는 메소드 종류를 확인할 수 있다. |

- 뷰의 이름은 반드시 home이어야 한다.

- 브라우저에 보이는 뷰에는 반드시 "Welcome to..." 텍스트가 포함되어야 한다.

#### 삽질

- import 할 때, static을 붙여주지 않아서 import가 되지 않음
  - import static ::

# 12 ] 의존선 선택의 DevTools

\- pom.xml 파일의 <dependency>요소에 지정되어 있다.

\- 스프링 부트 대시보드에서는 프로젝트에 DevTools가 활성화 되었다는 것을 프로젝트 이름 다음에 보여준다.

- [![image-20220421172849989]()](https://github.com/euncheol-kim/SpringInActionGroupStudy/blob/fd7ea77af3ef8ff12bce489092c73d7fb4de9194/전체리뷰/1장%2C 스프링의 이해.md)

## 1. DevTools란?

- DevTools는 스프링 개발자에게 아래와 같은 개발 시점의 편리한 도구를 제공한다.
  - 코드가 변경될 때 자동으로 애플리케이션을 다시 시작한다.
  - 브라우저로 전송되는 리소스(템플릿, 자바스크립트, 스타일시트)가 변경될 때 자동으로 브라우저를 새로고침한다.
  - 템플릿 캐시를 자동으로 비활성화 한다.
  - 만일 H2 데이터베이스가 사용 중이라면 자동으로 H2 콘솔을 활성화한다.
- 개발 시에만 사용되도록 했으므로 실제 운영에서는 스스로 비활성화가 된다.
  - 비활성화가 되는지는 나중에(19장)에 나올 예정

## 2. DevTools의 유용한 기능

| 유용 기능 종류                                              | 설명                                                         |
| ----------------------------------------------------------- | ------------------------------------------------------------ |
| 자동으로 애플리케이션 다시 시작 시키기                      | 프로젝트의 자바 코드와 속성 파일들을 변경될 때, 곧바로 해당 변경이 적용된다. - DevTools를 사용 중일 때, 애플리케이션은 JVM에 두 개의 클래스 로더에(loder)에 의해 로드된다. 그 중 하나는 우리의 자바 코드, 속성 파일, 프로젝트의 src/main/ 경로에 있는 모든 것과 함께 로드 된다. 이것들은 자주 변경 될 수 있는 것들이며 나머지 클래스 로더는 자주 변경되지 않는 의존성 라이브러리와 함께 로드된다. |
| 자동으로 브라우저를 새로고침하고 템플릿 캐시를 비활성화하기 | 기본적으로 Thymeleaf와 FreeMarker 같은 템플릿에서는 템플릿의 parsing(코드 분석) 결과를 캐시에 저장하고 사용하도록 구동한다. - 템플릿이 사용되는 모든 웹 요청마다 매번 다시 파싱 되지 않게 하기 위해서이다. - 성능상의 이점을 얻는다. 즉, 실제 운영시 이점을 가져온다.  개발 시점에서는 템플릿 캐싱이 그리 유용하지 않다. - 애플리케이션이 실행 중일 때 템플릿을 변경하고 브라우저를 새로고침하더라도 여전히 변경 전의 캐싱된 템플릿이 사용되므로 결과를 볼 수 없기 때문이다. - 위와 같은 경우에서는 다시 시작해야만 변경된 결과를 볼 수 있다.  DevTools는 모든 템플릿 캐싱을 자동으로 비활성하여 이 문제를 해결한다. - 따라서, 템플릿을 얼마든지 변경하더라도 브라우저만 새로고침해주면 변경된 템플릿이 적용된다. |
| LiveReload Server제공                                       | 위의 설명의 연장선인데, LiveReload Server 덕분에 브라우저가 자동으로 새로 고침된다. - 새로고침 되는 경우 :: 템플릿, 이미지, 스타일시트, 자바스크립트 등의 수정이 이루어질때 LiveReload는 다양한 크롬, 사파리, 파이어폭스 브라우저의 플러그인을 갖는다. |
| H2 콘솔                                                     | 만일, 개발용으로 H2 데이터베이스의 사용을 선택하면, 웹 브라우저에서 사용할 수 있는 H2 콘솔도 DevTools가 자동으로 활성화 한다. - 즉, 웹브라우저에서 http://localhost:8080/h2-console에 접속하면 애플리케이션에서 사용하는 데이터를 알 수 있다. |

# 13 ] 정리, Spring의 핵심 기능

- 스프링 MVC 제공
  - HTML이 아닌 출력을 생성하는 REST API를 만들 때도 스프링 MVC를 사용할 수 있다. (6장)
- 템플릿 기반의 JDBC(JdbcTemplate)을 포함해, 기본적인 데이터 퍼시스턴스도 제공한다.
  - persistent :: 끊임없는, 지속적인
  - 메모리에 저장된 객체는 컴퓨터 종료 후 사라진다. (영속성이 보장되지 않는다.)
  - 영속성을 보장하기 위해서는 파일에 저장하거나 DB에 저장해야 하는데 이를 **퍼시스턴트화**라고 한다.
- Reactive programing 지원한다. 지원하는 웹 프레임워크는 SpringMVC개념의 Spring WebFlux이다.
  - Reactive :: 반응이 빠른
  - 10장, 11장, 12장에서 다룰 예정

# 14 ] 정리, Spring boot의 핵심 기능

- 스타터 의존성과 자동-구성

- 액추에이터(Actuator)는 애플리케이션의 내부 작동을 런타임 시에 살펴볼 수 있는 기능 제공하며, 여기에는 메트릭(metric), 스레드 덤프 정보, 애플리케이션의 상태, 애플리케이션에서 사용할 수 있는 환경 속성이 포함된다.

  - actuator :: 조작기

  - | 용어            | 설명                                                         |
    | --------------- | ------------------------------------------------------------ |
    | 메트릭(metrics) | 타임스탬프와 보통 한두 가지 숫자 값을 포함하는 이벤트이다. - log와는 달리 metrics은 주기적으로 보낸다. - log는 보통 무언가가 발생했을 때 로그 파일에 추가한다. - metrics은 종종 리소스 사용 모니터링, 데이터베이스 실행 메트릭 모니터링 등 상태 모니터링 맥락에서 사용한다. |
    | 스레드 덤프     | 프로세스에 속한 모든 모든 쓰레드들의 상태를 기록(snapshot)한 것이다. - 참고링크 쓰레드 덤프(Thread Dump), https://siyoon210.tistory.com/127, siyoon210 Thread Dump분석하기, https://brunch.co.kr/@springboot/126, 에디의 기술블로그 |

- 환경 속성의 명세

- 핵심 프레임워크에 추가되는 테스트 지원

## 1. Spring data

\- 기본적인 데이터 퍼시스턴스 지원은 핵심 스프링 프레임워크에 포함되어있지만,

\- 스프링 데이터는 이외에도 꽤 놀랄 만한 기능을 제공한다.

- 즉, 간단한 자바 인터페이스로 우리 애플리케이션의 데이터 레포지토리(repository)를 정의할 수 있다.
  - repository :: 데이터 집합체가 보관되고 조직적인 방식으로 유지되는 대체로 컴퓨터 저장장치 내의 주요 장소이다.
- 스프링의 데이터는 다른 데이터베이스와 결합하여 사용할 수 있다.
  - Mongo 또한 가능하며, 그래프형 데이터베이스인 Neo4j 등

## 2. Spring security

- 강력한 보안 프레임워크
  - 인증(authentication), 허가(authorization), API보안을 포함하여 폭넓은 범위의 애플리케이션 보안 요구를 다룬다. 매우 광범위하다 (4장,12장)

## 3. Spring 통합과 배치

\- 애플리케이션은 다른 애플리케이션 또는 같은 애플리케이션의 서로 다른 컴포넌트 통합(Intergration)할 필요가 생긴다. 이러한 요구사항을 해결하기 위해 알려진 패턴들이 존재하는데, 스프링 통합과 배치(Batch)는 스프링 기반 애플리케이션의 이런 패턴 구현을 제공한다.

- 스프링 통합(Intergration) : 데이터가 사용 가능한 즉시 처리되는 실시간 통합을 한다. (9장에서 다룸)
- 스프링 배치(Batch) : 다량의 데이터가 처리되는 시점을 트리거(기준 시간에)가 알려줄 때 데이터가 수집 처리되는 배치 통합을 처리해준다. (9장에서 다룸)

## 4. Spring 클라우드

※ 마이크로서비스 : 거대한 단일체 애플리케이션을 개발하는 것이 아닌, 여러 개의 개별적 단위의 개발을 하고 이를 합성하는 것

- 스프링을 사용해, 클라우드 애플리케이션을 개발하기 위한 프로젝트들의 모음인 스프링 클라우드를 사용한다.
  - 양이 매우 방대하며, 13, 14, 15장에서 일부만 알아볼 예정
  - 참고 도서 :: 스프링 마이크로서비스 코딩 공작소, 존 카넬, 정성권 번역