

---



### **👨‍👨‍👦‍👦 SpringInAction Group Study [이채민, 권태구, 최재호, 곽현기, 김은철]**

**group - GIT : https://github.com/euncheol-kim/SpringInActionGroupStudy**

<br>

#### **이채민 님 : https://github.com/CokeLee777**

#### **권태구 님 : https://github.com/Hashtae9**

#### **최재호 님 : https://github.com/jaero0725**

#### **곽현기 님 : https://github.com/nicebyy**

#### **김은철 (본인): https://github.com/euncheol-kim**

<br>

#### 작성자 : 김은철 

<br>

-----

<br>

<br>

### 발표 범위

> [ Chap 04 - 01 ] 스프링 시큐리티 활성화 하기  

<br><br>

### goal

>  작성해야합니다.



# 1 ] Spring security를 시작하기 전에

## * 보안 용어

| 보안 용어             | 설명                                                         |
| --------------------- | ------------------------------------------------------------ |
| 접근 주체 (Principle) | 보호된 리소스에 접근하는 대상                                |
| 인증 (Authentication) | 보호된 리소스에 접근한 대상에 대해 이 유저가 누구인지,<br />애플리케이션의 작업을 수행해도 되는 주체인지 확인하는 과정 |
| 인가 (Authorize)      | 인증 이후에 진행되는 과정이며, <br />접근 주체가 어떤 접근 권한을 갖고 있는지 확인하는 과정 |
| 권한                  |                                                              |



# 2 ] 스프링 시큐리티 활성화 하기 (설정)

## [1] 의존성 설정

- **pom.xml**

```xml

<!-- 스프링 부트 보안 스타터 의존성 --> 
<dependency>
	<groupId> org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- 보안 테스트 의존성 -->
<dependency>
	<gropuId> org.springframework.boot</groupId>
	<artifactId>spring-security-test</artifactId>
	<scope>test</scope>
</dependency>
```



### 1. `스프링 부트 보안 스타터`를 설정함으로써 자동으로 제공받는 기능

> 1. **`모든` HTTP 요청 경로는 인증**(authentication)되어야 한다.
> 2. 어떤 특정 역할이나 권한이 없다.
> 3. 로그인 페이지가 따로 없다.
> 4. 스프링 시큐리티의 HTTP 기본 인증을 사용해서 인증된다. 
>    - 기본 로그인 페이지를 Spring Security 내부에서 제공한다.
>
> 5. 사용자는 하나만 있으며, 이름은 user이고 비밀번호는 암호화해주며 log를 통해 제공 받는다.
>    - 사용자가 하나만 있다는 것은 user아이디 하나만 존재한다는 것



### 2. (교재 외) Spring Security 의존성 추가 시 내부 동작

> 1. **서버가 기동되면** **스프링 시큐리티의 초기화 작업 및 보안 설정이 진행**된다.
> 2. 별도의 설정이나 구현을 하지 않아도 기본적인 보안 기능이 자동 구성되어 구동된다.
>    - 1번의 <스프링 부트 보안 스타터를 설정함으로써 자동으로 제공받는 기능>





# 3 ] JWT

> https://brunch.co.kr/@jinyoungchoi95/1
>
> https://bcp0109.tistory.com/301
>
> https://dololak.tistory.com/535
>
> https://universitytomorrow.com/22



## [1] JWT를 설명하기 전에 Cookie란 ??

###  1. Cookie의 등장 배경

<u>HTTP(웹환경)의 특성상</u> <u>**매번 발생하는 HTTP 트랜잭션**은 별개의 요청으로 판단하기에 상태를 가질 수 없다.</u>

※ 트랜잭션 : 작업단위

<br>

> 쉽게 이해하자면 page1 요청 후에, page2를 요청할 경우 이 둘의 요청은 서로 연관성을 가지지 않고 독립적이어서 page1에서 만들어진 데이터는 page2를 요청할 때 유지되지 않는다는 것이다.

이러한 HTTP의 특성 때문에 HTTP를 **stateless protocol(무상태 프로토콜)**이라고 한다.



###  2. Cookie는 필연적인 데이터의 유지를 하기 위해서 사용하기 위해 등장

개발을 하다보면 필연적으로 여러 요청에 걸쳐 상태를 유지해야하는 경우가 생긴다.

> 1. 로그인 이후, 로그인의 상태가 유지되어야하는 경우
> 2. 쇼핑몰 장바구니를 담은 데이터가 다른 페이지 이동시에도 유지되어야 하는 경우
> 3. 회원가입시에 입력된 정보가 페이지 전환시에도 유지되어야하는 경우

<br>

**`cookie`는 데이터를 브라우저(사용자)측에 저장**해두고 <u>매번 요청시마다 서버로 쿠키를 전송</u>한다.

따라서, 사용자에 의한 페이지 전환 / 새로고침이 되더라도 데이터가 저장된 상태이기 때문에 온전히 받은 데이터가 유지된다.



※ 쿠키가 등장했을 때 하드웨어의 성능이 좋지 못하여 웹서버에 많은 데이터를 저장할 수 없었다.

따라서 쿠키라는 데이터 형식으로 서버가 아닌 클라이언트측에 데이터를 브라우저 자신에게 저장하였던 것.



###  3. Cookie의 단점

1. **보안에 매우 취약하다.** 페이지 요청시에 쿠키 값을 그대로 보낸다.
2. **허용 용량이 적다**. 각 쿠키가 저장할 수 있는 데이터는 4Byte를 넘을 수 없다.
3. **웹 브라우저마다 지원 형태가 다르다.**
4. 웹 브라우저 변경시, 저장되어있던 쿠키 값을 다른 브라우저에서 사용할 수 없다.
5. **네트워크의 부하**, 쿠기의 크기가 클 경우 네트워크의 부하는 커진다.
6. 한번에 하나의 정보만 저장할 수 있다.

<br>

### 4. Cookie의 단점을 어떻게 보완할까?

쿠키의 단점을 보완하기 위해 등장한 것이 Session이다.

하드웨어가 발전함에 따라 중요한 데이터는 서버에 저장하는 방식으로 발전하게 된다.



### 5. Session의 등장으로 Cookie를 사용하지 않을까?

![image](https://user-images.githubusercontent.com/72078208/170814796-d70b6c90-2f4d-4ef9-b551-ab19123f82b9.png)

> 이미지 : <a href="https://dololak.tistory.com/535" target="_blank"> 코끼리를 냉장고에 넣는 방법 </a>

오늘날의 **쿠키는 서버에 저장되어있는 자신의 세션을 구분하기 위한 세션키값을 쿠키로 저장하여 사용**한다.

또한, 브라우저의 검색기록등을 쿠키에 저장하여 쿠키에 저장된 데이터를 적절히 사용하기도 한다.

(유튜브 알고리즘, 상품 추천, 광고 등) 





## [2] JWT를 설명하기 전에 Session이란 ??

### 1. Session의 등장 배경

Session은 브라우저에 데이터를 저장하는 방식이 아닌 서버에 데이터를 저장하는 방식이다.



### 2. Session 장점

직접적으로 id와 pw를 주고 받지 않음으로써 정보 유출을 보완한다.



### 3. Session 사용의 예시

> 1. 클라이언트가 id, pw로 서버에 로그인 요청
> 2. 서버에서는 인증 후, 사용자를 식별할 특정 유니크한 세션 ID를 만들어 서버의 세션 저장소에 저장한다.
> 3. 세션 ID를 특정한 형태(쿠키 또는 JSON)으로 클라이언트에게 반환한다.
> 4. 이후 클라이언트가 사용자 인증이 필요한 정보를 요청할 때마다 이 세션 ID를 쿠키에 담아 서버에 함께 전달한다.
> 5. 인증이 필요한 API인 경우, 서버는 세션 ID가 세션 저장소에 있는지 확인한다.
> 6. 있다면 인증 완료 후, API처리 없다면 401에러를 출력한다.

이와 같은 동작으로 인해 HTTP의 statless에 위배되고, 상태를 저장하기 때문에 stateful하게 되어진다.

즉, 데이터가 유지되지 않던 HTTP의 특성이 데이터가 유지되는 상태로 변환되어진다는 것이다.



- stateful의 단점

서버에 데이터가 저장되어야하기 때문에 서버가 증량이 될 경우 데이터 또한 증량되는 서버에 세션 ID를 복사해줘야한다. 이를 해결하기 위해서는 중앙 서버를 따로 두어 문제를 해결한다. (scale out의 문제 해결)



### 4. Session의 단점

1. 세션 저장소에 문제가 생긴다면 인증 체계에 문제가 생기게 되어 인증의 문제가 생길 수 있다.
2. stateful하기 때문에 http의 장점을 발휘하지 못하고 scale out에 걸림돌이 생긴다.
3. 세션ID를 저장해야하는 중앙 서버를 따로 둬야하기 때문에 비용이 든다.
4. 세션ID가 탈취 되었을 때, 대처는 가능하지만 해당 클라이언트인척 위장하는 보안의 약점이 있을 수 있다.
5. 사용자가 많아질 수록 메모리를 많이 차지한다.
6. "매번" 요청 시, 세션 저장소를 조회해야하는 단점이 있다.

<BR>

### 5. Session, 요청시마다 DB에 계속적인 접근이 이루어지는 문제의 해결방법은???

단점 중 하나인, 요청을 진행할 때마다 세션 저장소에 세션 ID를 조회하는 작업을 통해 DB 접근이 계속 되어진다는 점을 보완하기 위해 JWT가 등장한다.





## [2] Session을 이용한 계속적 DB접근의 해결책, JWT

인터넷 표준 인증 방식 

- 인증에 필요한 정보들을 Token에 담아 **암호화**시켜 사용하는 토큰



###  JWT의 기본적 인증 구조 방식은 Cookie와 다를건 없다. 그러나

기본적인 인증 구조 방식은 Cookie와 다를게 없지만, JWT는 **서명된 토큰**이라는 것이 차이점이다.

공개/개인 키를 쌍으로 사용하여 토큰에 서명할 경우, 서명된 토큰은 개인 키를 보유한 서버가 이 서명된 토큰이
정상적인 토큰인지 인증할 수 있다는 이야기이다.



이러한 구조 덕분에 인증 정보를 담아 안전하게 인증을 시도하게끔 전달할 수 있는 것이다.

즉, DB넘나들 필요가 없다는 것이다.



##  [3] JWT의 구조 (디코딩시)

| Header | Payload | Sinature |
| ------ | ------- | -------- |

![image](https://user-images.githubusercontent.com/72078208/170820371-07fa79e9-381f-4e5b-aea8-e92d7d2ee3c3.png)

> 이미지 : <a href="https://inpa.tistory.com/entry/WEB-%F0%9F%93%9A-JWTjson-web-token-%EB%9E%80-%F0%9F%92%AF-%EC%A0%95%EB%A6%AC" target="_blank"> Dev Scroll </a>, [WEB] JWT 쉽게 정리

### 1. Header (알고리즘 형식)

```
{
	"typ" : "JWT",
	"alg" : "HS512"
}
```

우선 헤더는 토큰의 타입과 서명 생성에 어떤 알고리즘이 사용되었는지 저장된다.

> 여기서는 토큰 타입이 JWT이며 서명 생성에는 HS512가 사용된다.



### 2. Payload (사용될 정보에 대한 내용 담는다.)

```
{
	"sub" : "pj",
	"exp" : 1636989718,
	"iss" : "euncheol"
}
```

사용자의 정보를 Claim으로 저장한다. 표준 스펙상 key의 이름은 3글자로 저장한다.

표준 스펙의 키는 아래와 같다.

| 표준 스펙 key명      | 설명            |
| -------------------- | --------------- |
| iss(Issuer)          | 토근 발급자     |
| sub(Subject)         | 토큰 제목       |
| aud(Audience)        | 토큰 대상자     |
| exp(Expiration Time) | 토큰 만료 시간  |
| nbf(Not Before)      | 토큰 활성 날짜  |
| iat(Issued At)       | 토큰 발급 시간  |
| jtu(JWT Id)          | JWT 토큰 식별자 |

위의 표의 해당하는 모든 것들이 반드시 Payload에 포함될 필요가 없으며 상황에 따라 해당 서버가 가져야 할 인증 체계에 따라 사용한다. 물론, 표준 스펙외에 key명을 지정하는 것도 가능하다.



※ payload에는 민감한 정보를 담지 않는다.

- header와 payload는 json이 디코딩되었을 뿐, 특별한 암호가 걸려있는 것이 아니기에
  누구나 jwt를 가지고 디코딩을 한다면 header와 payload에 담긴 값을 알 수 있다.



### 3. Sinature (헤더의 인코딩 값과 정보의 인코딩 값을 합친 후, 비밀키로 해쉬를 하여 생성한다.)

```
Signature = Base64Url(Header) + . + Base64Url(PayLoad) + Server's key
```

`header`와 `payload`는 단순히 인코딩된 값이기 때문에 제 3자가 <u>복호화 및 조작할 수 있지만</u>,

`Signature`는 서버 측에서 관리하는 **비밀키가 유출되지 않는 이상** <u>복호화가 불가능</u>하다.

따라서 `Signature`는 <u>토큰의 위변조 여부를 확인하는데 사용</u>된다.

> 이미지 : <a href="https://inpa.tistory.com/entry/WEB-%F0%9F%93%9A-JWTjson-web-token-%EB%9E%80-%F0%9F%92%AF-%EC%A0%95%EB%A6%AC" target="_blank"> Dev Scroll </a>, [WEB] JWT 쉽게 정리

