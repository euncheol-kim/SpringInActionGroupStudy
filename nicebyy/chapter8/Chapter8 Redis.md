# Chapter8 Redis

![Untitled](https://user-images.githubusercontent.com/45655434/188285042-f917aefd-792f-407c-9f8e-5490d7ad321e.png)

# Redis

> **Remote Dictionary Server** 의 약자로 In-Memory 기반 Key-Value 형식의 Cache Store 이다.
> 

### Cach

> 자주 사용하는 데이터나 값을 미리 복사해 놓는 임시 저장소이며 read-write 가 빈번하게 일어날 때 사용하며 저장공간이 적다는 것이 특징
> 

## MemCache vs Redis

> 세션은 Memory 기반으로 동작해서 데이터를 저장하는 저장소인데 굳이 Redis를 사용해야 하는이유와 Spring 에서 공식적으로 Session 클러스터링을 Redis 방식을 선택하는 이유가 있다.
> 

두 가지 공통점과 차이점을 비교하면 다음과 같다.

![Untitled 1](https://user-images.githubusercontent.com/45655434/188285055-b19a3b70-b366-4086-9a5a-95f660840406.png)

**가장 큰 특징**은 

1. **다양한 자료구조**를 지원
2. **데이터 저장할 때 Disk에도 저장이 가능**할 수 있게 해 주기 때문에 복구에 용이 
3. Memory Cache는 LRU 방식으로 동작하지만 **Redis는 다양한 정책을 선택**이 가능

### Redis 적용 예시

> Redis 를 사용하는 기준은 다르겠지만, 단순 캐싱용으로 사용하는 용도를 넘어서는 경우에 사용한다. 
>예를들어 서버가 여러개일경우 사용자 로그인 정보를 서버끼리 서로 공유할 수 있는 형태로 있어야 하는데 이를 세션 클러스터링이라고 한다.
> 

![Untitled 2](https://user-images.githubusercontent.com/45655434/188285063-f6f02174-4329-4bef-87ae-99251845871a.png)

> 만약 Server1에서 뭔가 요청을 처리하고 있고 , Server2 와 Server3도 다 같은 세션정보를 알고 있어야 한다고 가정을 하자. (ex. 로그인 된 사용자 정보)
>그럼 Server1는 Server2와 Server3에게 정보를 보내주면 Memory내에 사용자 정보를 읽어들여 세션을 유지해야하는데 이는 서버에게 부담을 초래 하게 된다.
> 

### Redis Session 클러스터링

![Untitled 3](https://user-images.githubusercontent.com/45655434/188285068-c61a22bf-f76b-4dfd-83a4-4380de3cc95d.png)

> 따라서 다음과 같이 세션 전용 서버를 따로두면 Server2와 Server3은 별도의 세션 서버로부터 정보를 읽어 들일 수 있기 때문에 로그인이 유지된 채 사용자는 여전히 서비스를 이용가능하다.
>
>혹은 만약 어떤 쇼핑몰 시스템이 있고 도메인서버가 분리되어있을 경우 같은 사용자 정보를 이용해야 할 때 세션 클러스터링을 통해 사용자정보를 받아올 수 있다.
> 

아래 Redis UI 예시를 보면 현재 로그인 된 사용자 세션을 확인할 수 있다.

![Untitled 4](https://user-images.githubusercontent.com/45655434/188285071-6897cdb7-92ed-4402-9051-b76ac1b65c88.png)

# Spring 에서 Redis 이용해보기

### 공식문서

Redis Download : [https://redis.io/download/](https://redis.io/download/)

Redis UI : [https://www.electronjs.org/apps/p3x-redis-ui](https://www.electronjs.org/apps/p3x-redis-ui)

Spring Redis Doc : [https://spring.io/projects/spring-data-redis/](https://spring.io/projects/spring-data-redis/)

본 예제에서는 Jedis를 사용한다.

의존성추가 (build.gradle)

```jsx
"org.springframework.boot:spring-boot-starter-data-redis"
```

Redis 서버 설정 (appliction.properties)

```jsx
### Redis
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=1234
spring.redis.pool.max-idle=10
spring.redis.pool.min-idle=5
spring.redis.pool.max-active=10
spring.redis.pool.max-wait=-10
```

```java
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jcf = new JedisConnectionFactory();
        jcf.setHostName(host);
        jcf.setPort(port);
        jcf.setPassword(password);
        return jcf;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
```

StringReedisSerializer를 넘겨줘서 Sring 타입의 Key Value 템플릿을 사용한다.

이렇게 하고 Redis UI를 통해서 보면 SpringSecurity에서의 세션관리가 Redis를 통해 되고있다는 것을 확인할 수 있다.

![Untitled 5](https://user-images.githubusercontent.com/45655434/188285076-74ea9e9f-1ef6-4084-9af0-7eb2cf258442.png)