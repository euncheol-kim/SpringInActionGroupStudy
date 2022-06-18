## Spring - Rest 서비스 생성하기

### <span style="color:red">**1. Rest 컨트롤러 작성하기**</span>

#### @Controller vs @RestController

> 전통적인 Spring MVC 컨트롤러인 @Controller는 주로 View를 반환하기 위해 사용
>
> > + 아래의 과정을 통해 Spring MVC Container는 Client의 요청으로부터 View 반환



##### **1. Controller로 View 반환하기**

<img src="C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612171801348.png" alt="image-20220612171801348" style="zoom:67%;" align="left" />

###### <span style ="black">**Spring MVC Container의 동작 과정**</span>

1. Client는 URI 형식으로 웹 서비스에 요청을 보냄
2. DispatcherServlet이 요청을 위임할 HandlerMapping을 찾음
3. HandlerMapping을 통해 요청을 Controller로 위임
4. Controller는 요청을 처리한 후에 View Name을 반환
5. DispatcherServlet은 ViewResolver를 통해 ViewName에 해당하는 View를 찾아 사용자에게 반환 



> **Controller가 반환한 View의 이름으로부터 View를 렌더링 하기 위해서는 View Resolver가 사용되며, ViewResolver 설정에 맞게 View를 찾아 렌더링**



##### **2. Controller로 Data 반환하기**



<img src="C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612210101978.png" alt="image-20220612210101978" style="zoom:67%;" align="left"/>

###### <span style ="black">**Spring MVC Container의 동작 과정**</span>

1. Client는 URI 형식으로 웹 서비스에 요청을 보냄
2. DispatcherServlet이 요청을 위임할 HandlerMapping을 찾음
3. HandlerMapping을 통해 요청을 Controller로 위임
4. Controller는 요청을 처리한 후에 객체를 반환
5. 반환되는 객체는 Json으로 Serialize 되어 사용자에게 반환



> 컨트롤러를 통해 객체 반환 시 일반적으로 ResponseEntity로 감싸서 반환
>
> 객체를 반환하기 위해서는 viewResolver 대신에 HttpMessageConverter가 동작
>
> > HttpMessageConverter : 여러 Converter가 등록, 반환해야 하는 데이터에 따라 Converter가 달라짐

+ 단순 문자열 : StringHttpMessageConverter
+ 객체 : MappingJackson2HttpMessageConverter
+ Spring은 클라이언트의 HTTP Accept 헤더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해 적합한 HttpMessageConverter를 선택 후 처리
+ 컨트롤러에서 응답을 반환하면 디스패처 서블릿으로 응답이 바로 전달되지 않고 중간에 핸들러 어댑터를 거치는데, 이 핸들러 어댑터 내부에서 메세지 컨버터에 의해 변환



```java
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
	
    //데이터를 반환
    @GetMapping(value = "/users")
    public @ResponseBody ResponseEntity<User> findUser(@RequestParam("userName") String userName){
        return ResponseEntity.ok(userService.findUser(user));
    }
    
    //View를 반환
    @GetMapping(value = "/users/detailView")
    public String detailView(Model model, @RequestParam("userName") String userName){
        User user = userService.findUser(userName);
        model.addAttribute("user", user);
        return "/users/detailView";
    }
}
```

​	**<코드 분석>**

+ findUser
  + User 객체를 ResponseEntity로 감싸서 반환
  + @ResponseBody 를 이용해 User를 json으로 반환하기 위해 사용
+ detailView
  + View를 전달해주고 있기 때문에 String을 반환값으로 설정
+ ResponseEntity
  + 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스(HttpRequest에 대한 응답 데이터가 포함)
  + 구조
    + HttpStatus - 요청/응답에 성공했는지의 여부
    + HttpHeaders - 요청/응답에 대한 요구사항
    + HttpBody - 요구사항에 대한 내용



![image-20220612212036210](C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612212036210.png)



##### **RestController**

> @Controller에 @ResponseBody가 추가된 것
>
> > 대부분 Json 형태로 객체 데이터를 반환
> >
> > 최근 데이터를 응답으로 제공하는 REST API를 개발할 때 주로 사용하며 객체를 ResponseEntity로 감싸서 반환 
> >
> > 동작과정이 @Controller에 @ResponseBody를 붙인것과 동일
> >
> > > 지정된 클래스를 스프링의 컴포넌트 검색으로 찾을 수 있음
> > >
> > > 컨트롤러의 모든 HTTP 요청 처리 메서드에서 HTTP 응답 몸체에 직접 쓰는 값을 반환한다는 것을 spring에 알려줌

<img src="C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612213249368.png" alt="image-20220612213249368" style="zoom:67%;" align = "left"/>

###### <span style ="black">**Spring MVC Container의 동작 과정**</span>

1. Client는 URI 형식으로 웹 서비스에 요청을 보냄
2. DispatcherServlet이 요청을 위임할 HandlerMapping을 찾음
3. HandlerMapping을 통해 요청을 Controller로 위임
4. Controller는 요청을 처리한 후에 객체를 반환
5. 반환되는 객체는 Json으로 Serialize 되어 사용자에게 반환



```java
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/users")
    public User findUser(@RequestParam("userName") String userName){
        return userService.findUser(user);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<User> findUserWithResponseEntity(@RequestParam("userName") String userName){
        return ResponseEntity.ok(userService.findUser(user));
    }
}
```



#### **<정리>**

> 예전에 프로그래밍을 할 때에는 jsp나 html과 같은 뷰를 전달해 주었기 때문에 @Controller를 사용해왔었지만, 최근에는 프론트엔드와 백엔드를 따로 두고, 백엔드에서는 REST API를 통해 json으로 데이터만 전달하기 때문에 편리성을 위해 @RestController를 사용



### <span style="color:red">**2. SPA vs MPA**</span>

#### **SPA**

> 한개의(Single)의 Page로 구성된 Application

+ 웹 어플리케이션에 필요한 모든 정적 리소스(HTML, CSS, JavaScript)를 최초 한번에 다운로드 -> 이후 데이터를 받아올때만 서버와 통신
  + 첫 요청시 딱 한페이지만 불러오고 페이지 이동 시 기존 페이지의 내부를 수정해서 보여주는 방식
+ 클라이언트 관점에서 최초 페이지를 로딩한 시점부터는 페이지 리로딩 없이 필요한 부분만 서버로 부터 받아서 화면 갱신
+ 네이티브 앱에 가까운 페이지 이동과 사용자 경험(UX) 제공
+ Angular, React, Vue등 프론트엔드 기술이 나오며 크게 유행
+ SEO(검색 엔진 최적화)에 불리

<img src="C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612215734114.png" alt="image-20220612215734114" style="zoom:50%;" align="left"/>

**장점**

+ 자연스러운 사용자 경험(UX)
+ 필요한 리소스만 부분적으로 로딩
  + SPA의 Application은 서버에게 정적 리소드를 한번만 요청
  + 받은 데이터는 전부 저장(캐시)
+ 서버의 템플릿 연산을 클라이언트로 분산
+ 컴포넌트 별 개발 용이
+ 모바일 앱 개발을 염두에 둔다면 동일한 API를 사용하도록 설계 가능(생산성)



**단점**

+ JavaScript 파일을 번들링 해서 한 번에 받기에 초기 구동이 느림
  + Webpack의 code splitting으로 해결가능
+ 검색엔진최적화가 어려움
+ 보안 이슈(프론트엔드에서 비즈니스 로직 최소화)
  + SSR에서는 사용자에 대한 정보를 서버측에서 세션으로 관리하지만 CSR 방식에서는 클라이언트측의 쿠키 말고는 사용자에 대한
    정보를 저장할 공간이 애매함



#### MPA

> 여러개의(Multi)의 Page로 구성된 Application

+ 새로운 페이지를 요청할 때마다 정적 리소스(HTML, CSS, JavaScript)가 다운
+ 페이지 이동을 하거나 새로고침하면 전체 페이지를 다시 렌더링

<img src="C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220612215144184.png" alt="image-20220612215144184" style="zoom:50%;" align = "left"/>

**장점**

+ SEO(검색 엔진 최적화) 관점에서 유리
  + MPA는 완성된 형태의 HTML 파일을 서버로 부터 전달받음 -> 검색엔진이 페이지를 크롤링 하기 적합
+ 첫 로딩이 매우 짧음
  + 서버에서 이미 렌더링해 오기 때문
  + 클라이언트가 JS파일을 모두 다운로드하고 적용하기 전까지는 각각의 기능은 동작하지 않음



**단점**

+ 새로운 페이지를 이동하면 리로딩(새로고침)발생
+ 페이지 이동시 불필요한 템플릿도 중복해서 로딩
+ 서버 렌더링에 따른 부하
+ 모바일 앱 개발시 추가적인 백엔드 작업 필요 -> 개발(생산성)이 복잡



#### SSR - Server Side Rendering

+ 모든 템플릿을 서버 연산을 통해 렌더링하고 완성된 페이지 형태로 응답하는 과정
+ SEO의 장점
+ 대부분 MPA



#### CSR - Client Side Rendering

+ 최초에 한번 서버에서 전체 페이지를 로딩하여 보여주고 이후에는 사용자의 요청이 올 때마다, 리소스를
  서버에서 제공한 후 클라이언트가 해석하고 렌더링하는 방식
+ SEO가 어려움



### <span style="color:red">**3. RESTful API**</span>

### **REST**(REpresentational State Transfer)

> HTTP 통신에서 어떤 자원에 대한 CRUD요청을 Resource와 Method로 표현하여 특정한 형태로 전달하는 방식
>
> > 어떤 자원에 대해 CRUD(Create, Read, Update, Delete)연산을 수행하기 위해 URI(Resource)로 요청을 보내는 것

+ Get, Post 등의 방식(Method)을 사용하여 요청

+ 요청을 위한 자원은 특정한 형태(Representation of Resource)로 표현

+ 이러한 REST 기반의 API를 웹으로 구현한 것이 RESTful API

+ 확장성 있는 웹 애플리케이션을 설계하기 위한 일종의 제약




##### **RESTful API 구성요소**

+ Resource

> 서버는 Unique한 ID를 가지는 Resource를 가지고 있으며, 클라이언트는 이러한 Resource에 요청을 보냄



+ Method

> 서버에 요청을 보내기 위한 방식으로 GET, POST, PUT, PATCH, DELETE
>
> CRUD 연산 중에서 처리를 위한 연산에 맞는 Method를 사용하여 서버에 요청을 보내야 함



+ Represontation of Resource

> 클라이언트와 서버가 데이터를 주고받는 형태로, json, xml, text, rss 등이 있음
>
> 최근에는 Key, Value를 활용하는 json을 주로 사용



*추가 정보

URL vs URL

> URL은 Uniform Resource Locator로 인터넷 상 자원의 위치를 의미 
>
> 자원의 위치라는 것은 결국 어떤 파일의 위치를 의미
>
> 반면에 URI는 Uniform Resource Identifier로 인터넷 상의 자원을 식별하기 위한 문자열의 구성으로, URI는 URL을 포함하게 됨
>
> 그러므로 URI가 보다 포괄적인 범위라고 할 수 있음



#### **REST의 조건**

 1. **Uniform Interface(일관된 인터페이스)**

  > Uniform Interface란, Resource(URI)에 대한 요청을 통일되고, 한정적으로 수행하는 아키텍처 스타일을 의미
  >
  >  이것은 요청을 하는 Client가 플랫폼(Android, Ios, Jsp 등) 에 무관하며, 특정 언어나 기술에 종속받지 않는 특징을 의미
  >
  > 이러한 특징 덕분에 Rest API는 HTTP를 사용하는 모든 플랫폼에서 요청가능하며, Loosely Coupling(느슨한 결함) 형태를 갖게 되었음

  2. **Stateless(무상태성)**

  > 서버는 각각의 요청을 별개의 것으로 인식하고 처리해야하며, 이전 요청이 다음 요청에 연관되어서는 안됨.
  >
  > 그래서 Rest API는 세션정보나 쿠키정보를 활용하여 작업을 위한 상태정보를 저장 및 관리하지 않음
  >
  > 이러한 무상태성때문에 Rest API는 서비스의 자유도가 높으며, 서버에서 불필요한 정보를 관리하지 않으므로 구현이 단순
  >
  > 이러한 무상태성은 서버의 처리방식에 일관성을 부여하고, 서버의 부담을 줄이기 위함

  3. **Cacheable(캐시 가능)**

  > Rest API는 결국 HTTP라는 기존의 웹표준을 그대로 사용하기 때문에, 웹의 기존 인프라를 그대로 활용 가능
  >
  > 그러므로 Rest API에서도 캐싱 기능을 적용할 수 있는데, HTTP 프로토콜 표준에서 사용하는 Last-Modified Tag 또는 E-Tag를 이용하여 캐싱을 구현할 수 있고, 이것은 대량의 요청을 효울척으로 처리할 수 있게 도와줌

  4. **Client-Server Architecture (서버-클라이언트 구조)**

  > Rest API에서 자원을 가지고 있는 쪽이 서버, 자원을 요청하는 쪽이 클라이언트에 해당.
  >
  > 서버는 API를 제공하며, 클라이언트는 사용자 인증, Context(세션, 로그인 정보) 등을 직접 관리하는 등 역할을 확실히 구분시킴으로써 서로 간의 의존성을  줄여줌

  5. **Self-Descriptiveness(자체 표현)**

  > Rest API는 요청 메세지만 보고도 이를 쉽게 이해할 수 있는 자체 표현 구조
  >
  > 아래와 같은 JSON 형태의 Rest 메세지는 http://localhost:8080/board 로 게시글의 제목, 내용을 전달하고 있음을 손쉽게 이해할 수 있음
  >
  > 또한 board라는 데이터를 추가(POST)하는 요청임을 파악 가능

```json
HTTP POST , http://localhost:8080/board
{
	"board":{
		"title":"제목",
		"content":"내용"
	}
}
```

  6. **Layered System(계층 구조)**

  > Rest API의 서버는 다중 계층으로 구성될 수 있으며 보안, 로드 밸런싱, 암호화 등을 위한 계층을 추가하여 구조를 변경 가능
  >
  > 또한 Proxy, Gateway와 같은 네트워크 기반의 중간매체를 사용할 수 있게 해줌
  >
  > 하지만 클라이언트는 서버와 직접 통신하는지, 중간 서버와 통신하는지 알 수 없음



#### REST의 규칙

1. URI는 명사를 사용
2. 슬래시로 계층 관계를 표현
3. URI의 마지막에는 슬래시를 붙이지 않음
4. URI는 소문자로만 구성
5. 가독성이 떨어지는 경우 하이픈을 사용



#### **스프링 MVC의 HTTP 요청-처리 애노테이션**

| 애노테이션      | HTTP 메서드                                           | 용도               |
| --------------- | ----------------------------------------------------- | ------------------ |
| @GetMapping     | HTTP GET 요청                                         | 리소스 데이터 읽기 |
| @PostMapping    | HTTP POST 요청                                        | 리소스 생성하기    |
| @PutMapping     | HTTP PUT 요청                                         | 리소스 변경하기    |
| @PatchMapping   | HTTP PATCH 요청                                       | 리소스 변경하기    |
| @DeleteMapping  | HTTP DELETE 요청                                      | 리소스 삭제하기    |
| @RequestMapping | 다목적 요청처리<br />HTTP 메서드가 method 속성에 지정 |                    |



### <span style="color:red">**4. REST 컨트롤러 작성하기**</span>

#### 	1. 서버에서 데이터 가져오기(GetMapping)

<DesignTacoController>

```java
package tacos.web.api;

import ...

@RestController
@RequestMapping(path="/design",     				// /design 경로의 요청 처리
                produces="application/json")        // produces : 반환하는 데이터의 타입을 정의 <-> consumes : 들어오는 데이터의 타입 정의
@CrossOrigin(origins="*")        
    // 서로 다른 도메인 간의 요청을 허용, 현재 앵귤러 코드와 해당 api 코드는 별도의 도메인(호스트와 포트 어느 하나라도 다른)에서 실행중
public class DesignTacoController {
  private TacoRepository tacoRepo;
  
  @Autowired
  EntityLinks entityLinks;

  public DesignTacoController(TacoRepository tacoRepo) {
    this.tacoRepo = tacoRepo;
  }

  @GetMapping("/recent")
  public Iterable<Taco> recentTacos() {                 //최근 생성된 타코 디자인들을 가져와서 반환
    PageRequest page = PageRequest.of(
            0, 12, Sort.by("createdAt").descending());
    return tacoRepo.findAll(page).getContent();
  }
    
  @GetMapping("/{id}") // Class의 경로까지 합치면 /desing/{id}에 해당하는 경로의 GET 요청 수행, {id} -> 플레이스 홀더
  public Taco tacoById(@PathVariable("id") Long id) {
    Optional<Taco> optTaco = tacoRepo.findById(id);
    if (optTaco.isPresent()) {
      return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
  }

```

+ RequestMapping()
  + path = "경로" : "경로"로 들어오는 요청 처리
  + produces : 반환하는 데이터의 타입을 정의
  + consumes : 들어오는 데이터의 타입을 정의
    + "application/json" -> json
    + "text/html" -> xml
    + "text/plain;charset=UTF-8" -> DB에 한글 데이터가 return시 한글이 깨지는 현상 발생 시 사용 가능
+ @CrossOrigin()
  + CORS(Cross-origin resource sharing)
    + 웹 페이지의 제한된 자원을 외부 도메인에서 접근을 허용해주는 메커니즘
    + Ajax 등을 통해 다른 도메인의 서버에 url(data)를 호출할 경우 XMLHttpRequest는 보안상의 이유로 자신과 동일한 도메인으로만 HTTP요청을 보내도록 제한하고 있어 에러가 발생
      내가 만든 웹서비스에서 사용하기 위한 rest api 서버를 무분별하게 다른 도메인에서 접근하여 사용하게 한다면 보안상 문제가 될 수 있기 때문에 제한하였지만 지속적으로 웹 애플리케이션을 개선하고 쉽게 개발하기 위해서는 이러한 request가 꼭 필요하였기에 그래서 XMLHttpRequest가 cross-domain을 요청할 수 있도록하는 방법(CORS)이 고안
  + 도메인
    + 프로토콜과 호스트 및 포트로 구성
  + @CrossOrigin(origins="허용주소:포트")

+ recentTacos()
  + PageRequest
    + 몇 페이지, 한페이지의 사이즈, Sorting 방법(Option)을 가지고 Repository에 Paging을 요청할 때 사용

![image-20220613184804332](C:\Users\LG\AppData\Roaming\Typora\typora-user-images\image-20220613184804332.png)

+ tacoById()
  + 타코 ID로 특정 타코만 가져오는 엔드포인트 제공
  + 플레이스 홀더
    + 변수의 타입을 미리 설정해 놓고 필요한 변수를 나중에 받아서 실행하는 것을 의미
    + @PathVariable
      + Controller 단에서 클라이언트에서 URL에 파라미터를 같이 전달해야 하는 경우
      + 여기서는 플레이스홀더와 대응되는 id 매개변수에  해당 요청의 실제 값이 지정



#### 	2. 서버에서 데이터 전송하기(PostMapping)

<DesignTacoController>

```java
package tacos.web.api;

import ...

@RestController
@RequestMapping(path="/design",     				// /design 경로의 요청 처리
                produces="application/json")        // produces : 반환하는 데이터의 타입을 정의 <-> consumes : 들어오는 데이터의 타입 정의
@CrossOrigin(origins="*")        
    // 서로 다른 도메인 간의 요청을 허용, 현재 앵귤러 코드와 해당 api 코드는 별도의 도메인(호스트와 포트 어느 하나라도 다른)에서 실행중
public class DesignTacoController {
  private TacoRepository tacoRepo;
  
  @Autowired
  EntityLinks entityLinks;

  public DesignTacoController(TacoRepository tacoRepo) {
    this.tacoRepo = tacoRepo;
  }

	...

  @PostMapping(consumes="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Taco postTaco(@RequestBody Taco taco) {
    return tacoRepo.save(taco);
  }
```

+ postTaco()
  + HTTP POST 요청을 처리
  + path 속성을 지정하지 않아 /design 경로에 대한 요청 처리
  + @RequestBody
    + 요청 본문
    + 요청 몸체의 JSON 데이터가 Taco 객체로 변환되어 바인딩된다는 것을 나타냄
  + @ResponseStatus
    + 응답요청의 결과로 리소스가 생성되면 HTTP 201(CREATED)상태 코드가 클라이언트에 전달



#### 3. 서버에서 데이터 변경하기(PutMapping, PostMapping)

**PUT**

> 데이터를 변경하는 데 사용되기는 하지만, 실제로는 GET과 반대의 의미
>
> > 즉, GET 요청은 서버로부터 클라이언트로 데이터를 전송하는 방면, PUT 요청은 클라이언트로부터 서버로 데이터를 전송
> >
> > 따라서, 변경 보다는 **데이터 교체**의 의미를 가짐

**PATCH**

> 데이터의 일부분을 변경



```java
@PutMapping("/{orderId}")
public Order putOrder(@ReqeustBody Order order){
	return repo.save(order);
}
```

+ 해당 주문 전체에 대해 PUT 요청
+ 전체 데이터를 받고 저장



```
@PatchMapping(path="/{orderId}", consumes="application/json")
public Order patchOrder(@PathVariable("orderId") Long orderId, @RequestBody Order patch){
	Order order = repo.findById(orderId).get();
	if(patch.getDeliveryName() != null){
		order.setDeliveryName(patch.getDeliveryName());
	}
	if(patch.getDeliveryCity() != null){
		order.setDeliveryCity(patch.getDeliveryCity());
	}	
	if(patch.getDeliveryName() != null){
		order.setDeliveryName(patch.getDeliveryName());
	}
	...
	
	return repo.save(order);
}
```

+ 주문의 디테일만 수정이 가능
+ 데이터의 일부분만 변경하기 위한 로직 필요
+ 실제로 변경을 수행하는 코드는 직접 작성해야 함.



#### 4. 서버에서 데이터 삭제(@DeleteMapping)

```java
@DeleteMapping("/{orderId}")
@ResponseStatus(code=HttpStatus.No_CONTENT)
public void deleteOrder(@PathVariable("orderId") Long orderId){
	try{
		repo.deleteById(orderId);
	} catch(EmptyResultDataAccessException e){}
}
```

+ EmptyResultDataAccessException
  + catch후 아무것도 하지 않음
    + 비어있는 경우 예외가 발생하는데 이 상태가 주문이 삭제된 것처럼 특별히 할 것이 없기 때문
    + null 로 지정된 ResponseEntity와 'NOT FOUND' Http 상태 코드를 deleteOrder() 메서드에서 반환하게 할 수 있음



#### **Angular(앵귤러)**

> HTML과 TypeScript로 클라이언트 SPA를 개발할 때 사용되는 플랫폼이자 프레임워크

