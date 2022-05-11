# Chapter1.1 스프링 시작하기



# 스프링 구조

**어플리케이션**은 기본적으로 `Components` 간의 상호작용으로 동작하는데 
`Component` 또는 `Bean` 형태로 스프링 컨테이너인 `ApplicationContext` 라는 DI 컨테이너에 올려두고 관리 되는 구조.

---
&nbsp;&nbsp;

### ApplicationContext 와 Bean 등록
&nbsp;
> 빈을 등록하는 방법은 위와 같이 `annotation` 기반으로 등록하거나 `xml` 파일로 등록할 수 있다.
위와 같이 스프링 컨테이너에 빈을 등록하면 실제 어플리케이션이 동작할 때 
의존성 주입 (DI) 을 기반으로 해서 스프링이 관리하는 빈으로 변경되어 수행된다.
> 

```java
@Configuration // Coonfiguration 또한 Component 이다.
public class AppConfig {

	@Bean
	public UserService userService() {
		return new UserService(memberRepository());
	}

	@Bean
	public UserRepository userRepository() {
		return new UserRepository();
	}

}
```

| 빈 이름 | 빈 객체 |
| --- | --- |
| userService | UserService@x01 |
| userRepository | UserRepository@x02 |

&nbsp;
### 등록한 bean 출력해보기

```java
ApplicationContext ac = new
AnnotationConfigApplicationContext(AppConfig.class);

AppConfig bean = ac.getBean(AppConfig.class);
System.out.println("bean = " + bean.getClass());
//출력: bean = AppConfig$$EnhancerBySpringCGLIB$$bd479d70
```

> 실제로 `annotation` 으로 등록한 `bean` 을 출력해보면 `EnhancerBySpringCGLIB` 라는 것이 출력이 된다. 즉, 스프링이 관리하는 bean 형태로 변환이 되어서 DI 컨테이너에 관리되고 있다는 것.

`@Autowired` 를 필드, 메서드, 생성자에 붙여줌으로 써 필요한 `Component` 를 주입받을 수 있다.
> 



---
&nbsp;
### 빈을 등록하는 예시

- 설정 정보  : `@Configuration`
- 빈 : `@Bean`
- 컴포넌트 : `@Component` ( `@Service` `@Repository` `@Controller` 등 )

위와 같이 보통 `bean`을 등록할 때 적절한 `annotation`을 붙여서 등록을 한다.
`@Bean` 형태로 등록하는 것은 `수동 bean` 을 등록하는 방법이고 
그 외는 `자동 bean`을 등록하는 방법이다.

### 수동 bean 을 등록하는 기준

> 어플리케이션은 크게 **업무 로직**과 **기술 지원 로직**으로 나눌 수 있다.
> 
>**업무로직**은 `Service` `Repository` `Controller` 등 비즈니스 로직을 말한다. 만약 문제가 발생하면 어디서 발생했는지 명확히 파악하기가 쉽다.
>
>**기술지원 로직**은 기술적인 문제나 `AOP`를 처리할 때 주로 사용된다. (log, DBconnetion 등 )
횡단 관심사 처리를 목적으로 어플리케이션 전반적으로 영향을 끼치기 때문에 만약 문제가 발생하면 어디서 발생했는지 명확하게 드러나지 않는다.
>
>따라서 이러한 기술지원 로직들은 수동빈 등록으로 설정정보에 바로 나타나게 하는 것이 유지보수에 유리하다.
>
