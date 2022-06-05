
# Chapter5 YML

# YML

> `YML` 은 ‘**사람이 쉽게 읽을 수 있는**’ 데이터 직렬화 양식이고 원래 Yet Another Markup Language 였는데 핵심은 마크업이 아닌 데이터임을 보여주기 위해 YAML Ain't Markup Language 라는 재귀적요소를 더한 이름으로 바꾸었음.
> 
&nbsp;
## YML의 장점

1. 가독성
2. 자료형과의 결합
3. 주석 사용가능
&nbsp;
## 1. 가독성

> `xml`의 경우엔 태그기반 markup으로 속성을 표시해 주는데 조금만 복잡해도 사람이 읽기가 힘들다. `json` 의 경우도 마찬가지로 어디까지가 Object이고 Array 인지 보기가 힘듬.
그에 반해 `yml`은  계층구조로 표현하여 가독성이 좋으며 .properties 에 비해 불필요한 중복제거도 가능.
> 

![Untitled](https://user-images.githubusercontent.com/45655434/172022566-90366d9c-a9d0-45eb-a248-3c8a0d9214e9.png)

&nbsp;
## 2. 자료형과의 결합

### YML의 기본 자료형

- 스칼라 : number, String
- 배열

```yaml
Youtube:
 channel:
  - penbird
  - kyleschool
  - habithackers
```

- 맵

```yaml
Youtube:
 channel:
  - name : penbird
    type : ["keyboard and hand writings","keyboard and hand writings2"]
  - name : kyleschool
    type : ["data science and engineering", "data science and engineering2"]
  - name : habithackers
    type : ["opct training", "opct training2"]
```
&nbsp;
## 3. 주석사용가능

#기호 하나로 간단하게 주석을 추가할 수 있음

```yaml
Youtube: # 유튜브
 channel: # 채널
  - name : penbird #이름
    type : ["keyboard and hand writings","keyboard and hand writings2"]
  - name : kyleschool
    type : ["data science and engineering", "data science and engineering2"]
  - name : habithackers
    type : ["opct training", "opct training2"]
```
&nbsp;
## Springboot에서의 YML

Springboot 에서 구성속성들을 bean에 필요하면 추가로 매핑도 할 수 있음

```java
@Component
@ConfigurationProperties(prefix = "taco-orders")
@Data
@Validated
public class OrderProps {

    @Range(min=5,max=25,message = "must be in 5~25")
    private int pageSize = 20;

}
```

```yaml
taco:
  orders:
    pageSize: 10
```

Spring-configuration-metadata.json 처럼 json 포맷형식에 매핑되어 있거나

```json
{
	.....
	{
      "name": "spring.profiles.active",
      "type": "java.util.List<java.lang.String>",
      "description": "Comma-separated list of active profiles. Can be overridden by a command line switch.",
      "sourceType": "org.springframework.boot.context.config.ConfigFileApplicationListener"
    },
	{
      "name": "logging.level.values",
      "values": [
        {
          "value": "trace"
        },
        {
          "value": "debug"
        },
        {
          "value": "info"
        },
        {
          "value": "warn"
        },
        {
          "value": "error"
        },
        {
          "value": "fatal"
        },
        {
          "value": "off"
        }
      ],
      "providers": [
        {
          "name": "any"
        }
      ]
    },
  .....
}
```

@ConfigurationProperties 를 이용할 수도 있음 

```java
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties implements BeanClassLoaderAware, InitializingBean {

	private ClassLoader classLoader;

	/**
	 * Whether to generate a random datasource name.
	 */
	private boolean generateUniqueName = true;

	/**
	 * Datasource name to use if "generate-unique-name" is false. Defaults to "testdb"
	 * when using an embedded database, otherwise null.
	 */
	private String name;

	/**
	 * Fully qualified name of the connection pool implementation to use. By default, it
	 * is auto-detected from the classpath.
	 */
	private Class<? extends DataSource> type;

	/**
	 * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
	 */
	private String driverClassName;

	/**
	 * JDBC URL of the database.
	 */
	private String url;

	/**
	 * Login username of the database.
	 */
	private String username;

	/**
	 * Login password of the database.
	 */
	private String password;

.......
```
&nbsp;
## profile 분리

개발환경, 운영환경, 테스트환경 에 맞는 속성들을 줘서 따로 관리를 할 수 있음

application.yml

```yaml
spring:
  datasource:
      url: jdbc:mariadb://localhost:3306/shop?serverTimezone=Asia/Seoul
      username: root
      password: 1234
      driver-class-name: org.mariadb.jdbc.Driver
```

application-prod.yml

```yaml
spring:
  datasource:
      url: jdbc:mariadb://localhost:3306/shop?serverTimezone=Asia/Seoul
      username: root
      password: 1234
      driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none
```

application-test.yml

```yaml
spring:
  datasource:
      url: jdbc:h2:tcp://localhost/D:/db/h2/taco
      username: sa
      password:
      driver-class-name: org.h2.Driver
```
&nbsp;
## properties vs yml

> 분명 불 필요한 반복도 줄고 보기도 쉬운데 yml만 쓰면 좋아보인다. 하지만 단점도 있다.
> 

```java
spring.datasource.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
spring.datasource.url=jdbc:log4jdbc:mariadb://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.hikari.username=root
spring.datasource.hikari.password=qlalfqjsgh
spring.datasource.hikari.maximum-pool-size=10
```

```yaml
spring:
  datasource:
    driverClassName: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
    url: jdbc:log4jdbc:mariadb://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=UTC
    hikari:
      username: root
      password: qlalfqjsgh
      maximum-pool-size: 10
```
&nbsp;
### 프로퍼티 값을 불러 올 수 없음

.properties 에서의 값은 @PropertySource로 불러올 수 있지만 기본적으로 .yml에서는 불가능.

```java
@Configuration 
@PropertySource("classpath:/com/taco/application.properties")
public class AppConfig{
	
    @Autowired 
    Environment env;
    
    @Bean
    public TestBean testBean(){
	    	TestBean testBean = new TestBean();
        testBean.setName(env.getProperty("testbean.name"));
        return testBean;
    }
}
```

```yaml
testbean.name = test
```

그 외 메세지 국제화 기능이나 예외 공통처리 등 .properties에서만 가능한 것들이 있음

참고로, yml ⇒ properties 로 변환하는 방법이 있음. ([공식문서](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#boot-features-external-config))

**참고) 프로퍼티 우선순위**

1. 유저 홈 디렉토리에 있는 spring-boot-dev-tools.properties
2. 테스트에 있는 @TestPropertySource
3. @SpringBootTest 애노테이션의 properties 애트리뷰트
4. 커맨드 라인 아규먼트
5. SPRING_APPLICATION_JSON (환경 변수 또는 시스템 프로티) 에 들어있는 프로퍼티
6. ServletConfig 파라미터
7. ServletContext 파라미터
8. java:comp/env JNDI 애트리뷰트
9. System.getProperties() 자바 시스템 프로퍼티
10. OS 환경 변수
11. RandomValuePropertySource
12. JAR 밖에 있는 특정 프로파일용 application properties
13. JAR 안에 있는 특정 프로파일용 application properties
14. JAR 밖에 있는 application properties
15. JAR 안에 있는 application properties
16. @PropertySource
17. 기본 프로퍼티 (SpringApplication.setDefaultProperties)