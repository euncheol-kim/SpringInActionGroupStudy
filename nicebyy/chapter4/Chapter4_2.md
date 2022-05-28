# Chapter4.2 Spring Secutiry 구성하기

### WebSecurityConfigurerAdapter

> SpringSecutiry 의존성을 추가하게 되면 기본적으로 `WebSecurityConfigurerAdapter` 클래스가 실행.
> `WebSecurityConfigurerAdapter` 는 SpringSecutiry 의 웹(http) 보안 기능 초기화 및 설정들을 당담하는 내용이 담겨있고 인증/인가 관련 설정을 제공.
> 

![Untitled](https://user-images.githubusercontent.com/45655434/170840377-7d349cc9-87bc-4a48-abef-b225db0c18e3.png)
&nbsp;
configure() 메서드를 Override 하여 설정정보를 구성할 수 있다.

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll")
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .csrf();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

}
```
&nbsp;
### 참고 (구성 메소드)

| 메소드 | 하는 일 |
| --- | --- |
| access(String) | 인자로 전달된 SpEL 표현식이 true면 접근을 허용한다. |
| anonymous() | 익명의 사용자에게 접근을 허용한다. |
| authenticated() | 익명이 아닌 사용자로 인증된 경우 접근을 허용한다. |
| denyAll() | 무조건 접근을 거부한다. |
| fullyAuthenticated() | 익명이 아니거나 또는 remember-me가 아닌 사용자로 인증되면 접근을 허용한다. |
| hasAnyAuthority(String...) | 지정된 권한 중 어떤 것이라도 사용자가 갖고 있으면 접근을 허용한다. |
| hasAnyRole(String...) | 지정된 역할 중 어느 하나라도 사용자가 갖고 있으면 접근을 허용한다. |
| hasAuthority(String) | 지정된 권한을 사용자가 갖고 있으면 접근을 허용한다. |
| hasIpAddress(String) | 지정된 IP 주소로부터 요청이 오면 접근을 하용한다. |
| hasRole(String) | 지정된 역할을 사용자가 갖고 있으면 접근을 허용한다. |
| not() | 다른 접근 메소드들의 효력을 무효화한다. |
| permitAll() | 무조건 접근을 허용한다. |
| rememberMe() | remember-me(이전 로그인 정보를 쿠키나 데이터베이스로 저장한 후 일정 기간 내에 다시 접근 시 저장된 정보로 자동 로그인됨)를 통해 인증된 사용자의 접근을 허용한다. |

| authentication | 해당 사용자의 인증 객체 |
| --- | --- |
| denyAll | 항상 false를 산출한다. |
| hasAnyRole(역할 내역) | 지정된 역할 중 어느 하나라도 해당 사용자가 갖고 있으면 true |
| hasRole(역할) | 지정된 역할을 해당 사용자가 갖고 있으면 true |
| hasIpAddress(IP 주소) | 지정된 IP 주소로부터 해당 요청이 온 것이면 true |
| isAnonymous() | 해당 사용자가 익명 사용자이면 true |
| isAuthenticated() | 해당 사용자가 익명이 아닌 사용자로 인증되었으면 true |
| isFullyAuthenticated() | 해당 사용자가 익명이 아니거나 또는 remember-me가 아닌 사용자로 인증 되었으면 true |
| isRememberMe() | 해당 사용자가 remember-me 기능으로 인증되었으면 true |
| permitAll | 항상 true를 산출한다. |
| principal | 해당 사용자의 principal 객체 |

&nbsp;

# 사용자 스토어 구성

- In-Memory 기반
- JDBC 기반
- LDAP 기반
- 커스텀 사용자 명세 서비스

## In-Memory 기반

> 사용자 정보를 유지/관리 할 수 있는 곳 중 하나가 메모리
변경이 필요없는 사용자를 정해놓는 용도로 사용 ⇒ 테스트 목적
> 

```java
@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}1234")
                .authorities("ADMIN")
                .and()
                .withUser("testuser")
                .password("{noop}1234")
                .authorities("USER");
    }
```

## JDBC 기반

> 사용자 정보를 RDBMS로 유지/관리 하려는 목적으로 사용.
기본으로 제공하는 Query를 커스터마이징 가능
> 

기본적으로 제공되는 쿼리로 가져오는 값 (username으로 검색)

- 사용자 검색 :  username, password, enabled
- 권한 검색 : username, authority
- 그룹 검색 : groupid, groupname, groupautority

```java
@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("""
                        select username, password, enabled 
                        from users 
                        where username=?
                        """)
                .authoritiesByUsernameQuery("""
                        select username, authority 
                        from authorities 
                        where username=?
                        """)
                .passwordEncoder(new NoEncodingPasswordEncoder());
    }
     */
```

## LDAP 기반

> **Lightweight Directory Access Protocol**  : 네트워크 상의 디렉토리 서비스 표준인 X.500의 DAP를 기반으로한 경량화(Lightweight) 된 DAP 버전으로 TCP/IP 계층과 관련되어있음.

주로 사용자, 시스템, 네트워크, 서비스, 애플리케이션 등의 정보를 트리 구조로 저장하여 조회하거나 관리하는 용도나 특정 영역에서 이용자명과 패스워드를 확인하여 인증하는 용도.

관련 기술블로그 : [https://www.blocko.io/developer/ldap-인증/](https://www.blocko.io/developer/ldap-%EC%9D%B8%EC%A6%9D/)
> 

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.ldapAuthentication()
                .userSearchBase("ou=people") // 사용자를 찾기위한 기준점 쿼리
                .userSearchFilter("(uid={0})") // 사용자를 찾기위한 필터
                .groupSearchBase("ou=groups") // 그룹을 찾기위한 기준점 쿼리
                .groupSearchFilter("member={0}") // 그룹을 찾기위한 필터
                .contextSource()
                .root("dc=taco,dc=com") // ldap 서버 루트 경로 지정
                .ldif("classpath:users.ldif") // ldif 파일을 지정
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasscode");

    }
```

### 커스텀 사용자 명세 서비스

> 스프링에 내장된 사용자 스토어 외 사용자 명세 서비스를 커스터마이징해서 사용이 가능
> `UserDetails` 를 구현받는 엔티티를 생성해야함.
> 

```java

import java.io.Serial;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@RequiredArgsConstructor
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private final String username;
    private final String password;
    private final String fullname;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String phoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

### 참고: UserDetails

| 메소드명 | 리턴타입 | 설명 |
| --- | --- | --- |
| getAuthorities() |  Collection<? extends   GrantedAuthority> |  계정이 갖고있는 권한 목록을 리턴한다. |
|  getPassword() |  String |  계정의 비밀번호를 리턴한다. |
|  getUsername() |  String |  계정의 이름을 리턴한다. |
|  isAccountNonExpired() |  boolean |  계정이 만료되지 않았는 지 리턴한다. (true: 만료안됨) |
|  isAccountNonLocked() |  boolean |  계정이 잠겨있지 않았는 지 리턴한다. (true: 잠기지 않음) |
|  isCredentialNonExpired() |  boolean |  비밀번호가 만료되지 않았는 지 리턴한다. (true: 만료안됨) |
|  isEnabled() |  boolean |  계정이 활성화(사용가능)인 지 리턴한다. (true: 활성화) |

```java
@Service
@RequiredArgsConstructor
public class UserRepositoryUserDetailsService implements UserDetailsService {
	private final UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepo.findByUsername(username);
		if (user != null) {
			return user;
		}
		throw new UsernameNotFoundException("User '" + username + "' not found");
	}
}

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
}

		@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll")
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .csrf();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

@Data
public class RegistrationForm {

	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;

	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(username,passwordEncoder.encode(password),fullname, street, city, state, zip, phone);
	}
}

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	@GetMapping
	public String registerForm() {
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(RegistrationForm form) {
		userRepo.save(form.toUser(passwordEncoder));
		return "redirect:/login";
	}
}
```

# password 암호화

> SpringSecurity5부터 의무적으로 `PasswordEncoder`를 이용하여 암호화를 하지 않으면 오류가 발생함.  암호화를 하면 DB에 저장될 때 암호화된 password가 저장이 됨.
> 

암호화된 사용자정보

| ID   | CITY   | FULLNAME   | PASSWORD   | PHONE_NUMBER   | STATE   | STREET   | USERNAME   | ZIP   |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | aaa | user | $2a$10$GtCiKop4o9Vlm7CoKryHCuXVehCyNYB5krnMJBbfOylkVInzinc7K | aaa | aaa | aaa | user | aaa |

### SpringSecutiry 암호화 모듈

- `BCryptPasswordEncoder` : bcrypt 해싱
- `NoOpPasswordEncoder` : 암호화 x
- `Pbkdf2PasswordEncoder` : PBKDF2 암호화
- `SCryptPasswordEncoder` : scrypt 해싱
- `StandardPasswordEncoder` : SHA-256

```java
public class NoEncodingPasswordEncoder implements PasswordEncoder {
	
	@Override
	public String encode(CharSequence rawPwd) {
		return rawPwd.toString();
	}
	
	@Override
	public boolean matches(CharSequence rawPwd, String encodedPwd) {
		return rawPwd.toString().equals(encodedPwd);
	}
}
```

PasswordEncoder 의 encode 와 matches 메서드를 이용하여 암호화/비교를 수행함.