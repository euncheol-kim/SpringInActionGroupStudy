※ 2장 이후의 내용



# CHAPTER 3. 데이터로 작업하기

> 스프링에서 관계형 데이터베이스를 사용할 경우, 선택할 수 있는 대표 방법들
>
> 
>
> 1. Spring JDBC를 활용한 데이터 읽고 쓰기 (생략)
>
> 2. Spring Data JPA를 사용해서 데이터 저장하고 사용하기





# 1. JDBC를 사용해서 데이터를 읽고 쓰기

> Spring JDBC는 JdbcTemplate 클래에스 기반을 둔다.



## [1] 객체가 저장된 필드를 추가한다.

> src/main/java/tacos/web/Taco.java

```java
...
import java.util.Date;

@Data
public class Taco {
    private Long id;
    private Data createAt;
    ...
}

```

> src/main/java/tacos/web/Order.java

```java
...
import java.util.Date;

@Data
public class Order {
    private Long id;
    private Date placedAt;
    ...
}
```



## [2] JdbcTemplate 및 내장 데이터 베이스(H2) 종속성 추가

> jdbc Template 종속성 추가

- **target/pom.xml**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
    <version>2.6.6</version>
</dependency>
```

> 내장 데이터베이스 H2 종속성 추가

- **target/pom.xml**

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.200</version>
    <scope>test</scope>
</dependency>
```





## [3] JdbcRepository 정의하기

> [1] 식자재 레포지토리의 연산은 아래와 같아야한다.
>
> 
>
> 1. 데이터베이스의 모든 식자재 데이터를 쿼리하여 Ingredient 객체 컬렉션에 넣어야한다.
> 2. id를 사용해서 하나의 Ingredient를 쿼리해야한다.
> 3. Ingredient 객체를 데이터베이스에 저장해야 한다.



### <1> IngredientRepository 인터페이스 설정

> 식자재 레포지토리의 연산을 메소드로 구현한다.



- **tacos/data/IngredientRepository.java**

```java
package tacos.data;

import tacos.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Ingredient finById(String id);
    Ingredient save(Ingredient ingredient);
}

```

> 인터페이스를 구현했으니, JdbcTemplate를 이용해서 데이터베이스 쿼리에 사용할 수 있도록
> IngredientRepository 인터페이스를 구현해야한다. 구현한 코드는 아래와 같다.



>





































# 2. JPA를 사용해서 데이터를 읽고 쓰기