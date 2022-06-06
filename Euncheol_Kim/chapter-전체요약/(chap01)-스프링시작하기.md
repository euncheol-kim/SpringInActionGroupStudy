### 목차

> 1. 스프링이란?
>    1. 스프링 애플리케이션 컨텍스트<sup>Spring Application context</sup> [스프링에서 제공하는 container]
>       1. <font color="brown"> **컨테이너<sup>container</sup>**</font> [교재외]
>    2. Bean의 상호연결
>       1. IoC 컨테이너 [교재외]
>       2. IoC가 관리하는 Bean으로 등록하는 방법 [교재외]
>       3. 질문, < 인터페이스를 객체로 구현한다 ??? >
>       4. IoC컨테이너의 종류 [교재외]
>    3. 자바 기반의 구성<sub> configuration </sub>을 이용한 빈의 연결
>       1. 자바 기반의 구성의 장점
>    4. Bean의 자동-구성
>       1.  자동연결과 컴포넌트 검색
>       2. 자동구성<sub>autoconfiguration</sub>의 장점
>    5. 실습 초기 프로젝트 설정
>       1. 스프링 이니셜라이저<sub>Spring Initializr</sub>를 이용한 설정하기
>       2. 프로젝트 종속성 주입하기
>    6. 스프링 부트 내부 파일 들여다보기
>       1. 애플리케이션의 부트스트랩(구동) 클래스
>       2. 애플리케이션 테스트
>    7. 스프링 애플리케이션 작성하기
>       1. 스프링은 MVC라고 하는 강력한 웹 프레임워크를 가진다.
>       2. HomeController.java
>    8. 컨트롤러 테스트하기
>       1. 위에서 작성한 애플리케이션을 토대로 테스트할 항목 [아래 2번 소스 코드로 확인]
>       2. 홈페이지 컨트롤러 테스트 코드 작성 (HomeControllerTest.java) 
>    9. 종속성을 넣어준 Spring DevTools 알아보기
>       1. Spring DevTools의 특징
>       2. 자동으로 애플리케이션 다시 시작하기
>       3. 자동으로 브라우저를 새로고침하고 템플릿 캐시를 비활성화하기





# 1. 스프링이란?

> 스프링의 동작을 이해하기 위해 몇 가지의 개념 정리



애플리케이션은 애플리케이션 전체 기능 중, 일부를 담당하는 많은 컴포넌트로 구성되어있다.

**<u>각 컴포넌트는</u>** <u>다른 애플리케이션 구성 요소와 협력해서 작업을 처리</u>한다

그리고 **애플리케이션이 실행될 때**는 <u>각 컴포넌트가 어떻게든 생성되어야 하고, 상호 간에 알 수 있어야 한다.</u>



## [1] 스프링 애플리케이션 컨텍스트<sup>Spring Application context</sup> [스프링에서 제공하는 container]

> 1. 스프링은 스프링 애플리케이션 컨텍스트(Spring Application Context)라는 <sup>**1**</sup>컨테이너(container)를 제공한다.
> 2. 스프링 애플리케이션 컨텍스트는 **<u>컴포넌트를 생성하고 관리</u>**한다.
>
> 3. 그리고 애플리케이션 <u>컴포넌트 또는 빈(Bean)들은</u> 스**프링 애플리케이션 컨텍스트 내부에서 서로 연결되어 완전한 애플리케이션을 만든다.**



### <sup>1</sup><font color="brown"> **컨테이너<sup>container</sup>**</font> [교재외]

기존의 IT 또는 클라우드 등 **어디서나 실행될 수 있도록** <u>애플리케이션 코드가</u> <u>해당 **라이브러리 및 종속 항목과 함께 <font color="blue">패키징</font>**되어 있는 소프트웨어 실행 유닛</u>이다.



## [2] Bean의 상호연결

> 1. Bean의 상호 연결은 **의존성 주입 <sup>Dependency InJection, DI</sup> 라는 패턴을 기반**으로 수행된다.
> 2. 즉, <u>애플리케이션 컴포넌트에서 의존(사용)하는 다른 빈의 생성과 관리를 자체적으로 하는 대신에</u> 별도의 **개체(컨테이너가) 해주며**, <u>이 개체에서는</u> **모든 컴포넌트를 생성/관리하고 해당 컴포넌트를 필요로 하는 빈에 주입(연결)한다.** 일반적으로 이것은 생성자 인자 또는 속성의 접근자 메소드를 통해 처리된다.



![스프링 컨텍스트가 재고서비스 제품서비스 컴포넌트를 관리하는 예시](https://user-images.githubusercontent.com/72078208/171992090-0a2a6a1d-ae1e-4741-8102-21df33b327a4.jpg)

> 이미지 : Spring In Action, 1장, 그림1.1, p5

위의 그림은 `재고 서비스 컴포넌트`와 `제품 서비스 컴포넌트`의 관계를 보여주는데,

`제품 서비스 컴포넌트`는 `재고 서비스 컴포넌트`를 필히 필요로하기에 **<제품 서비스 컴포넌트는 재고 서비스에 의존한다.>** `제품 서비스 컴포넌트`에서 주입되는 해당 빈을 관리하는 것이 아니라 <sup>**1**</sup>**`IoC컨테이너`**에서 생성하고 관리한다.



### 1. <sup>1</sup>  <font color="brown">IoC 컨테이너</font> [교재외]

> 참고자료 : <a href="https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=pjok1122&logNo=221744895053" target="_blank"> [Spring] IoC컨테이너와 Bean</a>

| 용어          | 동의어      | 설명                                                         |
| ------------- | ----------- | ------------------------------------------------------------ |
| IoC Container | BeanFactory | **IoC Container는**<br /> <u>오브젝트의 생성과 관계설정, 사용, 제거 등의 작업을 대신해준다</u>하여 붙인이름이다. 이때 **IoC Container에 의해 관리되는 오브젝트들을 Bean**이라 한다.<br /><br /><u>IoC Containter는 Bean을 저장한다고 하여</u>, **BeanFactory라고도 한다**.<br /><br />**BeanFactory는 인터페이스**이며, <u>Application Context는 BeanFactory의 구현체를 상속받고 있는 인터페이스다.</u><br /><br />실제로 스프링에서 IoC Container라고 불리는 것은 Application Context의 구현체이다. |

 

### 2. IoC가 관리하는 Bean으로 등록하는 방법 [교재외]

![image](https://user-images.githubusercontent.com/72078208/171993545-ac550ea9-56f5-47ca-8fe6-74febe38b63c.png)

> IoC 컨테이너가 관리하는 Bean으로 등록하기 위해서는 **적절한 메타정보를 만들어 제공**해야 한다.
> `스프링의 설정메타 정보는 ` **`BeanDefinition`** `이라는 인터페이스이다.` 
>
> <sup>**2** </sup>Xml, 애노테이션, 자바 코드 프로퍼티 파일 전부 상관없이 **`BeanDefinitionReader`**라는 인터페이스에 의해 **`BeanDefinition`**객체로 변환된다.



### 3. <sup>2</sup> [질문]

> 참고자료 : <a href ="https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=pjok1122&logNo=221744895053" target="_blank">IoC컨테이너와 Bean</a>
>
> **질문과 관련한 API doc**
>
> - Spring Framework 5.3.20 API, <a href = "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/config/BeanDefinition.html">Interface BeanDefinition</a>
> - Spring Framework 5.3.20 API , <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/support/BeanDefinitionReader.html" target="_blank">Interface BeanDefinitionReader</a>



BeanDefinitionReader는 인터페이스이고, BeanDefinition도 인터페이스인데

`BeanDefinitionReader 인터페이스에 의해 BeanDefinotopn객체로 만들어진다`는 말이 이해가 되지 않습니다.

<인터페이스를 객체로 구현한다>라는게 말이 되는건가요? 인터페이스는 객체로 못만드는걸로 알고있어서요.



### 4. IoC컨테이너의 종류[교재외]

| IoC컨테이너의 종류           | 설명                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| StaticApplicationContext     | 코드를 통해 빈 메타정보를 등록하기 위해 사용한다.<br />스프링 기능에 대한 학습 테스트를 만들 때를 제외하면 실제로 사용되지 않는다. |
| GenericApplicationContext    | 실전에서 사용될 수 있는 모든 기능을 갖추고 있는 애플리케이션 컨텍스트이다.<br />StaticApplicationContext와 달리 XML파일과 같은 외부의 리소스에 있는 빈 설정 메타정보를 리더를 통해 읽어들여서 메타정보로 전환하여 사용한다. |
| GenericXmlApplicationContext | `GenericApplicationContext`에서 `XmlBeanDefinitionReader`를 내장하고 있어, Xml을 읽어 사용할 때 편리하다. |
| WebApplicationContext        | 스프링 애플리케이션에서 가장 많이 사용된다. Xml 설정 파일을 사용하는 경우에는 `XmlWebApplicationContext`를 사용하며, 애노테이션을 사용한다면 `AnnotationConfigWebApplicationContext`를 사용한다. |



## [3] 자바 기반의 구성<sub> configuration </sub>을 이용한 빈의 연결

```java
@Configuration
public class ServiceConfigiration {
    @Bean
    public InventoryService inventoryService() {
        return new InventoryService;
    }
    
    @Bean
    public ProductService productService() {
        return new ProductService(inventoryService());
    }
}
```

@Configuration

> 1. 스프링에게 **`구성클래스`**임을 알려준다.
> 2. **`구성클래스`** 내부의 각 빈을 **<u>스프링 애플리케이션 컨텍스트에 제공</u>**한다.

@Bean

> 1. 각 메소드에서 반환되는 객체가 애플리케이션 컨텍스트의 빈으로 추가되어야 한다는 것을 나타낸다.



### 1. 자바 기반의 구성의 장점

> 1. 강화된 타입 안전
>
> 2. 향상된 리팩토링 기능
>
> 3. 자동-구성 기능으로 별도의 XML 구성이나 자바 구성이 필요없다.
>
>    > 만약, 자동-구성이 제공되지 않을 때만 XML에 추가해주면 된다.



## [4] Bean의 자동-구성

> `자동-구성`은 <sup>1</sup>자동 연결<sub>**autowiring**</sub>과 <sup>1</sup>컴포넌트 검색<sub>**component scanning**</sub>의 스프링 기법을 기반으로 한다.
>
> 최근에 Spring boot가 소개되며 자동-구성 기능이 더욱 향상되었다.
> 스프링 부트는 생산성 향상을 제공하는 스프링 프레임워크의 확장이며
> 향상된 **`자동-구성(autoconfiguration) 기능`**에 비해 <u>환경 변수인 classpath를 기준으로</u> <u>어떤 컴포넌트가 **구성**되고 **연결**되어야 하는지 알 수 있다.</u> 
>
> 
>
> > **구성 - 빈으로 생성**
> >
> > **연결 - 컴포넌트의 의존성 주입**



### 1. <sup>1</sup> 자동연결과 컴포넌트 검색

| 개념          | 설명                                                         |
| ------------- | ------------------------------------------------------------ |
| 컴포넌트 검색 | 애플리케이션의 <u>classpath에 지정된 컴포넌트를 찾은 후,</u><br />스프링 <u>애플리케이션 컨텍스트의 빈으로 생성한다.</u> |
| 자동연결      | 의존 관계가 있는 컴포넌트를 자동으로 다른 빈에 주입(연결)한다. |



### 2. 자동구성<sub>autoconfiguration</sub>의 장점

1. xml이나 java의 코드량이 급격히 줄어든다.
2. 개발의 생산성을 향상시킨다.



## [5] 실습 초기 프로젝트 설정

### 1. 스프링 이니셜라이저<sub>Spring Initializr</sub>를 이용한 설정하기

![image](https://user-images.githubusercontent.com/72078208/171996910-432102e2-f371-4b97-b58c-123923d5a290.png)

 **`JAR`** vs **`WAR`** 

> <a href="https://kimeuncheol.tistory.com/92" target="_blank">spring 이론 :) jar(java Archive)와 War(Web Application Archive)</a>
>
> <a href="https://kimeuncheol.tistory.com/110" target="_blank">Spring에서 사용하는 Template의 종류와 WAR, WAR에 따른 Template채택</a>>
>
> 요약 : JAR의 선택은 클라우드를 염두한 선택 
> 만약, <u>기존의 자바 애플리케이션 서버에</u> <u>우리 애플리케이션을 배포하고자 한다면 WAR패키징을 선택하고 웹 초기 설정 클래스를 포함시켜야한다.</u>



### 2. 프로젝트 종속성 주입하기

![image](https://user-images.githubusercontent.com/72078208/171997294-764b1c16-eaec-409d-ad66-2ef7f39bc1c1.png)

 Spring Boot DevTools

> 참고 :  <a href="https://velog.io/@bread_dd/Spring-Boot-Devtools" target="_blank">Spring Boot Devtools 알아보기</a>, bread_dd, velog



 Spring Web

> 참고 : <a href="https://www.javatpoint.com/spring-boot-starter-web" target="_blank">Spring Boot Starter Web</a>,  javaTpoint
>
> > ![image](https://user-images.githubusercontent.com/72078208/171997690-f3b02b2e-4158-43f1-aada-6cbf1a1d3a8f.png)
> >
> > ![image](https://user-images.githubusercontent.com/72078208/171997675-907e7725-a7d5-463f-bd30-0761ea6df877.png)
> >
> > 공부해보면 좋을 것 : 디스패처 서블릿





## [6] 스프링 부트 내부 파일 들여다보기

### 1. 애플리케이션의 부트스트랩(구동) 클래스

<img src="https://user-images.githubusercontent.com/72078208/171997995-3d32b43d-56c4-4bc2-9005-187a9c2e65df.png" alt="image"  />



#### **@SpringBootApplication**

> @SpringBootApplication은 3개의 어노테이션의 결합 형태
>
> 스프링 부트 애플리케이션임을 나타낸다.

| @SpringBootApplication 각각의 결합된 어노테이션 | 설명                                                         |
| ----------------------------------------------- | ------------------------------------------------------------ |
| @SpringBootConfiguration                        | **구성클래스로 지정**한다.<br />- @Configuration 어노테이션의 특화된 형태 |
| @EnableAutoConfiguration                        | 스프링 부트 **자동-구성을 활성화**한다.                      |
| @ComponentScan                                  | **컴포넌트 검색을 활성**화한다.<br />- 이것은 @Component, @Controller, @Service 등의 어노테이션과 함께 클래스를 선언할 수 있게 해준다. 그러면 스프링은 자동으로 그런 클래스를 찾아 스프링 애플리케이션 컨텍스트에 **컴포넌트로 등록**한다. |

####  **main()메소드**

> 1. 애플리케이션의 실행
> 2. 애플리케이션 컨텍스트를 실행하는 SpringApplication클래스의 <sup>1</sup>run()메소드를 호출한다.



#### <sup>1</sup> run() 메소드

> 매개변수로 `구성클래스`와 `명령행`<sub>command-line</sub>을 받는다.
>
> `구성클래스`가 부트스트랩 클래스와 반드시 같아야 하는 것은 아니지만 대개 동일하다.









### 2. 애플리케이션 테스트

![image](https://user-images.githubusercontent.com/72078208/171998475-71afc974-2a36-46dc-8cec-6c06d94c3a33.png)

####  contextLoads() 메소드

> <u>실행시킨다면</u> 내부에 <u>실행 코드가 없더라도 스프링 애플리케이션 컨텍스트가 성공적으로 로드될 수 있는지 **기본적인 검사를 수행**한다.</u> 따라서 만일 스프링 애플리케이션 컨텍스트의 생성을 저해하는 코드가 존재한다면 이 테스트는 실패한다.





## [7] 스프링 애플리케이션 작성하기

><이번 주제에서 할 일>
>
>1. 홈페이지의 웹 요청<sub>request</sub>을 처리하는 컨트롤러<sub>controller</sub> 클래스
>2. 홈페이지의 모습을 정의하는 뷰 템플릿



### 1. 스프링은 MVC라고 하는 강력한 웹 프레임워크를 가진다.

> 참고
>
> 1. <a href="https://catsbi.oopy.io/f52511f3-1455-4a01-b8b7-f10875895d5b" target="_blank">스프링 MVC - 구조이해</a>, Catsbis DLog [자세하게 기술되어 있음]
> 2. <a href="https://yoonbing9.tistory.com/80" target="_blank"> Spring MVC - Front Controller의 핸들러매핑과 핸들러어댑터</a>, Bing9 Driven Development [핸들러란? 핸들러 매핑이란?]
> 3. <a href="https://kimeuncheol.tistory.com/19" target="_blank"> Spring 이론:) MVC패턴이란?</a>, 내 블로그 [간단히 MVC패턴에 대해서 표로 정리]



### 2. HomeController.java

![image](https://user-images.githubusercontent.com/72078208/171999397-3e788b1c-c142-4bea-addc-54d000fac425.png)

####  @Controller

> 컴포넌트 검색 시에 HomeController 클래스가 <u>**`컴포넌트`**로 식별되게 하는 것</u>이 주 목적이다.
> 따라서 스프링의 컴포넌트 검색시에, 자동으로 HomeController 클래스를 찾은 후,
> <u>스프링 애플리케이션 컨텍스트의 빈으로 HomeController의 인스턴스를 생성한다.</u>
>
> @Component, @Service, @Repository를 포함해서 소수의 다른 어노테이션도 @Controller와 같은 기능을 수행하나, @Controller를 선택한 이유는 컴포넌트 역할을 더 잘 설명해 주기 때문이다.

####  @GetMapping

>루트 경로의 HTTP GET 요청이 수신되면 home()메소드가 해당 요청을 처리한다.
>
>리턴값은 뷰의 논리적인 이름이다. (해당 템플릿으로 요청을 한다.)
>
>- 뷰는 여러 방법으로 구성이 될 수 있다. 
>- 실습에서는 Thymeleaf가 우리의 classpath에 지정되어 있으므로 Thymeleaf를 뷰 템플릿으로 정의할 것이다.
>- <a href="https://laegel.tistory.com/m/6?category=856922" target="_blank"> 스프링 classpath가 가리키는 곳</a>, 후루룹쨥쨥



### 3. Home.html (Thymeleaf)

![image](https://user-images.githubusercontent.com/72078208/172011247-50ec8096-9937-4fe3-9350-e7ce72e94347.png)

#### th:src = "@{...}"

> 상대적 경로에 이미지를 참조하기 위해 사용함



## [8] 컨트롤러 테스트하기

> 기본적으로 HTML 페이지의 콘테츠에 대한 어서션<sub>assertion</sub>(테스트를 하기 위해 지정하는 단언)을 설정하기 어렵다. 하지만 스프링은 웹 애플리케이션을 쉽게 테스트하는 강력한 테스트 지원 기능을 제공한다.



### 1. 위에서 작성한 애플리케이션을 토대로 테스트할 항목 [아래 2번 소스 코드로 확인]

1. 응답은 반드시 HTTP 200 (OK) 상태가 되어야한다.
2. 뷰의 이름은 반드시 home이어야 한다.
3. 브라우저에 보이는 뷰에는 반드시 'Welcome to...' 텍스트가 포함되어야 한다.



### 2. 홈페이지 컨트롤러 테스트 코드 작성 (HomeControllerTest.java) 

![image](https://user-images.githubusercontent.com/72078208/172020938-73ff84ab-97fc-49c8-bb32-650ee2349f5e.png)

![image](https://user-images.githubusercontent.com/72078208/172021099-99ae701f-2462-4e7e-8ef6-0f61451740e6.png)

> **나의 삽질 기록**
>
> 코드를 작성하며 생겼던 이슈 : [0605~0606사이 업로드 예정]
>
> 
>
> **나의 포스팅**
>
> Junit5 : [복습좀 하고 업로드 예정]
>
> 
>
> **참고하면 좋을 자료**
>
> <a href="https://donghyeon.dev/junit/2021/04/11/JUnit5-%EC%99%84%EB%B2%BD-%EA%B0%80%EC%9D%B4%EB%93%9C/" target="_blank"> JUnit5 완벽 가이드</a>, 민동현 Dream Cometrue [자세하게 기술, 양이 매우 방대함]
>
> <a href="https://loosie.tistory.com/120" target="_blank"> [Java/4주차 과제 #0] JUnit5 이란?</a>, 루지, tistory [Junit4와의 비교 및 잘 읽히는 참고자료]
>
> <a href="https://steady-coding.tistory.com/349" href="_blank"> Junit5 필수 개념 정리(JAVA)</a>, 느리더라도 꾸준하게 [Junit 핵심 개념만 잘 정리]
>
> <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/junit/jupiter/SpringExtension.html"> Annotation Interface ExtendWith</a>, Junit - Spring freamwork 5.3.20 API
>
> <a href="https://ozofweird.tistory.com/entry/Spring-Boot-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%96%B4%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98" target="_blank">[Spring Boot] 테스트 어노테이션 </a>, 나의 개발 기록 [통합테스트, 단위테스트 등 표로 설명]
>
> 

#### @DisplayName

> 테스트 클래스나 테스트 메소드에 이름을 붙여줄 때 사용한다.
>
> - 실행결과를 보면 DisplayName의 매개변수로 준 이름이 나온다. (한글도 가능)

#### @ExtendWith

> 확장을 선언적으로 등록할 때 사용한다.

#### @WebMvcTest

> Spring MVC 애플리케이션의 형태로 테스트가 실행되도록 한다.
> 즉, 해당 코드에서는 HomeConttoller가 스프링 MVC에 등록되므로 우리가 스프링 MVC에 웹 요청을 보낼 수 있다.
> 단위테스트시에 사용하며, @SpringBootTest 어노테이션보다 가볍다.
>
> - 주의할 것은 @WebMvcTest와 @SpringBootTest는 같이 사용할 수 없다. [사용시 에러]

#### @Autowired

> Spring context bean이 관리하는 빈(Bean)을 주입 받는다.

#### @Test

> 테스트 메소드임을 지정한다.

#### MockMvc

> 실제 서버로 테스트하지 않고, 스프링 MVC의 모의<sub>mocking</sub>메커니즘을 사용해도 충분하므로 모의 테스트를 하기 위해
> 테스트 클래스에서 MockMvc 객체를 주입한다.



## [9] 종속성을 넣어준 Spring DevTools 알아보기

> 참고하면 좋을 자료 :  <a href="https://velog.io/@bread_dd/Spring-Boot-Devtools" target="_blank">Spring Boot Devtools 알아보기</a>, bread_dd, velog



#### 1. Spring DevTools의 특징

1. 코드가 변경될 때 자동으로 애플리케이션을 다시 시작시킨다.
2. 브라우저로 전송되는 리소스(예로 템플릿, 자바스크립트, 스타일시트) 변경시 자동으로 브라우저를 새로고침
3. 템플릿 캐시를 자동으로 비활성화 한다.
4. 만일 H2 데이터베이스가 사용 중이라면 자동으로 H2 콘솔을 활성화한다.



#### 자동으로 애플리케이션 다시 시작하기

>  프로젝트의 자바 코드와 속성 파일들을 변경할 때  애플리케이션을 다시 시작시킨다. 
>
> 다시 시작할 때는 **자주 변경되는 클래스 로더만 다시 시작**시킨다. 이로, 애플리케이션 다시 시작시 시간 단축
> 
>
>
> 정확히는 JVM에서 두 개의 클래스가 로더되는데 두 개의 클래스는 아래와 같다.
>
> - 자주 변경되는 파일 : 자바 코드, 속성 파일, 프로젝트의 src/main/경로에 있는 모든 것
> - 자주 변경되지 않는 파일 : 자주 변경되지 않는 의존성 라이브러리와 함께 로드
>   
>
> **단, 애플리케이션이 자동으로 다시 시작될 때 의존성 변경이 적용될 수 없다.**
> 의존성 라이브러리를 포함하는 클래스 로더는 자동으로 다시 로드되지 않기 때문이다.
> 만약, 의존성을 추가, 변경, 삭제할 때는 애플리케이션을 새로 시작해야지 변경의 효과를 얻는다.



#### 자동으로 브라우저를 새로고침하고 템플릿 캐시를 비활성화하기

> 기본적으로 템플릿의 파싱(코드 분석) 결과를 캐시에 저장하고 사용하도록 구성된다.
> 템플릿이 사용되는 **모든 웹 요청마다 매번 다시 파싱 되지 않게 하기 위해서이다.**
>
> 실제 운영시에 매우 큰 이점을 얻는다.
>
>
> 하지만, 개발 시점에 캐시에 저장된다면 변경 사항이 있더라도 캐싱된 템플릿이 보이게 될 것이다.
> 이 경우는 다시 애플리케이션을 시작해야 변경 사항을 볼 수 있다.
>
>
> DevTools는 모든 템플릿 캐싱을 자동으로 비활성화하여 이를 해결하고, 템플릿이 변경된다면 새로고침만 하면 된다.





기록은 하지 않겠지만... 뒤에 나오는 스프링 살펴보기 내용

1. 스프링 데이터
2. 스프링 시큐리티
3. 스프링 통합과 배치
4. 스프링 클라우드