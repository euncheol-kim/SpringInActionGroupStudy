title : Chap7_freePresentation



## Index

> ### 1. Rest API
>
> ### 2. 좋은 REST API를 디자인하는 방법
>
> ### 3. Spring의 하이퍼미디어의 적용 HATEOAS 개념 (성숙도 레벨 3단계)
>
> - Spring의 하이퍼미디어, HATEOAS
>
> <br>
>
> ### 4. Spring 프로젝트 구조 <sup>Spring Project Structure</sup>
>
> <br>
>
> ### 5. @SpringBootApplication
>
> - @Component Scan
>
> <br>
>
> ### 6. Spring Web MVC
>
> ### 7. Spring 기본 3가지 계층 구조<sup> Three Tier in Spring Web MVC</sup>
>
> ### 8. VO와 DTO
>
> ### 9.  DTO <sup>Data Transfer Object</sup>와 Entity class의 분리 이유
>
> - DTO의 개념
> - Entiry의 개념
>
> <br>
>
> ### 11. Spring annotation 
>
> > #### [Index에서 참고링크만 첨부]
>
> - Spring Validation annotation 총정리, https://hyeran-story.tistory.com/81
> - Spring Annotation 종류별 소개 그리고 annotation별 역할, https://gmlwjd9405.github.io/2018/12/02/spring-annotation-types.html

<br>

<br>

# *1. REST & REST API <SUP>Representational State Transfer</sup>

# [1] REST

> ### [REST 개념]
>
> > -  소프트웨어 개발 아키텍쳐의 한 형식
> >
> > - 자원을 이름 <u><sup>자원의 표현</sup>으로 구분</u>하여 해당 <u>자원의 상태(정보)를 주고 받는 모든 것</u>을 의미한다.
> >
> > - REST는 자원기반의 구조 <SUP>ROA(Resource Oriented Architecture)</sup> 설계의 중심에 Resource가 있고, HTTP Method를 통해 Resource를 처리하도록 설계된 아키텍쳐를 의미한다.
> >
> > <br>
> >
> > #### [REST의 자원, 자원의 표현, 자원의 상태 전달]
> >
> > > - **자원** : 소프트웨어가 관리하는 모든 것 (문서, 그림, 데이터, 해당 소프트웨어)
> > > - **자원의 표현** : 그 자원을 표현하기 위한 이름 (DB학생 정보 자원 -> student로 표현)
> > > - **자원의 상태 전달** : 데이터 요청이 되어지는 시점에서 자원의 상태(정보)를 전달하는 것
> > >   - 자원은 일반적으로 JSON으로 전달 된다.
>
> <br>
>
> ### [필요성]
>
> > - 애플리케이션 분리 및 통합을 위해
> > - 다양한 클라이언트의 등장
> > - HTTP프로토콜을 그대로 활용하기 때문에 웹의 장점을 최대한 활용할 수 있다.
>
> <br>
> 
>
> ### [REST 구성요소]
>
> > - **자원(Resource)** : 클라이언트는 URI를 이용해서 자원을 Server에 요청하고 응답 받는다.
> > - **행위(Verb)** : HTTP 프로토콜의 Method를 사용한다. GET, POST, PUT, DELETE와 같은 메소드 제공
> > - **표현(Representational of Resource)** : Server가 클라이언트에게 보내는 응답으로 JSON등으로 응답한다.
>
> <br>
>
> ### [REST 특징]
>
> > - **Server-Client 구조**
> > - **Stateless**
> >   - HTTP 프로토콜이 Stateless한 상태를 유지하므로, REST또한 Stateless한 상태를 유지한다.
> >   - Client의 context<sup> IT의 context :응답간 상태의 흐름</sup>를 Server에 저장하지 않는다.
> >   - 즉, context의 정보를 담는 쿠키, 세션의 정보를 신경쓰지 않아도 되기 때문에 구현이 단순해진다.
> >   - **Server는 각각의 요청을 완전한 별개의 요청으로 인식하고 처리한다.**
> > - **Cacheable (캐시 처리 가능)**
> >   - HTTP의 가장 강력한 기능중 하나인 캐시 처리 기능을 적용할 수 있다.
> >   - 대량의 요청을 효율적으로 처리하기 위해 캐시가 요구된다.
> >   - 캐시 덕분에 응답시간이 빨라진다. (자원 이용률을 향상)
> > - **Layered System (계층화)**
> > - **Code-On-Demand (optional)**
> >   - Server로부터 스크립트를 받아 Client에서 실행한다.
> > - **Uniform Interface (인터페이스 일관성)**

<br>

# [2] REST API

> ### [개념]
>
> > REST 기반의 API 구성
>
> <br>
>
> ### [REST API 특징]
>
> > - REST 기반으로 시스템을 분산해 확장성과 재사용성을 높여 유지보수 및 운용을 용이하게 한다.
> > - REST는 HTTP프로토콜을 기반으로 구현되기 때문에 HTTP를 지원하는 프로그래밍 언어로 클라이언트, 서버를 구현할 수 있다.

<br>

<br>

# *2. 좋은 REST API를 디자인하는 방법

> ### [리차드슨의 REST 성숙도 모델의 구조화]
>
> ![image](https://user-images.githubusercontent.com/72078208/175757378-86745635-ad2b-4081-8e22-acdd1ee3e428.png)
>
> ### [REST 성숙도 모델 : 0단계]
>
> ![image](https://user-images.githubusercontent.com/72078208/175757417-0c10da59-bbb4-4bdd-8e03-d7a946cd9310.png)
>
> > 단순히 HTTP 프토토콜 규칙에 의거해 작성된 모델
>
> <br>
>
> ### [REST 성숙도 모델 : 1단계]
>
> ![image](https://user-images.githubusercontent.com/72078208/175757471-990a08e5-26c5-4804-ab6c-32d933aa40cd.png)
>
> ![image](https://user-images.githubusercontent.com/72078208/175757675-91774322-a98d-400a-a9e0-0f8d582b6810.png)
>
> > 개별 리소스와의 통신을 준수하게 바꾼 형태
> >
> > - <u>개별 리소스에 맞는 Endpoint를 사용하는 것</u>과 <u>요청하고 받은 자원에 대한 정보를 응답으로 전달해야한다는 의미</u>  
>
> <br>
>
> ### [REST 성숙도 모델 - 2단계]
>
> ![image](https://user-images.githubusercontent.com/72078208/175757726-bbd568ef-1519-4e66-bab3-d632c323571e.png)
>
> > CRUD에 따른 적합한 HTTP Method 지정 및 응답에 대한 명확성 제공
> >
> > - GET : 특정 조회에 사용된다 <u>**\<GET HTTP Method는 body를 가지지 않음을 기억한다.>**</u>
> > - POST : 특정한 것을 생성하기 위해 사용한다.
> >
> > <br>
> >
> > #### [`예약 가능한 시간 확인` 요청 / 응답에 대한 설명]
> >
> > 예약 가능한 시간을 확인하는 것은, READ하는 것이다. 이는 GET 요청으로 충분하다.
> > GET요청을 하기 위해서는 query string(parameter)를 사용하여 필요한 리소를 전달한다.
> >
> > 이후, 서버측에서는 성공적으로 작업이 수행했다는 200 응답과, 해당 날짜에 예약이 가능한 상태를 보여준다.
> >
> > <br>
> >
> >
> > #### [`특정 시간에 예약` 요청 / 응답에 대한 설명]
> >
> > 특정 시간대를 예약하는 것이기 때문에 클라이언트가 POST로 요청을 한다.
> >
> > Server측에서는 클라이언트가 요청한 내용을 성공한 것을 알리기 위해 201로 알린 후,
> >
> > 클라이언트 측에서 예약된 정보를 특정 URI로 확인할 수 있도록 헤더의 URI와 body의 가공된 데이터 내용으로 응답을 한다.
>
> <br>
>
> ### [REST 성숙도 모델 - 3단계]
>
> ![image](https://user-images.githubusercontent.com/72078208/175758129-0ccae8b1-770f-4743-90af-72e685dd047f.png)
>
> > 하이퍼미디어 컨트롤을 적용한다.  *** Spring의 HAETOAS(헤이티오스)의 적용***
> >
> > - 응답에는 리소스의 URI를 포함한 링크 요소를 삽입하여 작성한다.
> > - 링크의 응답을 줌으로써 클라이언트가 다른 서비스를 기능을 이용할 수 있도록 돕는다.
> > - 클라이언트 입장에서는 보다 리소스와 기능 접근이 쉬워진다.



# *3. Spring의 하이퍼미디어의 적용 HATEOAS 개념 (성숙도 레벨 3단계)

## [1] Spring의 하이퍼미디어, HATEOAS

> 참고자료 
>
> - <a href = "https://bepoz-study-diary.tistory.com/197" target="_blank">[Spring] HATEOAS에 대해</a>, 파즈의 공부일기

> ### [무조건 사용하는 것이 좋은가?]
>
> > REST API의 사용 주체가 빠른 속도의 의사결정과 개발을 중시하는 웹 서비스라면 개발 속도가 더딜 수 있기 때문에 기술적 사용 여부는 고려해봐야하며, HATEOAS의 이해가 부족한 조직이라면 더더욱 기술적 도입을 더더욱 고려해봐야한다.
>
> <br>
>
> ### [HAETOAS의 필요성]
>
> > - 응답에서 지정되고 사용되기 때문에 URI 구조에 대한 긴밀한 종속성은 없다. (느슨한 결합)
> > - 클라이언트가 모든 리소스를 URL 하드 코딩해야하는 경우 서비스 구현과 밀접하게 연결된다.
>
> <br>
>
> ### [HATEOAS 개념]
>
> > - 클라이언트가 전적으로 서버와 동적인 상호작용이 가능하도록 하는 것이 HATEOAS.
>
> <br>
>
> ### [HATEOAS 장점]
>
> > - 요청 URI 정보가 변경되더라도 클라이언트에서 동적으로 생성된 URI를 사용한다면, 클라이언트 입장에서는 URI 수정에 따른 코드 변경이 불필요하다.
> > - URI 정보를 통해 의존되는 요청을 예측 가능하게 한다. 코드로 확인하면 아래와 같다.
> >
> > ```http
> > GET /accounts/12345 HTTP/1.1
> > Host: bank.example.com
> > Accept: application/vnd.acme.account+json
> > 
> > // 클라이언트의 요청
> > ```
> >
> > ```JSON
> > HTTP/1.1 200 OK
> > Content-Type: application/vnd.acme.account+json
> > Content-Length: ...
> > 
> > {
> >     "account": {
> >         "account_number": 12345,
> >         "balance": {
> >             "currency": "usd",
> >             "value": 100.00
> >         },
> >         "links": {
> >             "deposit": "/accounts/12345/deposit",
> >             "withdraw": "/accounts/12345/withdraw",
> >             "transfer": "/accounts/12345/transfer",
> >             "close": "/accounts/12345/close"
> >         }
> >     }
> > }
> > 
> > // 서버의 응답 [계좌의 잔액이 마이너스가 아닌 경우]
> > 
> > /* 
> > 	응답의 의의 
> >     "account의 정보와 더불어 link 정보로 수행할 수 있는 의존되는 요청을 예측을 가능하게 한다."
> > ```
> >
> > ```json
> > HTTP/1.1 200 OK
> > Content-Type: application/vnd.acme.account+json
> > Content-Length: ...
> > 
> > {
> >     "account": {
> >         "account_number": 12345,
> >         "balance": {
> >             "currency": "usd",
> >             "value": -25.00
> >         },
> >         "links": {
> >             "deposit": "/accounts/12345/deposit"
> >         }
> >     }
> > }
> > 
> > // 서버의 응답 [계좌의 잔액이 마이너스인 경우]
> > 
> > /*
> > 	응답의 의의
> > 		"계좌의 잔액이 마이너스라면 응답 자체가 달라한다."
> > 		"따라서, 클라이언트가 의존 link가 달라진다는 것은, 클라이언트가 수행할 수 있는 의존이 바뀌는 것을 의미한다."
> > */
> > ```

<br>

<br>

# *4. Spring 프로젝트 구조 <sup>Spring Project Structure</sup>

> ###  [종류]
>
> > - 기능 기반 패키지 구조<SUP> package-by-feature</sup>
> > - 계층 기반 패키지 구조 <sup>package-by-layer</sup>
>
> <br>
>
> ### [종류 1/2 : 기능 기반 패키지 구조<SUP> package-by-feature</sup> ]
>
> ![image](https://user-images.githubusercontent.com/72078208/175759912-a8ecccf0-a056-401a-9fd7-9df6b83f955d.png)
>
> > 구현해야하는 기능을 기준으로 패키지를 구성하는 것.
> >
> > - 하나의 기능을 완성하기 위해 계층별 (API 계층, 서비스 계층, 데이터 액세스 계층) 클래스들이 모여있다.
> > - Spring boot 팀에서는 테스트와 리팩토링이 용이하고, 향후 마이크로 서비스 시스템으로 분리가 상대적으로 용이하여 기능 기반 패키지 구조 사용을 권장한다.
>
> <br>
>
> ### [종류 2/2 : 계층 기반 패키지 구조 <sup>package-by-layer </sup>]
>
> ![image](https://user-images.githubusercontent.com/72078208/175759990-c336d49e-63ec-4c50-92fd-464cdc230da2.png)
>
> > 클래스를계층별로 묶어 관리하는 형태

<br>

<br>

# *5. @SpringBootApplication

![image](https://user-images.githubusercontent.com/72078208/175760154-03f6dd5b-a955-4808-b904-36b7e076a548.png)

> ### [@SpringBootApplication 내부적 3가지 동작]
>
> - **@EnableAutoConfiguration** : 자동구성기능
> - **@ComponentScan** : `@Component`, `@Service`, `@Repository`, `@Controller`가 붙은 클래스를 검색하여 Spring Bean으로 등록하는 기능 (`@Configuration`도 내부적으로 `@Component`가 붙어있다.)
>
> <br>
>
> ### [내부 동작 : @EnableAutoConfiguration]
>
> > Spring boot의 meta 파일을 읽어서, 미리 정의 되어 있는 자바 설정 파일(@Configuration)들을 빈으로 등록한다. meta 데이터는 `spring.factories`라는 Spring boot의 파일이다.
>

<br>

<br>

# * 6. Spring Web MVC

> ### [개념]
>
> > - Spring MVC는 클라이언트의 요청을 편리하게 처리해주는 프레임워크이다.
> >   - Spring MVC는 웹 프레임워크의 한 종류이기 때문에 **Spring MVC 프레임워크**라고도 한다.
> > - Spring MVC 내부에서는 Servelt을 기반으로 웹 애플리케이션이 동작함을 기억한다.
>
> <br>
>
> ### [종류]
>
> > Model
> >
> > View
> >
> > Controller

<br>

## [1] Model

> ### [개념]
>
> > - 작업의 결과물
> >
> > > `클라이언트`가 웹에 어떠한 요청을 하면, 요청 사항을 처리하는 작업을 진행한다.
> > >
> > > 작업이 된 데이터를 `클라이언트`에게 돌려줘워야하는데, 이 때 클라이언트에게 응답으로 돌려주는
> > > <u>**`작업의 처리 결과 데이터`**</u>를 **Model**이라고 한다.
>
> <br>
>
> ### [알아두기]
>
> > - **Service Layer** :: 클라이언트의 요청 사항을 구체적으로 처리하는 영역을 가리킨다.
> > - **Business Logic** :: 실제로 요청 사항을 처리하기 위해 Java 코드로 구현한 것을 의미한다.

<br>

<br>

## [2] View

> ### [개념]
>
> > - `클라이언트 애플리케이션`의 화면에 보여지는 <u>리소스를 제공하는 역할</u>을 한다.
> > - `Presentation Layer`에 속한다.
>
> <br>
>
> ### [View의 형태]
>
> > - HTML 페이지 출력
> > - PDF, Excel 등의 문서 형태로 출력
> > - XML, JSON 등 특정 형식의 포맷으로 변환
> >   - Model 데이터를 특정 프로토콜 규약에 맞추어 변환 후, 변환된 데이터를 클라이언트 측에 전송하는 방식이다.
> >   - `백엔드`에선특정 형태 형식으로 포맷하여 데이터만 전송하고 데이터를 받아 `프론트`쪽에서 HTML 페이지를 만드는 방식이다.
>
> <br>
>
> <br>
>
> ### [JSON은 자주 쓰이는 포맷 형식이다]
>
> #### [장점]
>
> > - `프론트`와 `백엔드`의 명확한 구분으로 유지보수가 상대적으로 용이하다.
> > - `프론트` 측에서 **비동기 클라이언트 애플리케이션**을 만드는 것이 가능해진다.
>
> <br>
>
> ### [ [java] Gson Type을 이용해 Json 형태로 포맷하는 예시 ]
>
> ```java
>  public class JsonExample {
>         public static void main(String[] args) {
>             // Coffee class는 있다고 가정한다.
>             Coffee coffee = new Coffee("아메리카노", "Americano", 3000);
>             Gson gson = new Gson();
>             String jsonString = gson.toJson(coffee);
> 
>             System.out.println(jsonString);
>         }
>     }
> 
> 
> /* 
> 		output
> 		---------------------------------------------------------------
> 		{"korName":"아메리카노","engName":"Americano","price":3000}
> 		---------------------------------------------------------------
> */
> ```





## [3] Controller

> ### [개념]
>
> > - `클라이언트` 측의 요청을 직접적으로 전달 받는 Endpoint이다.
> > - Model과 View의 중간에서 상호 작용의 역할을 한다.
> > - 즉, 데이터를 전달할 때 **<u>Business Logic -> Model ------(Controller)-------> View</u>**의 형식으로 동작하여 Modle 데이터를 View로 전달하는 역할을 한다.



# * 7. Spring 기본 3가지 계층 구조<sup> Three Tier in Spring Web MVC</sup>

![image](https://user-images.githubusercontent.com/72078208/175764811-e724d9d5-7900-4078-a207-b1411d29d338.png)

> ### [계층 구분의 필요성]
>
> > - 계층마다의 독립적으로 분리하여 구현하는 것이 가능해진다.
> > - 효율적인 개발 및 유지보수를 위함
>
> <br>
>
> ### [계층의 종류]
>
> > - Presentation Layer
> > - Service Layer
> > - Data Access Layer
> > - (+ Domain Model)
>
> <br>
>
> ### [계층간의 통신]
>
> > - 정말 특별한 경우가 아닌 이상 인터페이스를 이용한다. (다형성 보장 및 유지보수에 용이하다.)
> > - 인터페이스로 통신해야 단위 테스트도 매우 용이하다.
>
> <br>
>
> ### [계층의 종류 1/3 : Presentation Layer]
>
> > MVC Design Pattern에서의 `Controller`와 `View`가 해당 계층에 포함된다.
> >
> > > - <u>웹 클라이언트</u>의 <u>요청 및 응답을 처리</u>한다.
> > > - 서비스 계층과 데이터 액세스 계층에서 발생하는 Exception에 대한 처리를 한다.
> > > - 유효성 검증 기능(@Validation)을 프리젠테이션 계층에서 제공한다.
>
> <br>
>
> ### [계층의 종류 2/3 : Service Layer]
>
> > 같이보면 좋은 글 : <a herf = "https://velog.io/@nnoshel/Service-Layer%EA%B3%BC-Domain-Model" target = "_blank"> Service Layer과 Domain Model</a>>, nnoshel
> >
> > - Service와 Domain의 역할에 대해서 많이 헷갈렸는데 해당 블로그로 어느정도 해소되었다.
>
> > - - Service Layer에서의 비즈니스 로직 처리 : 직접적으로 많은 처리는 하지 않는다.
> >   - 핵심 비즈니스 로직의 처리는 Domain Model에서 이루어져야한다.
> >   - Service Layer는 트랜잭션과 도메인간의 순서만 보장해야한다.
> > - 트랜잭션을 관리한다. (@Transection)
> > - Presentation 계층과 Data Access 계층 사이를 통신하기 위해 사용한다.
> > - 즉 Presentation 계층과 Data Access 계층은 직접적으로 통신하지 않게 한다.
> > - Presentaion Layer와 Data Access Layer를 연결하기 위해 사용한다.
>
> <br>
>
> ### [계층의 종류 3/3 : Data Access Layer]
>
> > - Database를 접근하는 영역
> > - DAO(Data Access Object) 영역이라고 보면 된다.
>
> <br>
>
> ### [계층의 종류 +a/3 : Domain]
>
> > - 도메인이라고 불리는 개발 대상을 java의 코드로 단순화 시킨 것을 의미한다.
> > - 여기서의 개발 대상이란, 하나의 목적성을 처리하기 위해 만들어진 객체들을 의미한다.
> >   - 예로, 쇼핑몰의 `구매`를 위해 사용되는 `Member`,`Product`,`Purchase`등을 도메인 객체라고 말할 수 있다. 해당 클래스들 안에는 각각의 객체가 처리해야할 기능인 핵심 비즈니스 로직을 포함한다.
> > - 비즈니스 로직을 처리한다.
> > - `@Entity`가 사용된 영역도 도메인 모델이다. (@Entity를 사용한 클래스는 Entity class라고도 한다.)
> > - 무조건적으로 Database와 관계가 있어야 하는 것은 아니다.
>
> <br>
>
> ### [Domain에서 비즈니스 로직처리를 하는 이유]
>
> > 참고자료 
> >
> > - <a href="https://kyu9341.github.io/java/2020/07/08/java_springBoot_Layer/#%EC%99%9C-%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4-%EB%A1%9C%EC%A7%81%EC%9D%84-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B3%84%EC%B8%B5%EC%97%90-%EC%9E%91%EC%84%B1%ED%95%98%EB%8A%94%EA%B0%80" target="_blank">스프링 - 계층구조</a>
>
> > 위의 블로그에서는 Domain Model에서 비즈니스 로직을 처리한다고 소개한다.
> >
> > 이유는 Service Layer는 다양한 Domain을 읽어 처리하고 제공한다고 한다. 복잡한 서비스는 더 많은 Domain을 읽어 서비스를 제공하기 때문에 모든 처리를 Service로직에서 처리한다면 Service 로직의 복잡도는 증가할 수 밖에 없는데, 복잡도를 낮추고자 최대한 비즈니스 로직을 Domain쪽으로 이동시킨다고한다. 이로써 유지보수와 테스트를 하기 쉬운 코드가 생성이되고 유연한 소프트웨어를 얻을 수 있다고 한다.

<br>

# * 8. VO<sup>Value Object</sup> DTO<SUP>Data Transfer Object</sup>

> 참고자료
>
> - <a href = "https://lemontia.tistory.com/591" target="_blank">[JAVA] DAO, DTO, VO 차이</a>, side impact, tistory
> - <a href = "https://tecoble.techcourse.co.kr/post/2021-05-16-dto-vs-vo-vs-entity/" target="_blank">DTO vs VO vs Entity</a>, 3기_다니, tecoble
> - 10분

>### [미묘하게 다른 VO와 DTO]
>
>> VO는 불변성의 성격을 갖는 반면, DTO는 가변성의 성격을 갖는다.
>> 보통 혼용해서 쓰이는데, 중요한 차이를 상기하며 잘 기억하도록 한다.
>
><br>
>
>### [VO의 개념]
>
>> READ only의 성격
>>
>> - VO 클래스는 `getter`만 갖는다. (setter는 가지지 않는다.)
>> - 이러한 클래스는 중간에 값을 바꿀 수 없고 새로 만들어야하는 특징이 존재한다.
>
><br>
>
>### [DTO의 개념]
>
>> Getter와 Setter 모두를 갖는다.
>>
>> - getter와 setter를 모두 갖기 때문에 가변성이 있다.
>> - 추가적인 개념은 아래 9번을 참고한다.

<br>
<br>

# * 9. DTO <sup>Data Transfer Object</sup>와 Entity class의 분리 이유

> 참고자료
>
> - <a href="https://gmlwjd9405.github.io/2018/12/25/difference-dao-dto-entity.html" target="_blank">[DAO] DAO, DTO, Entity class</a>, heejeong Kwon, github.id

> ![image](https://user-images.githubusercontent.com/72078208/175767732-0eafa695-62f2-4339-a322-3f6eff758af9.png)
>
> ### [DTO란?]
>
> > - 계층간 데이터 교환을 위한 객체(JavaBeans)이다.
> >   - DB에서 데이터를 얻어 Service나 Controller 등으로 보낼 때 사용하는 객체를 말한다.
> >   - 주로 Controller와 View사이에서 데이터를 교환할 때 활용한다.
> >   - 로직을 갖고 있지 않은 순수한 객체이며 `@getter/@setter`메소드만 갖는다. (가변성을 갖는다.)
>
> <br>
>
> ### [Entity class란?]
>
> > - 실제 Database의 table과 매칭이 될 클래스를 말한다.
> > - 최대한 `getter`메소드를 밖에서 구현하지 않도록 안에서 구현한다.
> >   - 단, Domain Logic만 존재해야하며 Presentation Logic을 가져서는 안 된다.
> >   - 여기서 구현된 메소드는 주로 Service Layer에서 사용된다.
>
> <br>
>
> ### [DTO와 Entity class의 분리 이유]
>
> > 우선, DTO는 `getter`와 `setter`만을 가질 수 밖에 없지만 Entity class는 기능을 가질 수 있다.
> > 즉, Entity class는 핵심 로직을 갖는 Domain Model이라고 볼 수 있다는 것이다. 인터넷을 보고 가져온 이유들은 아래와 같다.
> >
> > > - View Layer와 DB Layer의 역할을 철저하게 분리하기 위함
> > > - View와 통신하는 DTO클래스(Request / Response 클래스)는 자주 변경이 되지만, 테이블과 매핑이되어지는 Entity class는 자주 변경이 되면 안 된다. 이유는, Entity 클래스가 변경되면 다른 여러 클래스들에게 영향을 끼치기 때문이다.
