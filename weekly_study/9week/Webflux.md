[TOC]



# Webflux



## 1. 블로킹 vs 논블로킹, 동기 vs 비동기

### 기본 개념 잡기

- 제어권
  - 제어권은 자신(함수)의 코드를 실행할 권리 같은 것이다. 제어권을 가진 함수는 자신의 코드를 끝까지 실행한 후, 자신을 호출한 함수에게 돌려준다.
- 결과값을 기다린다는 것
  - A 함수에서 B 함수를 호출했을 때, A 함수가 B 함수의 결과값을 기다리느냐의 여부를 의미한다.



### <span style = "color:red"> 블로킹</span>

> A 함수가 B 함수를 호출하면, **제어권을 A가 호출한 B 함수에 넘겨준다.**

<img src="https://user-images.githubusercontent.com/101400894/189789617-bfbd1309-99d9-426b-b394-db61a47596e0.png" alt="image" style="zoom:30%;" align="left"/>

#### 	동작과정

1. A함수가 B함수를 호출하면 B에게 제어권을 넘긴다.
2. 제어권을 넘겨받은 B는 열심히 함수를 실행한다. A는 B에게 제어권을 넘겨주었기 때문에 함수 실행을 잠시 멈춘다.
3. B함수는 실행이 끝나면 자신을 호출한 A에게 제어권을 돌려준다.



### <span style = "color:red"> 논블로킹</span>

> A함수가 B함수를 호출해도 **제어권은 그대로 자신이 가지고 있는다**.

<img src="https://user-images.githubusercontent.com/101400894/189789791-85325fa1-1d07-4c93-a989-f57dacbbf2f9.png" alt="image" style="zoom:30%;" align="left"/>



#### 	동작과정

1. A함수가 B함수를 호출하면, B 함수는 실행되지만, **제어권은 A 함수가 그대로 가지고 있는다.**
2. A함수는 계속 제어권을 가지고 있기 때문에 B함수를 호출한 이후에도 자신의 코드를 계속 실행한다.





### <span style = "color:red"> 동기(Synchronous)와 비동기(Asynchronous)</span>

> **호출되는 함수의 작업 완료 여부를 신경쓰는지의 여부**의 차이

<img src="https://user-images.githubusercontent.com/101400894/190148431-dfd78c9f-c12c-4355-8548-df64dfb00025.png" alt="image" style="zoom:80%;" align="left"/>

#### 동기

>  함수 A가 함수 B를 호출한 뒤, **함수 B의 리턴값을 계속 확인하면서 신경쓰는 것**이 동기이다.



#### 비동기

> 함수 A가 함수 B를 호출할 때 **콜백 함수를 함께 전달**해서, 함수 B의 작업이 완료되면 함께 보낸 콜백 함수를 실행한다.
>
> 함수 A는 함수 B를 호출한 후로 **함수 B의 작업 완료 여부에는 신경쓰지 않는다.**

+ 동기와 비교했을때의 장점
  +  빠른 속도 - 두 개의 요청을 동시에 보내기 때문에 더 빠른 응답 속도
  +  적은 리소스 사용 - 현재 스레드가 블로킹되지 않고 다른 업무를 처리할 수 있어서 더 적은 수의 스레드로 더 많은 양의 요청을 처리



### 블로킹과 논블로킹, 동기와 비동기 비교

#### Sync-Blocking

>  함수 A는 함수 B의 리턴값을 필요로 한다(**동기**). 그래서 제어권을 함수 B에게 넘겨주고, 함수 B가 실행을 완료하여 리턴값과 제어권을 돌려줄때까지 기다린다(**블로킹**).

<img src="https://user-images.githubusercontent.com/101400894/189790205-dc59477f-ea01-47cf-af8c-931d301770cd.png" alt="image" style="zoom:33%;" align="left"/>



#### Sync-Nonblocking

> 동기를 논블로킹처럼 작동시킬 수 있다.
>
> > A 함수는 B 함수를 호출한다. 이 때 **A 함수는 B 함수에게 제어권을 주지 않고**, 자신의 코드를 계속 실행한다(**논블로킹**).
> >
> > 그런데 **A 함수는 B 함수의 리턴값이 필요하기 때문**에, 중간중간 B 함수에게 함수 실행을 완료했는지 물어본다(**동기**).
> >
> > 즉, 논블로킹인 동시에 동기인 것이다.

<img src="https://user-images.githubusercontent.com/101400894/189790331-2612f11d-345f-4df1-8a0d-2866b539f138.png" alt="image" style="zoom:33%;" align="left"/>



#### Async-Nonblocking

> 비동기 논블로킹은 이해하기 쉽다. A 함수는 B 함수를 호출한다.
>
> 이 때 제어권을 B 함수에 주지 않고, 자신이 계속 가지고 있는다(**논블로킹**). 따라서 B 함수를 호출한 이후에도 멈추지 않고 자신의 코드를 계속 실행한다.
>
> 그리고 B 함수를 호출할 때 **콜백함수**를 함께 준다. B 함수는 자신의 작업이 끝나면 A 함수가 준 콜백 함수를 실행한다(**비동기**).

<img src="https://user-images.githubusercontent.com/101400894/189790548-c5513a17-293e-49ab-8cf8-aef64fb7fe02.png" alt="image" style="zoom:33%;" align="left"/>



#### Async-blocking

> Async-blocking의 경우는 사실 잘 마주하기 쉽지 않다.
>
> A 함수는 B 함수의 리턴값에 신경쓰지 않고, 콜백함수를 보낸다(**비동기**).
>
> 그런데, B 함수의 작업에 관심없음에도 불구하고, A 함수는 B 함수에게 제어권을 넘긴다(**블로킹**).
>
> 따라서, A 함수는 자신과 관련 없는 B 함수의 작업이 끝날 때까지 기다려야 한다.

<img src="https://user-images.githubusercontent.com/101400894/189791060-0f762315-b363-4bca-963a-99797f40c4f0.png" alt="image" style="zoom:33%;" align="left"/> 

✔Async-blocking의 경우 sync-blocking과 성능의 차이가 또이또이하기 때문에 사용하는 경우는 거의 없다.





## 2. 스트리밍 처리

<img src="https://user-images.githubusercontent.com/101400894/190126969-2cce4c27-d342-4130-870b-7aab7a341c79.png" alt="image" style="zoom:80%;" align="left"/>

+ 전통적인 데이터 처리 방식(왼쪽 그림)

  + 데이터 처리 요청이 오면 페이로드(payload)를 모두 애플리케이션의 메모리에 저장한 후에 다음 처리 진행

    ✔페이로드(payload) : 전송되는 데이터

  + 추가로 필요한 데이터도 저장소에서 조회하여 메모리에 적재해야 함

  + 이 방식의 문제점은 전달된 데이터는 물론 저장소에서 조회한 데이터까지 모든 데이터가 애플리케이션의 메모리에 적재되어야만 응답 메시지를 만들 수 있다는 것

  + 만약 필요한 데이터의 크기가 메모리 용량보다 크다면 'out of memory' 에러가 발생하게 됨 

  + 그리고 서비스를 운영하다 보면 꼭 하나의 요청이 'out of memory'를 발생시키지 않더라도, 순간적으로 많은 요청이 몰리면서 다량의 GC(Garbage Collection)가 발생, 서버가 정상적으로 응답하지 못하는 경우가 종종 발생

+ 많은 양의 데이터를 처리하는 애플리케이션에 스트림 처리 방식을 적용(오른쪽)
  + 크기가 작은 시스템 메모리로도 많은 양의 데이터를 처리할 수 있습니다. 
  + 입력 데이터에 대한 파이프 라인을 만들어 데이터가 들어오는 대로 물 흐르듯이 구독(subscribe)하고, 처리한 뒤, 발행(publish)까지 한 번에 연결하여 처리 가능
  + 이렇게 하면 서버는 많은 양의 데이터도 탄력적으로 처리



## 3. 백 프레셔

> Publisher 에서 발행하고, Subscriber에서 구독할 때, Publisher 에서 데이터를 Subscriber 로 Push 하는 방식이 아니라, Pull 방식으로 Subscriber 가 Publisher 로 처리할 수 있는 양의 크기만큼 데이터를 요청 함으로써 Subscriber의 장애를 방지하기 위함이다.



### 푸시 방식

<img src="https://user-images.githubusercontent.com/101400894/190149029-9641950d-5d5e-49c2-9f7c-eecee01b56d7.png" alt="image" style="zoom:50%;" align="left"/><img src="https://user-images.githubusercontent.com/101400894/190149485-9dd49d86-d928-4b74-a91d-38332c26466a.png" alt="image" style="zoom:50%;" />

+ 옵저버 패턴

  + 발행자(publisher)가 구독자(subscriber)에게 밀어 넣는 방식으로 데이터가 전달
  + 발행자는 구독자의 상태를 고려하지 않고 데이터를 전달하는 데에만 충실
  + 만약 발행자가 1초 동안 100개의 메시지를 보내는데 구독자는 1초에 10개밖에 처리하지 못한다면 큐(queue)를 이용해서 대기 중인 이벤트를 저장
  + 서버가 가용할 수 있는 메모리는 한정
  + 만약 초당 100개의 메모리를 계속 푸시한다면 버퍼는 순식간에 소모
  + 버퍼를 다 사용해버려서 오버플로(overflow)가 발생 가능

  

**발생하는 경우를 고정 길이 버퍼와 가변 길이 버퍼로 나누어 살펴보기**

#### 고정 길이 버퍼

> 신규로 수신된 메시지를 거절합니다. 거절된 메시지는 재요청하게 되는데요. 재요청 과정에서 네트워크와 CPU 연산 비용이 추가로 발생합니다.

<img src="https://user-images.githubusercontent.com/101400894/190149861-ac203235-981f-4f60-a0b8-376c81719a4b.png" alt="image" style="zoom:50%;" align="left"/>



#### 가변 길이 버퍼

> 이벤트를 저장할 때 'out of memory' 에러가 발생하면서 서버 크래시(crash)가 발생합니다. '누가 그렇게 구현하겠어?'라고 생각할 수 있지만, Java에서 많이 사용되는 `List`가 가변 길이 자료 구조형입니다. 예를 들어 SQL로 많은 양의 데이터를 질의하면 DBMS는 발행자가 되고 여러분의 서버가 구독자가 되어 `List` 자료 구조형에 데이터를 전부 담으려고 하다가 다량의 GC가 발생하면서 서버가 정상적으로 응답할 수 없는 상태에 이를 수 있습니다.

<img src="https://user-images.githubusercontent.com/101400894/190150163-e719a100-8775-4065-9e1c-39ceded6a8e4.png" alt="image" style="zoom:50%;" align="left"/>



📌**해결 방법**

> **발행자가 데이터를 전달할 때 구독자가 필요한 만큼만 전달하면 해결**
>
> > **이게 바로 백 프레셔의 기본 원리**



### 풀 방식

> 구독자가 10개를 처리할 수 있다면 발행자에게 10개만 요청합니다. 발행자는 요청받은 만큼만 전달하면 되고, 구독자는 더 이상 'out of memory' 에러를 걱정하지 않아도 됩니다.

<img src="https://user-images.githubusercontent.com/101400894/190150558-5d6ca228-3d09-4c72-8561-bdae9ea9372f.png" alt="image" style="zoom:50%;" align="left"/>

+ 여기서 좀 더 탄력적으로 적용하여, 구독자가 이미 8개의 일을 처리하고 있다면 추가로 2개만 더 요청하여 자신이 현재 처리 가능한 범위 내에서만 메시지를 받게 할 수 있음
+ 풀 방식에선 이렇게 전달되는 모든 데이터의 크기를 구독자가 결정
+ 이런 다이나믹 풀 방식의 데이터 요청을 통해서 구독자가 수용할 수 있는 만큼만 데이터를 요청하는 방식이 `백 프레셔`



## 4.reactive streams

Reactive Streams is an initiative to provide a standard for asynchronous stream processing with non-blocking back pressure.

> **논블로킹(Non-blocking) 백프레셔(back pressure)를 이용한 비동기 데이터 처리의 표준이다**



### 표준

> Reactive Streams는 표준화된 API



#### Reactive Streams의 역사

> 2013년에 Netflix와 Pivotal, Lightbend의 엔지니어들이 처음 개발하기 시작했습니다. Netflix는 RxJava, Pivotal은 WebFlux, 그리고 Lightbend는 분산 처리 액터(actor) 모델을 구현한 Akka를 만든 회사인데요. 모두 스트림 API가 꼭 필요한 회사였습니다. 그런데 스트림은 서로 유기적으로 엮여서 흘러야 의미가 있습니다. 데이터가 지속적으로 흐르기 위해서는 서로 다른 회사가 공통의 스펙을 설정하고 구현해야 합니다. 그래서 표준화가 필요했습니다. 
>
> Reactive Streams에선 2015년 4월에 JVM에서 사용하기 위한 [1.0.0](https://www.reactive-streams.org/announce-1.0.0) 스펙을 릴리스했습니다. 그리고 2017년 9월에, Reactive Streams의 API와 스펙, 풀(pull) 방식 사용 원칙을 그대로 포팅해서 [Flow API](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html)라는 이름으로 `java.util.concurrent` 패키지 아래 포함시킨 Java 9이 릴리스되었습니다. 이는 커뮤니티와 일부 기업에서 주도해 개발했던 Reactive Streams가 Java의 공식 기능이 되었다는 것을 의미합니다. 이어서 3달 뒤, Reactive Streams에서 Flow와 상호 변환이 가능한 어댑터를 릴리스하면서, 기존에 만들어진 라이브러리를 사용할 수 있게 되었습니다.

<img src="https://user-images.githubusercontent.com/101400894/190151499-16b95010-7896-4976-8c54-293425876b4b.png" alt="image" style="zoom:80%;" align="left"/>





### Reactive Streams API



```java
public interface Publisher<T> {
    public void subscribe(Subscriber<? super T> s);
}

public interface Subscriber<T> {
    public void onSubscribe(Subscription s);
    public void onNext(T t);
    public void onError(Throwable t);
    public void onComplete();
}

public interface Subscription {
    public void request(long n);
    public void cancel();
}

public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}
```

-  `Publisher`에는 `Subscriber`의 구독을 받기 위한 `subscribe` API 하나만 있습니다.
-  `Subscriber`에는 받은 데이터를 처리하기 위한 `onNext`, 에러를 처리하는 `onError`, 작업 완료 시 사용하는 `onComplete`, 그리고 매개 변수로 `Subscription`을 받는 `onSubscribe` API가 있습니다.
-  `Subscription`은 n개의 데이터를 요청하기 위한 `request`와 구독을 취소하기 위한 `cancel` API가 있습니다.





#### Reactive Streams에서 위 API를 사용하는 흐름

1. `Subscriber`가 `subscribe` 함수를 사용해 `Publisher`에게 구독을 요청합니다.
2. `Publisher`는 `onSubscribe` 함수를 사용해 `Subscriber`에게 `Subscription`을 전달합니다

<img src="https://user-images.githubusercontent.com/101400894/190152761-8645f352-2432-4b0b-acc1-53a7f3f3816f.png" alt="image" style="zoom:70%;" align="left"/>

3. 이제 `Subscription`은 `Subscriber`와 `Publisher` 간 통신의 매개체가 됩니다. `Subscriber`는 `Publisher`에게 직접 데이터 요청을 하지 않습니다. `Subscription`의 `request` 함수를 통해 `Publisher`에게 전달합니다.
4. `Publisher`는 `Subscription`을 통해 `Subscriber`의 `onNext`에 데이터를 전달하고, 작업이 완료되면 `onComplete`, 에러가 발생하면 `onError` 시그널을 전달합니다.
5. `Subscriber`와 `Publisher`, `Subscription`이 서로 유기적으로 연결되어 통신을 주고받으면서 `subscribe`부터 `onComplete`까지 연결되고, 이를 통해 백 프레셔가 완성됩니다.

<img src="https://user-images.githubusercontent.com/101400894/190153094-d2141919-0a43-4819-825c-900e653aa2f1.png" alt="image" style="zoom:70%;" align="left"/>



> 백 프레셔가 좋은 건 알겠는데, 과연 어떻게 사용하는 걸까요? Reactive Streams API는 [GitHub](https://github.com/reactive-streams/reactive-streams-jvm/tree/master/api/src/main/java/org/reactivestreams)에 들어가서 살펴봐도 위에서 살펴본 인터페이스가 전부입니다. 따로 구현체가 없습니다.
>
> 그렇다면 직접 구현해서 사용하면 될까요? 앞서 나온 규칙대로 `Publisher` 인터페이스를 구현하고 이를 구독할 때 `Subscription`을 생성해서 넘겨주도록 구현할 수는 있습니다. 하지만 이게 전부가 아닙니다. Reactive Streams에는 API 외에도 [명세서](https://github.com/reactive-streams/reactive-streams-jvm#specification)가 있는데요. 이 명세서에는 단순한 인터페이스와는 달리 구현 시 따라야 하는 규칙이 복잡하게 명세되어 있습니다.
>
> > 이 명세에 맞춰 직접 구현한 기능은 [Reative Streams TCK](https://github.com/reactive-streams/reactive-streams-jvm/tree/master/tck)라는 툴로 검증할 수 있는데요. 해당 분야의 전문가가 아니라면 모든 규칙을 만족하도록 구현하는 게 꽤나 까다로운 일입니다. 특히 `Publisher`를 구현하는 게 어렵습니다. Java의 유명한 Reactive Streams 구현체 중 하나인 [Project Reactor](https://projectreactor.io/)의 GitHub에 올라온 이슈를 살펴보면, 어떤 사용자가 자신이 만든 커스텀 `Publisher`가 Flux와 잘 연결되지 않는다는 내용으로 이슈를 올려놓은 게 있습니다([참고](https://github.com/reactor/reactor-core/issues/482)). 이에 대한 Project Reactor의 답변은 단호합니다. '만들지 마세요!'라고 답변했습니다. 학습 차원에서 토이 프로젝트로 만드는 것은 저도 추천합니다. 공부도 되고, 좋습니다. 하지만 실제 운영에서 사용할 코드라면 검증되지 않은 코드보다는, `Flux.create()`와 같은 생성자 함수를 활용해서 `Publisher`를 만드는 게 좋다고 생각합니다. Reactive Streams를 사용하려면 직접 구현하는 것보다는 이미 잘 만들어져서 검증까지 받은 구현체를 사용하는 게 좋습니다. 그렇다면 이런 구현체에는 어떤 게 있을까요?





### Reactive Streams 구현체

> Reactive Streams에는 [다양한 구현체](https://en.wikipedia.org/wiki/Reactive_Streams#Adoption)가 존재
>
> 각각의 구현체는 서로 특성이 조금씩 다르기 때문에 상황에 따라, 필요에 맞게 골라서 사용

- 순수하게 스트림 연산 처리만 필요하다면 [RxJava](https://github.com/ReactiveX/RxJava)나 [Reactor Core](https://github.com/reactor/reactor-core), [Akka Streams](https://doc.akka.io/docs/akka/current/stream/index.html) 등을 사용
- 저장소의 데이터를 Reactive Streams로 조회하고 싶다면 [ReactiveMongo](http://reactivemongo.org/)나 [Slick](http://scala-slick.org/) 등을 사용
- 웹 프로그래밍과 연결된 Reactive Streams가 필요하다면 [Armeria](https://line.github.io/armeria/)와 [Vert.x](https://vertx.io/), [Play Framework](https://www.playframework.com/), [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux)를 활용



## 5. WebFlux

> Spring 5에서 새롭게 추가된 모듈입니다.
>
> WebFlux는 클라이언트, 서버에서 reactive 스타일의 어플리케이션 개발을 도와주는 모듈이며, 
>
> reactive-stack web framework이며 non-blocking에 reactive stream을 지원합니다.
>
> > 리액티브 프로그래밍 : 데이터 흐름(data flows)과 변화 전파에 중점을 둔 프로그래밍 패러다임(programming paradigm)이다. 이것은 프로그래밍 언어로 정적 또는 동적인 데이터 흐름을 쉽게 표현할 수 있어야하며, 데이터 흐름을 통해 하부 실행 모델이 자동으로 변화를 전파할 수 있는 것을 의미한다.
> >
> > > 사용자 경험을 향상시키기 위해서 비동기 작업이 필요한데 이 때 리액티브 프로그래밍이 필요

+ 장점 : 고성능, spring 과 완벽한 통합, netty 지원, 비동기 non-blocking 메세지 처리, Back Pressure
  + **netty** : 프로토콜 서버 및 클라이언트와 같은 네트워크 응용 프로그램을 빠르고 쉽게 개발할 수있는 **NIO(Non-Blocking Input Ouput)** **클라이언트 서버 프레임 워크**



### **Spring WebFlux vs Spring MVC**

+ 스프링 MVC

1 request : 1 thread

sync + blocking

 

+ Spring WebFlux

many request : 1 thread

async + nonblock



### DB

> WebFlux로 개발하고 DB는 blocking 이라면 ? WebFlux를 쓸 이유가 없다. 라는 개념아래서 reactive를 지원하는 DB를 사용해야하는데 우리가 일반적으로 알고있는 RDBMS는 지원하지않고 Redis, Mongo 등은 지원한다. (R2DBC를 이용하면 MYSQL 등과 비동기 방식으로 연결이 가능한 듯합니다.)



### **그래서 WebFlux 쓰는 이유**

배민 기술 블로그를 보면 **가게노출 시스템**에 WebFlux를 도입하였는데 내용을 보면

가게노출 시스템은 배달의민족 앱 내에서 가장 많은 트래픽을 소화하는 시스템으로, 다음과 같은 리소스 최적화가 반드시 필요

- 하나의 사용자 요청을 처리하기 위해 수십여개의 외부 시스템에 대한 요청이 필요한 시스템에서, 어떻게 가장 빠른 응답을 줄 수 있을까?
- 수많은 요청을 처리해야할 때 어떻게 쓰레드 지옥을 벗어날 수 있을까?

앞서서도 설명하였듯 Spring MVC는 1:1로 요청을 처리하기 때문에 트래픽이 몰리면 많은 쓰레드가 생겨납니다. 쓰레드가 전환될때 context switching 비용이 발생하게 되는데 쓰레드가 많을 수록이 비용이 커지게 되기 때문에 적절한 쓰레드의 수를 유지해야하는 문제가 있습니다.

 

이에 반해서 WebFlux 는 Event-Driven 과 Asynchronous Non-blocking I/O 을 통해 리소스를 효율적으로 사용할 수 있도록 만들어 줍니다.

<img src="https://user-images.githubusercontent.com/101400894/190156849-d3a8acbd-db45-4d8a-b6cb-418a6e5286cf.png" alt="image" style="zoom:70%;" /><img src="https://user-images.githubusercontent.com/101400894/190156916-af535e56-ac7e-42eb-adf5-778e6d9a83b4.png" alt="image" style="zoom:70%;" /><img src="https://user-images.githubusercontent.com/101400894/190157057-af1515ea-18aa-4972-ba19-5dfc492de176.png" alt="image" style="zoom:70%;" />



### 이렇게 좋은데 왜 MVC를 쓰는 경우가 있나요?

Webflux 가 무조건 빠르고 좋은 것이 아니라 Webflux 프로젝트의 비지니스 로직들이 모두 Async + NonBlocking 으로 되어있다면 빠를 수 있습니다. 그런데 이런 코드를 작성하는 것은 꾀... 어렵죠. ㅎ 아래 주소는 NHN FORWARD 2020에서 WebFlux의 성능이 나오지 않는 부분에 대한 발표내용입니다.

https://www.youtube.com/watch?v=I0zMm6wIbRI 

 

MVC는 못해도 본전이지만, Reative 는 못하면 MVC를 적용하느니만 못하다. 그리고 트래픽이 높은 서비스가 아니라면 MVC로도 감당 가능하다.



### **Mono와 Flux**

> Spring Webflux에서 사용하는 reactive library가 Reactor이고 Reactor가 Reactive Streams의 구현체입니다.
>
> Flux와 Mono는 Reactor 객체이며, 차이점은 발행하는 **데이터 갯수**입니다.

- Flux : 0 ~ N 개의 데이터 전달
- Mono : 0 ~ 1 개의 데이터 전달

보통 여러 스트림을 하나의 결과를 모아줄 때 Mono를, 각각의 Mono를 합쳐서 하나의 여러 개의 값을 여러개의 값을 처리할 떄 Flux를 사용합니다.

📌**그런데 Flux도 0~1개의 데이터 전달이 가능한데, 굳이 한개까지만 데이터를 처리할 수 있는 Mono라는 타입이 필요할까요?**

>  데이터 설계를 할때 결과가 없거나 하나의 결과값만 받는 것이 명백한 경우, List나 배열을 사용하지 않는 것처럼, Multi Result가 아닌 하나의 결과셋만 받게 될 경우에는 불필요하게 Flux를 사용하지 않고 Mono를 사용하게 됩니다. 