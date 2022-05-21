# 3.2 스프링 데이터 JPA를 사용해서 데이터 저장하고 사용하기

## 3.2.1 스프링 데이터 JPA를 프로젝트에 추가하기

스프링 데이터 JPA는 JPA스타터를 의존성 파일(pom.xml)에 추가하기만 하면 스프링 부트 애플리케이션에서 사용할 수 있다.

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 3.2.2 도메인 객체에 애노테이션 추가하기

**Ingredient.java**

```java
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
public class Ingredient {

    //Ingredient 테이블은 따로 커스텀하게 id값을 생성해서 넣기 때문에 '키 자동생성 전략'이 필요없다.
    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

### @Entity

- @Entity가 붙은 클래스는 JPA가 관리하고, 엔티티라고 한다.
- JPA를 사용해서 관계형 데이터베이스의 테이블과 매핑할 클래스는 @Entity가 필수이다.

**주의할점**

- `Public`, `Protected`가 붙은 기본 생성자가 필수이다.
- final 클래스, enum, interface, inner 클래스에 사용할 수 없다.
- 저장할 필드에 final 키워드를 사용할 수 없다.

**속성 name**

```java
@Entity(name = "User")
public class Member{}
```
- JPA에서 사용할 엔티티의 이름을 정한다.
-  따로 지정하지 않으면 클래스 이름을 그대로 사용한다.(디폴트 값)
-  가급적 디폴트 값 사용 권장


### @Id

- 테이블의 PK(Primary Key)로 지정할 속성필드 앞에붙는 애노테이션이다.
-  직접 PK값을 할당하고 싶으면 이 애노테이션만 사용하면 된다.
-  PK 값 자동생성 전략을 사용하고 싶다면 @GeneratedValue 애노테이션을 추가하면 된다.

**Taco.java**

```java
@Getter
@Entity
public class Taco {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    @PrePersist
    void createdAt(){
        this.createdAt = new Date();
    }
}
```

### @GeneratedValue

- PK 자동생성 전략을 사용하고 싶을 때, PK 속성 앞에 붙히는 애노테이션이다.
- 기본값은 AUTO이다.
- 자동생성 전략은 다음과 같이 4가지가 존재한다.

**IDENTITY**

- 데이터베이스에 위임하는 방식이다.
- 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용한다.

```java
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**SEQUENCE**

- 데이터베이스 시퀀스 오브젝트에 사용하는 방식이다.
- @SequenceGenerator를 사용해서 시퀀스 생성기를 등록하고, 실제 데이터베이스의 생성될 시퀀스 이름을 지정해줘야 한다.
- 주로 시퀀스를 지원하는 Oracle, PostgreSQL, DB2, H2에서 사용한다.

```java
@Entity
@SequenceGenerator(name = "taco_seq_generator", sequenceName = "taco_seq")
public class Taco {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taco_seq_generator")
    private Long id;
}
```

**@SequenceGenerator 속성**

- name : 시퀀스 생성기 이름
- sequenceName : 시퀀스 이름
- intialValue : DDL 생성시에만 사용된다, 시퀀스를 생성할 때 처음 시작하는 수를 지정(기본값=1)
- allocationSize : 시퀀스 한 번 호출에 증가하는 수로 성능 최적화에 사용된다.(기본값=50)

**TABLE**

- 키 생성용 테이블을 사용하는 방식이다.
- 모든 DB에서 사용가능

```java
@TableGenerator(
        name = "TACO_ORDER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "TACO_SEQ", allocationSize = 1
)
public class Order implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "TACO_ORDER_SEQ_GENERATOR")
    private Long id;
}
```

**@TableGenerator 속성**

- name : 테이블 생성기 이름
- table : 테이블 이름
- pkColumnName : 시퀀스 컬럼명 (기본값=SEQUENCE_NAME)
- valueColumnName : 시퀀스 값 컬럼명 (기본값=NEXT_VAL)
- pkColumnValue : 키로 사용할 값 이름 (기본값=엔티티명)
- initialValue : 초기 값 (기본값=0)
- allocationSize : 시퀀스 한 번 호출에 증가하는 수 (기본값=50)
- uniqueConstraint : 유니크 제약조건을 지정할 수 있음

**AUTO**

- 방언(각각의 SQL 문법)에 따라서 자동으로 지정하는 방식이다.
- 데이터베이스의 종류에 따라서 IDENTITY, SEQUENCE, TABLE 방법 중 하나를 자동으로 선택해주는 방법이다.
- Oracle일 경우 SEQUENCE 방식을 사용한다.

```java
@Id @GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```

### @ManyToMany

- 연관관계에서 다대다(N:M) 관계를 설정할 때 사용하는 애노테이션이다.
- 실무에서는 보통 @ManyToMany 보다는 일대다, 다대일 관계로 연결 테이블을 하나더 만들어서 매핑한다.
- targetEntity 속성은 연관된 엔티티의 타입정보를 설정하는 속성이다.

[JPA 연관관계 매핑 간단한 설명](https://cokeprogrammer.tistory.com/12?category=969415)

## 3.2.3 JPA 리퍼지터리 선언하기

JPA를 사용하는데 있어서 우리는 기본적인 CRUD를 가장 많이 쓰게될 것이다. 스프링 데이터 JPA는 이런 CRUD기능들과 여러가지 편리한 부가기능들을 자동으로 생성해주고 제공해준다.

스프링 데이터 JPA는 다음과 같이 인터페이스의 상속만 해준다면, 아주 손쉽게 사용할 수 있다.

```java
public interface IngredientRepository extends CrudRepository<Ingredient, String> {}
```

페이징 기능이나 더 많은 부가적인 기능들의 자동화를 원한다면, JpaRepository를 상속받으면 된다.

제네릭의 첫번째 인자로는 데이터접근을 할 테이블과 매핑된 엔티티 객체를 넣어주면 되고, 두번째 인자로는 그 엔티티의 PK 자료형을 넣어주면 된다.

CrudRepository 인터페이스를 가보면 여러가지 데이터를 접근할 수 있는 메서드들을 볼 수 있다.

**CrudRepository.class**

```java
@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();
}
```

## 3.2.4 JPA 리퍼지터리 커스터마이징하기

스프링 데이터 JPA를 사용하면 저렇게 이미 구현되어있는 메서드들을 사용할 수도 있지만, 커스터마이징하여 내가 원하는 쿼리를 메서드 이름으로 구현할 수 있다. 단, 메서드 이름 규칙을 지켜야 가능하다.

메서드 이름에 따라 어떻게 쿼리가 나가는지 보면 바로 이해가될 것이다.

**예시 1**

```java
List<Order> findByDeliveryZip(String, deliveryZip);
```

위의 메서드는 실제로 다음과 같은 쿼리가 나가게된다.

```mysql
SELECT * FROM Order WHERE deliveryZip = ?;
```

**예시 2**

```java
List<Order> readOrderByDeliveryZipAndPlacedAtBetween(String, deliveryZip, Date startDate, Date endDate);
```

위의 메서드는 실제로 다음과 같은 쿼리가 나가게된다.

```mysql
SELECT * FROM Order
WHERE deliveryZip = ?
AND placedAt BETWEEN startDate AND endDate;
```

이렇게 스프링 데이터 JPA는 구현되어있는 편리한 메서드 이외에, 우리가 커스터마이징하게 메서드이름만 규칙대로 짓기만 하면 원하는 쿼리를 모든 사용할 수 있다.

메서드 이름 짓는 몇가지 예시들은 다음과 같다.

- IsGreaterThan, GreaterThan
- IsLessThan, LessThan
- IsNull
- Equals
- IsIn, IsNotIn
- Contains
- StartsWith, EndsWith

이외의 메서드 이름짓기 규칙에 관한것은 스프링 데이터 JPA 공식문서를 참고하도록 하자.

[쿼리 메서드 필터 조건](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)

이렇게 메서드 이름으로 편리하게 커스터마이징할 수 있지만, 쿼리가 매우 복잡해지면 메서드 이름도 길어지고, 또한 메서드 이름만으로는 쿼리를 짜기 어려울 것이다.

따라서 다음과 같이 쿼리를 직접 작성하는 방법도 존재한다. 해당 메서드를 실행시키면 쓰여있는 쿼리가 실행되는 것이다.

```java
@Query("Order o where o.deliveryCity = 'Seattle'")
List<Order> readOrdersDeliveredInSeattle();
```