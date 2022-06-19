> # 📋 Hypermedia Driven RESTful Web Services - Spring HATEOAS(헤디오스)
### 🏁 목표
- 1) HATEOAS란 
- 2) HATEOAS의 목적
- 3) HATEOAS의 

## 💡 REST maturity model : REST 성숙도 모델
![image](https://user-images.githubusercontent.com/55049159/120597342-e235cf00-c47f-11eb-96e5-54a1d9b34c32.png)
- level 0 ~ 3 | 이전까지 구현 한 것은 level 2 
- level 1 : url 뒷 부분에 연산에 관해 넣어준다. 액션에 대해 별도의 URL 을 넣어준다. 
- level 2 : Resouces +  연산을 하기 위해 HTTP method를 지정  (대부분의 프로젝트가 level2에 기반)
- level 3 : HATEOAS (level 2 + extra links to navigate through API) request는 차이가 없지만, 링크가 들어간다. 
- 완벽한 RESTful API를 구현하기 위해 HATEOAS를 고려해야 한다.

## 💡 What is HATEOAS?
- Hypermedia As The Engine Of Application State -
- One of the constraints of the REST architecture
- Server response in the form of JSON+HAL
- Such a resource consists of two parts: 
1) data
2) links to actions that are possible to be performed on a given resource
  ![image](https://user-images.githubusercontent.com/55049159/120597953-a818fd00-c480-11eb-9254-8d1dedce5e7d.png)
- Data content, Links 로 구분  
- level 2 에서는 Data content 까지만 전달함. 
1) self : 이자체 (자기자신 링크)
2) direct_movies : 링크를 쫓아가면, 이 감독이 만든 링크로 이동 
3) directors : 모든 감독에 대한 List 링크로 이동 
=> 연관된 링크도 함께 담아서 보내주는것이 Hypermedia. <br>
=> 응답으로서 상태정보 뿐 아니라 Hypermedia를 링크에 담아서 보내주는 것이 HATEOAS라고 보면된다. 
 
<hr>

### ✅  HATEOAS 란? (Hypermedia = Data + Links )
- REST API 에서, HATEOAS는 응답으로서 Hypermedia를 보내주는데 HATEOAS는 데이터 정보 뿐 아니라, 연관된 정보도 함께 링크에 넣어서 보내준다. 
- HATEOAS principle client에게 the next potential steps에 대한 관련된 정보를 리턴해줘야 한다. 
- Return 값을 보면 한눈에 파악이 된다.
- 좀더 완벽한 Rest Service를 구현할 수 있도록 도와준다. 

### ✅  HATEOAS 목적? 
- 높은 수준에서 클라이언트, 서버를 분리시키기 위함. 분리시킴으로써 각각 독립적으로 "진화" 할 수 있도록 함.
- 서버와 클라이언트는 계속 개발될 것이기 때문에, 서버를 수정하면 클라이언트도 고치고 해야되는데  url이 서버입장에서 변경이 될 수 있다.
- 서버를 고치면, 클라이언트가 변경되는 경우를 막음. 
- url이 변경되어도, client입장에서 이름을 통해 접근하기 때문에 클라이언트에서 영향이 없다.  
=> Application을 설계 ? => 이런부분을 설계 하는 것. 

### HAL(Hypertext Application Language) Model 
![image](https://user-images.githubusercontent.com/55049159/120600176-6b9ad080-c483-11eb-86fc-5e1a3d67a26e.png)
- Resource : Data Content, Links(Relation + Href) => 이런 식을 표현하는 것이 HAL로 표현했다고 하면된다.  

### HATEOAS의 장점
- Resouces에 대해 Client에 영향을 주지 않으면서 변경할 수 있다. 
- API를 사용할때, 처음에 접근할 때 보기 어려운데 좋은 Doucmentation이 될 수 있다. - 서비스의 기능이 뭔지,, 등등 알 수 있음. 
- 스프링에서는 자동적으로  Documentation을 만들어줌. (Swagger)
- 프론트엔드에서 조건을 만들 때 단순하다. (링크가 있느냐 없느냐애 따라 버튼을 활성화시키는 경우)
- 의존성을 낮추기 위함. 

### HATEOAS의 단점
- 추가적인 네트워크 오버헤드가 있다. 데이터 전송사이즈가 커짐
- 컨트롤러 복잡해짐
