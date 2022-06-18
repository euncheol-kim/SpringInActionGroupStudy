



# 1 ] 스프링 시큐리티<sup>Spring Security</sup>

> 스프링시큐리티를 처음하면 읽기 좋은 글 
>
> - <a href = "https://postitforhooney.tistory.com/entry/SpringSecurity-%EC%B4%88%EB%B3%B4%EC%9E%90%EA%B0%80-%EC%9D%B4%ED%95%B4%ED%95%98%EB%8A%94-Spring-Security-%ED%8D%BC%EC%98%B4"> [Spring/Security] 초보자가 이해하는 Spring Security - 퍼옴</a>, shun10114 -> 전반적인 설명이 모두 들어감

<br>

<br>

<br>

# * SpringSecurity를 하기전의 선행 [교재외]

## [1] 인증과 권한

| 개념                         | 설명                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| 인증<sub>Athentication</sub> | 애플리케이션 작업을 수행할 수 있는 **<u>주체(사용자)라고 주장</u>**하는 것 |
| 권한<sub>Authorization</sub> | 애플리케이션 **<u>작업을 수행해도 되는지 결정</u>**하는 것   |

> 권한 승인이 필요한 부분으로 접근한다면, 동작(작업)을 수행할 수 있도록 허락되있는지 결정하는 것을 말한다.
>
> 
>
> - 스프링 시큐리티를 이용하면, 간단히 자신만의 인증 매커니즘을 만들 수 있다.
> - 스프링 시큐리티는 상당히 깊은 권한 부여를 제공한다. 
> - <u><sup> 1 </sup>권한 부여는 두 가지가 영역이 있다.</u>

<br>

<br>

## [2] 권한 부여의 두 가지 영역

| 권한 부여 영역                                       |
| ---------------------------------------------------- |
| 웹 요청 권한                                         |
| 메소드 호출 및 도메인 인스턴스에 대한 접근 권한 부여 |





# 1. 스프링 시큐리티<sup>Spring Security</sup> 활성화하기

### <1> 종속성 설정

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.6.8</version>
</dependency>
```

## 종속성 설정으로 얻을 수 있는 구성

>1. 모든 HTTP 요청 경로는 인증<sub>authentication</sub>되어야 한다.
>2. 어떤 특정 역할이나 권한이 없다
>3. 로그인 페이지가 따로 없다
>4. 사용자는 하나만 있으며, 이름은 user다. 비밀번호는 암호화해 준다.

<br>

<br>

# 2. 실습 프로그램의 요구조건

> 1. 스프링 시큐리티의 HTTP 인증 대화상자 대신, 우리의 로그인 페이지로 인증한다.
> 2. 다수의 사용자를 제공하며, 새로운 타코 클라우드 고객이 사용자로 등록할 수 있는 페이지가 있어야 한다.
> 3. 서로 다른 HTTP 요청 경로마다 서로 다른 보안 규칙을 적용한다.
>    예를 들어, 홈페이지와 사용자 등록 페이지는 인증이 필요하지 않다.

앞선 요구조건을 만족 시키기 위해서는 스프링 자동-구성이 하는 것을 대체하기 위한 작업을 해야한다.

<br>

<br>

<br>

# 3. 한 명 이상의 사용자를 가질 수 있도록 사용자 스토어<SUB>store</sub> 구성하기 - chap01 : 인메모리 방식

## <1> 인메모리 스토어 방식의 장점

> [장점]
>
> - 간단한 테스트 목적에 유리하다.
>
> 
> [단점]
>
> - 사용자 정보의 추가나 변경이 쉽지 않다.
> - 즉, 사용자의 추가, 삭제, 변경을 해야 한다면 보안 구성 코드를 변경한 후, 
>   애플리케이션을 다시 빌드하고 배포, 설치해야한다.

<br>

<br>

## <2> 실습조건에서 본 인메모리 방식의 구성

> 실습 코드는, 고객 스스로 사용자로 등록하고 자신의 정보를 변경할 수 있어야 한다.
> 따라서 위와 같은 단점으로 인메모리 방식은 적합하지 않다.

<br>

<br>

## <3> 인메모리 스토어 구성하기

- **src/main/java/tacos/security/SecurityConfig**

```java
package tacos.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/design", "/orders")
                        .access("hasRole('ROLE_USER')")
                    .antMatchers("/", "/**").access("permitAll")
                .and()
                    .httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1")
                .password("{noop}password1")
                .authorities("ROLE_USER")
                .and()
                .withUser("user2")
                .password("{noop}password2")
                .authorities("ROLE_USER");
    }

}

```

### class WebSecurityConfigurerAdapter

> API Documentation : <a href = "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html"> class WebSecurityConfigurerAdapter : spring-security-docs 5.7.1 API</a>
>
> <현재 쓰이지 >
>
> 
>
> 스프링 시큐리티의 웹 보안 기능 초기화 및 설정
>
> 즉, 접근 보안 수준을 설정하기 위해 사용한다.



### @Override configurer(<sup> 1 </sup>HttpSecurity http)

> HTTP 선택적 보안 수준 설정



### <sup> 1 </sup>class HttpSecurity

> API Documentation : <a href = "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html"> class HttpSecurity : spring-security-docs 5.7.1 API</a>
> 
>
> 인증, 인가의 세부적인 기능을 설정할 수 있도록 API를 제공해주는 클래스이다.
>
> - <sup> 2 </sup>HttpSecurity의 사용 메소드
>
> ※ <sup> 3</sup> sup <u>class WebSecurity</u>와 <u>class HttpSecurity</u> 차이점



### <sup> 3</sup> sup <u>class WebSecurity</u>와 <u>class HttpSecurity</u> 차이점

> 참고자료 : <a href ="https://gngsn.tistory.com/155"> Spring Security, 어렵지 않게 설정하기</a>





### <sup>2 </sup>class HttpSecurity의 메소드

| class HttpSecurity의 메소드명   | 설명                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| authorizeRequests()             | 시큐리티 처리에 **HttpServletRequest를 이용한다는 것**이다.<br />- HttpServletRequest와 관련한 참고자료<br />- <a href = "https://chobopark.tistory.com/43" target = "_blank">HttpServletRequest 개념 및 사용법</a>,  Beginner Developer Playground |
| antMatchers("path1", "path2"..) | 특정한 경로를 지정                                           |
| access()                        | API DOC : <a href = "https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/web/configurers/ExpressionUrlAuthorizationConfigurer.AuthorizedUrl.html" target ="_blank">Class ExpressionUrlAuthorizationConfigurer.AuthorizedUrl</a><br /><br />주어진 <u>SpEL표현식</u>의 평가 결과가 true이면 접근 이용한다.<br />- SpEl(Spring Expression Language) : 스프링 표현 언어<br />- SpEl과 관련한 참고자료<br />- <a href = "https://devwithpug.github.io/spring/spring-spel/"> Spring Expression Language(SpEL) 에 대해 </a>, 최준규 |
| httpBasic()                     | 사용자 인증방법으로 HTTP Basic Authentication을 이용한다.    |



### @Override configurer(<sup> 4</sup>AuthenticationManagerBuilder auth)

> 스프링 시큐리티의 인증에 대한 지원을 설정하는 몇 가지 메소드를 가진다.



### <sup>4 </sup>class AuthenticationManagerBuilder의 사용과 메소드

> API DOC : <a href = "https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/authentication/builders/AuthenticationManagerBuilder.html"> class AuthenticationManagerBuilder</a>, Spring Security 4.0.4.RELEASE API
> 
>
> - 유저 인증 정보를 설정 할 수 있다.
> - 인증 명세를 구성하기 위해 빌더 형태의 API를 사용한다.

| AuthenticationManagerBuilder의 메소드 | 설명                                                         |
| ------------------------------------- | ------------------------------------------------------------ |
| inMemoryAuthentication()              | 메모리 내 인증을 추가하고,<br />메모리 내 인증을 사용자 정의할 수 있도록 한다.<br />- 인메모리 사용자 스토어 구성 |
| withUser("username")                  |                                                              |
| password("password")                  | 스프링5 이후, 반드시 암호화가 필요하다.<br />암호화하지 않는 경우, 접근이 불가하다.<br />- 위의 코드에서는 {noop}를 지정하여 암호화하지 않았다. |
| authorities("granted authority")      | granted authority에 해당하는 궈한을 부여한다.<br />- 현재 코드에서는 ROLE_USER의 권한을 가진다.<br />- 해당 부분을 지우고 roles("USER")로 사용해도 된다. |





# 3. 한 명 이상의 사용자를 가질 수 있도록 사용자 스토어<SUB>store</sub> 구성하기 - chap01 : JDBC기반 방식



> 인메모리 방식에서 진행한, configurer(AuthenticationManagerBuiler)를 지우고 아래와 같이 수정한다.

- **src/main/java/tacos/security/SecurityConfig**

```java
package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/design", "/orders")
                        .access("hasRole('ROLE_USER')")
                    .antMatchers("/", "/**").access("permitAll")
                .and()
                    .httpBasic();
    }

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .jdbcAuthentication()
                    .dataSource(dataSource);
        }

}

```

## DataSource

> DB와 관계된 커넥션 정보를 담고 있으며, 빈으로 등록하여 인자로 넘긴다.
>
> - 이 과정을 통해 Spring은 DataSource로 DB와의 연결을 획득한다.
>
> - DB 서버와의 연결을 해준다.
>
> - DB Connetion pooling 기능 (DBCP)
>
>   
>
> **※ DBCP?**
>
> - 웹 컨테이너 (WAS : Web Application Server)가 실행되면서 DB와 미리 connection(연결)을 해놓은 객체들을 pool에 저장해두었다가, 클라이언트 요청이 오면 connection을 빌려주고, 처리가 끝나면 다시 connection을 반답받아 pool에 저장하는 방식을 말한다.
> - 속도와 퍼포먼스가 좋아진다.
> - 커넥션풀을 관리하고, 커넥션 객체를 풀에서 꺼냈다 반납하는 이러한 과정을 위에서의 DataSource가 한다.



##  AuthticationManagerBuilder.jdbcAuthentication()

> API DOC : <a href = "https://docs.spring.io/spring-security/site/docs/5.3.2.RELEASE/api/index.html?org/springframework/security/config/annotation/authentication/builders/AuthenticationManagerBuilder.html" target="_blank">Class AuthenticationManagerBuilder</a>
>
> 
>
> jdbc 인증을 추가하고, jdbc 인증을 사용자 정의 할 수 있도록 한다.

##  AuthticationManagerBuilder.dataSource()

> API DOC : <a href = "https://docs.spring.io/spring-security/site/docs/5.3.2.RELEASE/api/index.html?org/springframework/security/config/annotation/authentication/builders/AuthenticationManagerBuilder.html" target="_blank"> Class JdbcUserDetailsManagerConfigurer </a>
>
> 
>
> 사용될 데이터 소스를 가져온다.



## 1. 채워야하는 데이터 소스관련 파일 (설명 X)

- **src/main/resources/data.sql**

```sql
insert into users (username, password) values ('user1', 'password1');
insert into users (username, password) values ('user2', 'password2');

insert into authorities (username, authority)
    values ('user1', 'ROLE_USER');
insert into authorities (username, authority)
    values ('user2', 'ROLE_USER');

commit;
```



- **src/main/resources/schema.sql**

```sql
drop table if exists users;
drop table if exists authorities;
drop index if exists ix_auth_username;

create table if not exists users(
    username varchar2(50) not null primary key,
	password varchar2(50) not null,
	enabled char(1) default '1');

create table if not exists authorities (
    username varchar2(50) not null,
	authority varchar2(50) not null,
	constraint fk_authorities_users
	   foreign key(username) references users(username));

create unique index ix_auth_username 
    on authorities (username, authority);

```





## 2. 현재 상태로 design에 진입하면 에러 -> 암호화되지 않음

> 