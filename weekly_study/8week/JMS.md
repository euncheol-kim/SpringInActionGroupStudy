# JMS

> 2개 이상의 클라이언트 간에 메시지 통신을 위한 API를 정의하는 자바 표준

+ JmsTemplate이라는 템플릿 기반의 클래스를 통해 JMS를 지원
  + 프로듀서(producer)가 큐와 토픽에 메시지를 전송
  + 컨슈머(consumer)는 그 메시지를 받을 수 있음
  + 브로커는 프로듀서와 컨슈머 사이의 메시지를 정의된 형식으로 메시지를 전달해 주는 중간 다리 역할
  
+ 메시지를 전송하고 수신할 수 있으려면 프로듀서와 컨슈머 간에 메시지를 전달해 주는 메시지 브로커가 먼저 준비되어야 함
+ 메시지 브로커 설정



## JMS 설정하기

- ActiveMQ pom.xml 에 설정

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

- ActiveMQ Artemis pom.xml 에 설정

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

Artemis는 ActiveMQ 를 새롭게 다시 구현한 차세대 브로커



#### Artemis 브로커의 위치와 인증 정보를 구성하는 속성

| 속성                    | 설명                           |
| ----------------------- | ------------------------------ |
| spring.artemis.host     | 브로커 호스트                  |
| spring.artemis.port     | 브로커 포트                    |
| spring.artemis.user     | 브로커 사용자 (선택 속성)      |
| spring.artemis.password | 브로커 사용자 암호 (선택 속성) |

- application.yml 설정

```yaml
spring
    artemis:
        host: localhost
        port: 61616
        user: admin
        password: admin
```

+ 기본적으로 스프링은 Artemis 브로커가 localhost의 61616 포트를 리스닝 하는 것으로 간주(설정변경 가능)



#### ActiveMQ 브로커의 위치와 인증 정보를 구성하는 속성

| 속성                       | 설명                                                |
| -------------------------- | --------------------------------------------------- |
| spring.activemq.broker-url | 브로커 URL                                          |
| spring.activemq.user       | 브로커를 사용하기 위한 사용자(선택속성)             |
| spring.activemq.password   | 브로커를 사용하기 위한 사용자 암호(선택속성)        |
| spring.activemq.in-memory  | 인메모리 브로커로 시작할 것인지의 여부(기본값 True) |



자세한 내용은 아래 문서 참고

- Artemis : https://activemq.apache.org/artemis/docs/latest/using-server.html
- ActiveMQ : http://activemq.apache.org/getting-started.html#GettingStarted-Pre-InstallationRequirements



### JmsTemplate을 사용해서 메시지 전송하기

> JmsTemplate은 스프링 JMS 통합 지원의 핵심

+ JmsTemplate 메서드

```java
// Send raw messages 원시 메시지 전송
void send(MessageCreator messageCreator) 
                throws JmsException;

void send(Destination destination, MessageCreator messageCreator) 
                throws JmsException;

void send(String destinationName, MessageCreator messageCreator) 
                throws JmsException;

// Send messages converted from objects 
// 객체로 부터 변환된 메시지 전송
void convertAndSend(Object message) throws JmsException;

void convertAndSend(Destination destination, Object message) 
                        throws JmsException;

void convertAndSend(String destinationName, Object message)
                         throws JmsException;

// Send messages converted from objects with post-processing
// 객체로부터 변환되고 전송에 앞서 후처리 메시지를 전송
void convertAndSend(Object message, MessagePostProcessor postProcessor) throws JmsException;

void convertAndSend(Destination destination, Object message, MessagePostProcessor postProcessor) 
                        throws JmsException;

void convertAndSend(String destinationName, Object message, MessagePostProcessor postProcessor) 
                        throws JmsException;
```

실제로는 `send()` 와 `convertAndSend()` 만 있으면 됨

- `send()` 메서드는 Message 객체를 생성하기 위해 MessageCreator 가 필요
- `convertAndSend()` 는 Object 타입 객체를 인자로 받아 내부적으로 Message 타입으로 변환
- `convertAndSend()` 는 Object 타입 객체를 Message 타입으로 변환
  그러나 메세지가 전송되기 전에 Message의 커스터마이징을 할 수 있도록 MessagePostProcessor도 인자로 받음



#### 1. Send()를 사용해서 주문 데이터를 Message 전송

1. 기본 도착지를 application.yml 에 따로 지정

```
//도착지 지정
spring:
  jms:
    template:
      default-destination: tacocloud.order.queue
```

```less
@Service
public class JmsOrderMessagingService implements OrderMessagingService {

    private JmsTemplate jmsTemplate;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    @Override
    public void sendOrder(Order order) {
        // Order로 부터 새로운 메시지 생성
        jmsTemplate.send(
            session -> session.createObjectMessage(order));
    }

}
```

2. 기본 도착지를 `send()` 의 파라미터로 전달할 때

```java
@Service
public class JmsOrderMessagingService implements OrderMessagingService {

    private JmsTemplate jmsTemplate;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendOrder(Order order) {
        // Order로 부터 새로운 메시지 생성
        jmsTemplate.send(
                "tacocloud.order.queue",
                session -> session.createObjectMessage(order));
    }

}
```

+ `send()` 메서드 사용하면 Message 객체를 생성하는 MessageCreator 를 두번째 인자로 전달 하므로 복잡
  (이 때 `convertAndSend()` 를 사용 가능)



---------

#### 2. 메시지 변환하고 전송하기

>  JmsTemplates의 convertAndSend() 메서드는 전송될 객체를 인자로 직접 전달
>
> 즉 해당 객체가 Message 객체로 변환되어 전송

```typescript
@Override
public void sendOrder(Order order) {
    //주문 객체를 지정된 도착지로 전송
    //전송될 객체를(주문객체)  Message 객체로 변환되어 전송
    jmsTemplate.convertAndSend("tacocloud.order.queue", order);
}
```

+ 객체를 Message 객체로 변환하는 번거로은 일은 MessageConverter 를 구현하여 처리



##### MessageConverter 구현하기

+ MessageConverter 는 스프링에 정의된 인터페이스 이며 두개만 정의

```java
public interface MessageConverter { 
    Message toMessage(Object object, Session session) 
                throws JMSException, MessageConversionException;

    Object fromMessage(Message message)
}
```

+ Spring message converters 종류

| 메시지변환기                    | 하는일                                                       |
| ------------------------------- | ------------------------------------------------------------ |
| MappingJackson2MessageConverter | Jackson 2 JSON 라이브러리를 사용해서 메시지를 JSON으로 상호변환 |
| MarshallingMessageConverter     | JAXB를 사용해서 메시지를 XML로 상호변환                      |
| MessagingMessageConverter       | 수신된 메시지의 MessageConverter를 사용해서 해당 메시지를 Message 객체로 상호변환<br />또는 JMS 헤더와 연관된 JmsHeaderMapper를 표준 메시지 헤더로 상호 변환 |
| SimpleMessageConverter          | 문자열을 TextMessage로, byte 배열을 ByteMessage로, Map을 MapMessage로, Serializable 객체를 ObjectMessage로 상호변환 |

+ 기본적으로는 SimpleMessageConverter가 사용되며 이 경우 전송될 객체가 Serializable 인터페이스를 구현

+ 다른 message converter를 적용할 때는 해당 변환기의 인스턴스를 빈으로 선언만 하면 됨

+ 아래 처럼 message converter의 setTypeIdMappings()를 호출하여 Order 클래스를 order 라는 타입 id로 매핑하도록 할 수 있음
  _typeId 속성에 전송되는 클래스 이름 대신 order 값이 전송된다.

```java
@Configuration
public class MessagingConfig {

  @Bean
  public MappingJackson2MessageConverter messageConverter() {
    MappingJackson2MessageConverter messageConverter =
                            new MappingJackson2MessageConverter();
    messageConverter.setTypeIdPropertyName("_typeId");

    Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
    typeIdMappings.put("order", Order.class);
    messageConverter.setTypeIdMappings(typeIdMappings);

    return messageConverter;
  }
}
```



---

#### 3. 후처리 메시지

> 후처리로 메시지가 전송되기 전에 커스텀 헤더에 메시지를 추가

```java
@Override
public void sendOrder(Order order) {
    jmsTemplate.convertAndSend("tacocloud.order.queue", order,
            this::addOrderSource);
}

private Message addOrderSource(Message message) throws JMSException {
    message.setStringProperty("X_ORDER_SOURCE", "WEB");
    return message;
}
```

----

## JMS 메시지 수신하기

메시지를 수신하는 방식에는 두 가지

- **Pull model** : 우리 코드에서 메시지를 요청하고 도착할 때 까지 기다림
- **Push model** : 메시지가 수신 가능 하게 되면 우리 코드로 자동 전달



### 1. JmsTemplate (Pull model) 로 메시지 수신하기

> `JmsTemplate` 을 사용해서 메시지 수신하기

```java
// 원시 메시지 수신
Message receive() throws JmsException;
Message receive(Destination destination) throws JmsException;
Message receive(String destinationName) throws JmsException;

// Message 를 도메인 타입으로 변환하여 수신
Object receiveAndConvert() throws JmsException;
Object receiveAndConvert(Destination destination) throws JmsException;
Object receiveAndConvert(String destinationName) throws JmsException;

---------------------------------------------------------------------------------
//큐에서 주문 데이터 가져오기
@Service
public class JmsOrderReceiver implements OrderReceiver {

  private JmsTemplate jms;

  public JmsOrderReceiver(JmsTemplate jms) {
    this.jms = jms;
  }

  @Override
  public Order receiveOrder() {
    return (Order) jms.receiveAndConvert("tacocloud.order.queue");
  }

}
```



### 2. 메시지 리스너 (Push model) 수신하기

> `receive()` 나 `receiveAndConvert()` 를 호출해야 하는 pull 모델과 달리
> 메시지 리스너는 메시지가 도착할 때 까지 대기하는 수동적 컴포넌트

+ `@JmsListener` 를 지정하여 JMS 메시지를 수동적으로 리스닝하는 메시지 리스너를 생성

```java
@Component
public class OrderListener {

  private KitchenUI ui;

  @Autowired
  public OrderListener(KitchenUI ui) {
    this.ui = ui;
  }

  @JmsListener(destination = "tacocloud.order.queue")
  public void receiveOrder(Order order) {
    ui.displayOrder(order);
  } 
}
```

> `@JmsListener` 는 "tacocloud.order.queue" 도착지의 메시지를 리스닝할 수 있도록 한다. 이 메서드는 `JmsTemplate` 를 사용하지 않으며 애플리케이션 코드에서도 호출하지 않는다. 대신에 스프링 프레임워크 코드가 특정 도착지에 메시지가 도착하는 것을 기다리다가 도착하면 해당 메시지에 적재된 Order 객체가 인자로 전달 되면서 receiveOrder() 메서드가 자동으로 호출된다.

+ 여러면에서 `@JmsListener` 어노테이션은 `@RequestMapping` 과 유사
+ 단 `@JmsListener` 가 지정된 메서드들은 지정된 도착지에 들어오는 메시지에 반응

--------

+ 일반적으로는 스레드 실행을 막지 않으므로 Push 모델이 좋은 선택

+ 메시지 리스너는 중단 없이 다수의 메시지를 빠르게 처리할 수 있어 좋은 선택이 될 때가 있음

+ 단 많은 메시지가 한번에 도착하여 리스너에 과부하가 걸리는 경우가 생길 수 있음(즉 메시지를 수신 후 빠르게 처리할 수 없다면 심각한 병목현상이 생길 수 있음)

- 용도에 따른 선택
  - 메시지 리스너는 메시지가 빠르게 처리될 수 있을 때 사용하면 매우 적합
  - JmsTemplate 은 메시지 처리기가 자신의 시간에 맞춰 더 많은 메시지를 요청할 수 있어야 한다면 JmsTemplate 이 제공하는 pull 모델이 적합

-----

+ JMS 는 표준 자바 명세에 정의되어 있고 여러 브로커에 지원되므로 자바 메시징에 많이 사용

+ 그러나 몇가지 단점이 있고 특히 java 애플리케이션에만 사용 가능

+ 따라서 RabbitMQ, 카프카 같이 다른 언어와 JVM 이외의 다른 플랫폼에서 사용 가능한 메시징 시스템이 등장
