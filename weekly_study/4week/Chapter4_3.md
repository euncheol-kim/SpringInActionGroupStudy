# 4.3 웹 요청 보안 처리하기

우리는 웹 사이트를 만들 때, 로그인을 해야 서비스를 이용할 수 있는 페이지와 로그인을 하지 않아도 서비스를 이용할 수 있는 페이지를 나눠서 생각해야 한다. 특히, 홈페이지나 로그인 페이지, 회원가입 페이지 등은 인증되지 않은 모든 사용자가 사용할 수 있어야 한다.

이렇게 필터링하는 보안 규칙을 추가하려면 `시큐리티 설정정보 클래스`에 다음과 같은 메서드를 오버라이딩 하고 로직을 구성해야한다.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
     ...
}
```

이 메서드는 HttpSecurity라는 객체를 인자로 받는데, 이 객체는 웹 수준에서 보안을 처리하는 방법을 구성하는데 사용한다. 이 객체를 사용해서 구성할 수 있는 것들은 다음과 같다.

- HTTP 요청 처리를 허용하기 전에 충족되어야 할 특정 보안 조건을 구성한다.
	- 만약 관리자가 아닌 일반 사용자가 관리자 페이지에 들어가면 안될 것이다. 이런것들을 필터링하는 역할을 해준다.
- 커스텀 로그인 페이지를 구성한다.
	- 커스텀 로그인 페이지를 구성하지 않으면, 스프링 시큐리티가 자동으로 만들어주는 로그인 페이지를 사용하게 된다.
- 사용자가 애플리케이션의 로그아웃을 할 수 있도록 한다.
- CSRF 공격으로부터 보호하도록 구성한다.
	- 이 설정은 필수적으로 넣는 것을 일반적으로 추천한다.

## 4.3.1 웹 요청 보안 처리하기

/design, /orders 요청은 로그인한 사용자에게만 허용되어야 한다. 그리고 타코 애플리케이션에서 이 pathname 이외에는 모든 사용자에게 허용되어야 한다. 이러한 보안 조건을 구성하는 코드는 다음과 같다.

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design", "/orders")
                    .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**")
                    .access("permitAll");
    }
```

**authorizeRequests()**

- URL 경로와 패턴 및 해당 경로의 보안 요구사항을 구성할 수 있다.
- 즉, URL에 따라서 인증 정보를 필터링한다.
- **메서드 체인** 형태로 원하는 보안 요구사항을 추가한다.

**antMatchers()**

- Ant Style Pattern으로 URL을 매핑한다.
- 웹 개발을 진행하면 URL 매핑을 대부분 Ant Pattern으로 한다.
	- ? : 1개의 문자와 매칭
	- * : 0개 이상의 문자와 매칭
	- ** : 0개 이상의 디렉토리와 파일 매칭

이렇게 **authorizeRequests()** 메서드를 사용하여 특정 URL에 접근할 수 있는 사용자들을 필터링하게끔 코드를 메서드 체인 형식으로 작성할 수 있다.

위의 코드에서는 `/design,` `/orders` 에 해당하는 URL Pathname은 `ROLE_USER` 역할에 해당하는 사용자만 접근할 수 있고, 그 이외의 URL Pathname은 모든 사용자가 접근할 수 있다. 

여기에서는 특정 URL을 필터링하는 기능을 access 메서드 하나로만 처리를 하였지만, 이 메서드 이외에 다양한 메서드가 존재하니 필요에 따라서 레퍼런스를 찾아서 구성하는 것이 좋을 것이다.

## 4.3.2 커스텀 로그인 페이지 생성하기

스프링이 제공하는 로그인 페이지나 HTTP 기본 로그인 대화상자는 너무 평범해서 이 애플리케이션에서 적합하지 않다. 스프링 시큐리티를 사용하면서, 기본 로그인 페이지를 교체하려면 커스텀 로그인 페이지가 있는 경로를 스프링 시큐리티에 알려주어야 한다. 이것도 마찬가지로 authorizeRequests() 메서드를 사용해서 표현하면 된다. 이 코드는 다음과같다.

보안 요구사항들을 추가할 때, 다른 성격의 보안 요구사항을 추가하려면 and() 메서드를 사용해서 Refresh하고 작성해야 한다는 것을 잊지말자. 이것은 하나의 인증 구성이 끝나서 추가적인 HTTP 구성을 적용할 준비가 되었다는 것을 의미한다.

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design", "/orders")
                    .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**")
                    .access("permitAll")
                .and()  // 이 메서드는 인증 구성이 끝나서 추가적인 HTTP 구성을 적용할 준비가 되었다는 것을 뜻한다.
                    .formLogin()
                    .loginPage("/login");
    }
```
일반적으로 이렇게 커스텀 로그인 페이지를 처리하면 기본적으로 아이디와 비밀번호 필드는 username, password로 구성되므로, 필드의 이름을 다르게 설정하고 싶으면 다음과 같이 파라미터를 구성하면 된다. 이 경우에는 /authenticate 의 경로의 요청으로 로그인을 처리하게 된다.

```java
.and()
   .formLogin()
   .loginPage("/login")
   .loginProcessingUrl("/authenticate")
   .usernameParameter("user")
   .passwordParameter("pwd");
```
이렇게 커스텀 로그인 페이지를 간단히 구성하면 사용자는 로그인을 하면 사용자가 머물던 페이지로 이동하게 된다. 그러나 사용자가 로그인을 하면 특정 경로로 이동시키고 싶으면 다음과 같이 커스텀하게 로그인 디폴트 경로를 구성할 수 있다. 이렇게 구성하면 만약 사용자가 로그인 버튼을 눌러서 직접 로그인 페이지로 가고 로그인을 성공한 후에 특정 URL로 가게 할 수 있다.

```java
.and()
   .formLogin()
   .loginPage("/login")
   .defaultSuccessUrl("/design");
```

위의 코드는 사용자가 로그인 페이지를 직접 갔을 경우에 특정 URL로 가는 경우의 소스코드인데, 이에 추가해서 어떤 경로에서 로그인을 하든간에 특정 URL로 가게끔 할 수 있다. 다음과 같이 파라미터 alwaysuse 를 true로 전달하기만 하면 된다.

```java
.and()
   .formLogin()
   .loginPage("/login")
   .defaultSuccessUrl("/design", true);
```

## 4.3.3 로그아웃하기

로그아웃도 로그인하는 로직처럼 비슷하게 코드를 구성하면 된다. 코드 구성은 다음과 같이 HttpSecurity 객체의 logout 메서드를 호출하면 된다. 다음 로직은 로그아웃을 하고 성공적으로 완료되면 특정 URL로 이동되는 코드이다.

```java
.and()
   .logout()
   .logoutSuccessUrl("/")
```

## 4.3.4 CSRF 공격 방어하기

### CSRF(Cross-Site Request Forgery, 크로스 사이트 요청 위조)

사용자가 웹사이트에 로그인한 상태에서 악의적인 코드(사이트 간의 요청을 위조하여 공격하는)가 삽입된 페이지를 열면 공격 대상이 되는 웹사이트에 자동으로 (그리고 은밀하게) 폼이 제출되고 이 사이트는 위조된 공격 명령이 믿을 수 있는 사용자로부터 제출된 것으로 판단하게 되어 공격에 노출된다.

예를들면 자동으로 해당 사용자의 거래 은행 웹사이트 URL(송금을 위한)로 다른 폼을 제출하는 공격자 웹사이트의 폼을 사용자가 볼 수 있다. 즉, 공격자가 은행의 계좌정보를 입력하는 폼을 자신의 폼으로 슬쩍 바꿔치기 하는 것이다.

CSRF 공격을 막기 위해서 애플리케이션에서는 폼의 숨김(hidden) 필드에 넣을 CSRF 토큰을 생성할 수 있다. 해당 필드에 토큰을 넣은 후 나중에 서버에서 사용한다.

다행히 스프링 시큐리티에는 내장된 CSRF 방어 기능이 있다. 단지 HttpSecurity 객체에 메서드체인으로 추가하고, _csrf 라는 이름의 요청 속성에 넣으면 되기 때문이다. 다음과 같이 두개의 코드를 각각 추가하면 된다.

```java
.and()
   .csrf();
```

```html
<input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
```