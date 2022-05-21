# Chapter3 JPA

# ORM 개요

> OOP 시대에 객체를 RDBMS에 관리하다 보니 SQL 중심적인 개발을 하게 됨으로써 나타나는 문제점들을 해결하기 위해 도입.
>
>객체는 ↔ RDBMS 사이를 매끄럽게 연결하여 **SQL 의존성을 덜어내고 OOP 중점적으로 개발**을 하는 것이 목표.
&nbsp;

# JPA (Java Persistense Api)

현재 Java 진형의 ORM 표준으로 인터페이스이고 구현체는 Hibernate, EclipseLink, DataNucleus 등 

그 중 `Hibernate` 가 우리가 흔히 쓰는 구현체이다.
&nbsp;

# SpringDataJPA

JPA를 한 단계 더 추상화 시켜 개발을 편하게 해주는 Spring에서 제공하는 인터페이스

![Untitled](https://user-images.githubusercontent.com/45655434/169654015-cedadf4f-c5b7-4f2e-a545-ddfb9f1fe923.png)

&nbsp;
# JPA 구동방식

![Untitled 1](https://user-images.githubusercontent.com/45655434/169654027-b75150ea-3e57-4dce-b5d6-19cb4f58ff9f.png)

> persistence.xml에 jdbc 관련 정보 ( driver, username, password, url ) 등을 읽어와서 
`EntityManagerFactory`에 저장하고 `EntityManager` 를 통해 작업을 한다.
>
>EntityManagerFactory 는 하나만 생성해서 어플리케이션 전체에 공유되고
>Threadsafe 한 EntityManager를 생성**하고 내부엔 `영속성 컨텍스트`가 있음.
>한번 사용한 EntityManager 는 사용 후 버려지는데 사용 단위는 `Transaction` 범위
 
&nbsp;
# 영속성 컨텍스트

엔티티를 영구적으로 저장하는 공간이며 내부에 `1차캐시`가 있다.

![Untitled 2](https://user-images.githubusercontent.com/45655434/169654048-a5c0fd4e-5121-44cd-b9b6-727e9737d0d8.png)

> DB에서 데이터를 조회할 땐 무조건 1차캐시를 먼저 확인하고 없으면 1차캐시로 데이터를 영속화를 한 후에 값을 반환한다.
> 

&nbsp;
## Entity의 생명주기

Entity의 상태는 `비영속`, `영속`, `준영속`, `삭제`총 4가지로 나타낼 수 있음.

![Untitled 4](https://user-images.githubusercontent.com/45655434/169654104-d3abbc01-c6b5-418a-b11c-ac9e2c44822a.png)

### 비영속 (new)

> 단순히 엔티티를 새로 생성한 상태
> 

```java
Member member = new Member("kim"); 
```

### 영속 (Managed)

> 새로 생성한 엔티티가 영속성 컨텍스트에 의해 관리되는 상태
저장 or 조회를 실행하면 영속성컨텍스트가 먼저 엔티티를 관리한다.
> 

```java
EntityManager.persist(member); // 저장
EntityManager.find(id); //조회
```

### 준영속 (detach)

> 영속성 컨텍스트가 엔티티를 관리하지 않는 상태
Query가 commit을 호출해도 영속성을 거치지 않는 상태이기 때문에 동작하지 않음
1차캐시에 남아있지 않으므로 당연히 DirtyChecking 기능도 동작하지 않음
영속성컨텍스트의 모든 기능을 이용할 수 없는 상태이다.
> 

```java
EntityManager.detach(member); // 준영속으로 만들기
EntityManager.clear() // 영속성 컨텍스트 초기화
```

### 삭제 (removed)

> 엔티티를 삭제한 상태
> 

```java
EntityManager.remove(member); // 삭제 명령
```
&nbsp;
## 영속성 컨텍스트 Flush

> 엔티티의 생명주기는 위와 같으며, `Transaction.commit()` 과 같이 커밋이 호출되어야 DB에 반영이 된다. 이를 `Flush` 라고 한다.
>그 전까진 영속성컨텍스트가 붙잡고 있는 상태이다. ( Flush가 되어도 영속성 컨텍스트가 사라지는건 x )
>
>그럼 커밋이 호출되는 시점, 즉 DB에 반영되는 시점 기준은 언제일까
> 

- EntityManager.flush()  를 직접호출
- Transaction commit (자동호출)
- JPQL 쿼리를 직접 날림 (자동호출)

위 3가지 조건일 때 DB에 작업한 내용이 반영이 된다. ( 반영이 되어도 영속성 컨텍스트엔 내용이 남아 있음)
&nbsp;
# 영속성 컨텍스트의 이점

위와 같은 내용을 바탕으로 영속성컨텍스트에 의해 관리를 받을때 얻을 수 있는 이점이 있다.

## 1. Entity 동일성 (==) 보장

데이터 값이 같은 두 객체는 참조하는 주소 값이 다르다.

하지만 EntityManager에 의해 관리되는 상태이면 두 객체는 같은 참조를 지닌다.

```java
@Slf4j
@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    EntityManager entityManager;

    @DisplayName("userA id 값으로 조회")
    @Test
    void same(){
        User userA = new User("userA");
        entityManager.persist(userA);

        User findUser = entityManager.find(User.class, userA.getId());

        log.info("user A = {}", userA);
        log.info("findUser = {}",findUser);

        Assertions.assertSame(userA,findUser);
    }
}
```

![Untitled 5](https://user-images.githubusercontent.com/45655434/169654163-6c5dcbac-2c4b-4744-9af5-6fce267b7be5.png)
&nbsp;
## 2. 쓰기지연

모든 데이터 변경 Query를 쓰기지연 저장소에 보관해 놨다가 commit을 수행할 때 한번에 날린다. 

![Untitled 6](https://user-images.githubusercontent.com/45655434/169654127-7e83695d-9866-425c-aeb7-880f9149d218.png)

```java
@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class UserTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void lazy(){

        log.info("===== persist 효출 전 =====");
        User userA = new User("userA");
        entityManager.persist(userA);
        log.info("===== persist 효출 후 =====");

    }
}
```

![Untitled 7](https://user-images.githubusercontent.com/45655434/169654197-4e16d73e-8cfb-4ef0-be72-6d7d83c098b7.png)

> persist 를 호출해도 바로 insert query가 발생하지 않고 commit시점에 호출이 된다.
> 
&nbsp;
## 3. DirtyChecking

```java
@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class UserTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void dirtyChecking(){

        User userA = new User("userA");
        entityManager.persist(userA);

        userA.setName("userB");

        entityManager.flush();
        entityManager.clear();

        User userB = entityManager.find(User.class, userA.getId());
        log.info("변경 후 = {}",userB);
    }
}
```

![Untitled 8](https://user-images.githubusercontent.com/45655434/169654236-a0411a49-50be-4d35-896c-0beda182676a.png)

![Untitled 9](https://user-images.githubusercontent.com/45655434/169654267-d182de10-445d-4e65-b947-fc6a1bbedc55.png)

> 모든 Entity는 영속성 컨텍스트에서 관리되며 데이터의 CRUD를 추적.
>
>변경이 일어나면 영속성 컨텍스트에서 `DirtyChecking`을 통해 1차캐시에서 먼저 조회를 한 후 스냅샷과 값이 다르면 **commit** 시점에 이를 반영한다.
> 
&nbsp;
## 4. 지연로딩

만약 연관관계에 있는 두 Entity 중(ex. user, team) 하나(user)를 호출 하였을때
그 와 연관된 다른 엔티티는 실제로 호출되기 전 까진 호출되지 않는다.

### User만 호출 했을 경우 (user에 대한 select query 한번만 나감)

```java
@Slf4j
@SpringBootTest
@Rollback(value = false)
@Transactional
class UserTest {

    @Autowired
    EntityManager entityManager;

    Long userId;

    @BeforeEach
    void save(){

        User userA = new User("userA");
        Team team = new Team("team");

        team.users.add(userA);

        userA.setTeam(team);

        entityManager.persist(team);
        userId = userA.getId();

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("team is null")
    void test(){

        User user = entityManager.find(User.class, userId);
        Team team = user.getTeam();

        Assertions.assertNull(team.name);
    }
}
```

![Untitled 10](https://user-images.githubusercontent.com/45655434/169654285-2657bc2e-34b8-4b54-804e-afa38f909fe4.png)
&nbsp;
### User의 Team 까지 호출 하였을 경우 (프록시 강제초기화)

```java
@Test
    @DisplayName("team is not null")
    void test(){

        User user = entityManager.find(User.class, userId);
        String teamName = user.getTeam().getName();

        Assertions.assertNotNull(teamName);
    }
```

![Untitled 11](https://user-images.githubusercontent.com/45655434/169654296-25a7f6df-f898-48fc-80f1-f98b402b7d96.png)

> team 의 getName() 을 호출해야만 team에 대한 select query가 한번 더 나간다.
>즉, 실제로 사용하기 전 까진 query가 발생하지 않아 리소스 절감에 도움이 된다.
>
>이 옵션을 사용하기 위해선 연관관계를 지정할때 `FetchType=Lazy`로 설정해야 동작함.
>(Default 옵션으로 `Eager` 이 설정되어 있어 query가 user만 호출해도 2번 발생함)
>보통 `@X To One` 관계일때 Lazy로 걸어둔다. ( 최적화 관련 옵션 )
> 

# 연관관계

> DB의 한쪽 엔티티가 다른 엔티티와의 연관된 데이터를 이용하기 위해선 한쪽에서 외래키를 가지고 있어야 한다.
>
>JPA에서는 필드에 외래키로 관리하는 대신 엔티티를 필드에 추가하고 연관관계의 주인을 지정해준다. ( FK 를 가지고 있는 쪽이 연관관계의 주인이다.)
> 

```java
@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    Long id;
    String name;

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Team team;

    public User(String name) {
        this.name = name;
    }

}

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    Long id;
    String name;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL) 
    List<User> users = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
```

## 연관관계의 종류

- N:M (@ManyToMany) ⇒ 필드추가가 불가능하므로 일대다 다대일로 풀어쓴다.
- 1:N (@OneToMany) ⇒ mappedby 옵션으로 연관관계의 주인을 지정
- N:1 (@ManyToOne) ⇒ 외래키 지정
- 1:1 (@OneToOne) ⇒ 외래키 지정