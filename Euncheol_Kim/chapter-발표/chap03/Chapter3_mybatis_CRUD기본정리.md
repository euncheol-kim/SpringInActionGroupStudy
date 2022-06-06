----



### **👨‍👨‍👦‍👦 SpringInAction Group Study [이채민, 권태구, 최재호, 곽현기, 김은철]**

**group - GIT : https://github.com/euncheol-kim/SpringInActionGroupStudy**

<br>

#### **이채민 님 : https://github.com/CokeLee777**

#### **권태구 님 : https://github.com/Hashtae9**

#### **최재호 님 : https://github.com/jaero0725**

#### **곽현기 님 : https://github.com/nicebyy**

#### **김은철 (본인): https://github.com/euncheol-kim**

<br>

#### 작성자 : 김은철 

<br>

-----

<br>

<br>

### goal

> jdbc 프레임워크인 mybatis와 Database(mySQL)을 이용해 기본적인 CRUD를 이해한다.
>
> - mybatis 프로젝트 설명
> - mybatis_2 프로젝트 설명
> - mybatis이론

<br>

<br> **Euncheol-Kim 원격브랜치 실습 프로젝트 파일 구성**

| 프로젝트명 | 설명                                                         |
| ---------- | ------------------------------------------------------------ |
| mybatis    | class 내부에서의 데이터 생성/삭제/수정/저거의 RestAPI <u>(JDBC 프로그래밍은 아님)</u> |
| mybatis_2  | 데이터베이스 연결 및 mabatis를 이용한 RestAPI CRUD           |

<br>

<br>

#  1. 파일명 : mybatis

## [1] 개요

> <u>Database를 연결하지 않고</u> **class 내부에서의 데이터 생성 / 삭제 / 수정 / 제거의 RestAPI를 생성한다.**

<br>

## [2] 프로젝트 생성시 종속성 설정

- Spring Web
- Spring Data jdbc
- lombok
- Spring Boot DevTools
- MySQL Driver

<br>

## [3] 설정구성

| 패키지                  | 파일명                     | 설명                                |
| ----------------------- | -------------------------- | ----------------------------------- |
| src/main/java/resources | **application.properties** | mysql드라이버 설정 및 mysql 연결    |
|                         | **pom.xml**                | jdbc의존성설정<br />mySQL의존성설정 |

<br>

- **pom.xml**

```xml
 <!-- mySQL JDBC 종속성 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<!-- mySQL종속성 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

<br>

- **application.properties**

```properties

# Load the mysql driver (Database 종류마다 우측에 들어가는 내용이 상이)
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connect the mysql database (Database 종류마다 우측에 들어가는 내용이 상이)
spring.datasource.url=jdbc:mysql://localhost:3306/user?serverTimezone=UTC&characterEncoding=UTF-8

# mysql username
spring.datasource.username=root 

# mysql password
spring.datasource.password=1234

```

<br>

<br> 

#### * 설정을하며 해결이 되지 않았던 궁금증

mysql을 사용하지 않는 실습이라 `pom.xml`의 <u>mysql dependency 삭제</u> `application.properties`의 jdbc를 제외한 모든 내용을 삭제하고 실행했더니 `error`가 나왔습니다. 
<br><br>

제가 생각하기엔 mysql과 관련된 내용의 의존성을 지워주고 서비스를 실행했을 때, 문제 없이 동작해야하는 것 아닌가요?



## [4] mybatis의 파일 구성

| 패키지                                       | 파일                | 설명                             |
| -------------------------------------------- | ------------------- | -------------------------------- |
| src/main/java/**<u>com.example.mybatis</u>** | UserController.java | RestFull CRUD의 기능을 담은 파일 |
| src/main/java/**<u>mybatis.model</u>**       | User                | Model                            |

<br>

- User.java

```java
package mybatis.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // lombok annotation : 변수에 대한 getter & setter생성
public class User {
    private String id;
    private String name;
    private String phone;
    private String address;

    public User(String id, String name, String phone, String address) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}
```

<br>

- UserController.java

```java
package com.example.mybatis;

import mybatis.model.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// RestAPI 생성 - class내부에서 데이터 생성/삭제/수정/제거

@RestController
public class UserController {
    private Map<String, User> userMap;

    @PostConstruct // 초기화 진행
    public void init() {
        userMap = new HashMap<String, User>();
        userMap.put("1", new User("1", "홍길동", "111-1111", "서울시 강남구 대치1동"));
        userMap.put("2", new User("2", "홍길자", "111-1112", "서울시 강남구 대치2동"));
        userMap.put("3", new User("3", "홍길순", "111-1113", "서울시 강남구 대치3동"));
    }

    @GetMapping("/user/{id}") // id값을 통한 데이터 반환
    public User getUser(@PathVariable("id") String id) {
        return userMap.get(id);
    }

    @GetMapping("/user/all") // 모든 데이터 반환
    public List<User> getUserList() {
        return new ArrayList<User>(userMap.values()); // https://docs.oracle.com/javase/8/docs/api/java/util/Map.html -> Map의 데이터들을 ArrayList로 바꾼다.
    }

    @PutMapping("/user/{id}") //데이터 생성
    public void putUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        User user = new User(id, name, phone, address);
        userMap.put(id, user);
    }

    @PostMapping("/user/{id}") //데이터 수정
    public void postUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        User user = userMap.get(id);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
    }

    @DeleteMapping("/user/{id}") //데이터 삭제
    public void deleteUser(@PathVariable("id") String id){
        userMap.remove(id);
    }

}

```

<br>

- **@PathVariable** 

> (ex) -> @PathVariable "id" String id
>
> 사용자 입력("id"에 해당)을 id매개변수로 받아와 내부적으로 처리

<br>

- **@RequestParam**

> (ex) -> @RequestParam("address") String address
>
> 객체의 데이터를 받아오기 위해 사용한다.

<br>

<br>

#  2. 파일명 : mybatis_2 

## [1] 개요

> <u>mysql을 spring과 연결</u>하여 **mybatis framework를 통해 CRUD를 진행한다.**

- mybatis frameowrk = ORM framework (객체와 관계형 데이터를 매핑하기 위한 기술)
- mabatis는 자바 오브젝트와 SQL사이의 자동 매핑 기능을 지원한다.

<br>

## [2] 프로젝트 생성시 종속성 설정

- Spring Web
- Spring Data jdbc
- lombok
- Spring Boot DevTools
- MySQL Driver

<br>

## [3] 설정구성

| 패키지                  | 파일명                     | 설명                                |
| ----------------------- | -------------------------- | ----------------------------------- |
| src/main/java/resources | **application.properties** | mysql드라이버 설정 및 mysql 연결    |
|                         | **pom.xml**                | jdbc의존성설정<br />mySQL의존성설정 |

<br>

- **pom.xml**

```xml
 <!-- mySQL JDBC 종속성 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<!-- mySQL종속성 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

<br>

- **application.properties** <u>\<enviroment></u>

```properties
# Load the mysql driver (Database 종류마다 우측에 들어가는 내용이 상이)
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connect the mysql database (Database 종류마다 우측에 들어가는 내용이 상이)
spring.datasource.url=jdbc:mysql://localhost:3306/user?serverTimezone=UTC&characterEncoding=UTF-8

# mysql username
spring.datasource.username=root 

# mysql password
spring.datasource.password=1234

```

<br>

<br> <br>

## [4] mybatis_2의 파일 구성

| 패키지                                       | 파일                | 설명                                        |
| -------------------------------------------- | ------------------- | ------------------------------------------- |
| src/main/java/**<u>com.example.mybatis</u>** | UserController.java | RestFull CRUD의 기능을 담은 파일            |
| src/main/java/**<u>mybatis.model</u>**       | User                | Model                                       |
| src/main/java/**<u>mapper</u>**              | UserMapper          | mybatis framework를 이용해 구성한 interface |

<br>

<br>

- User.java <u>\<typeAlias 설정></u>

```java
package mybatis.model;

import lombok.Data;

@Data // lombok annotation : 변수에 대한 getter & setter생성
public class User {
    private String id;
    private String name;
    private String phone;
    private String address;

    public User(String id, String name, String phone, String address) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}

```

<br>

- UserMapper.java <u><mapper등록></u>

```java
package com.example.mybatis_2.mapper;

import mybatis.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper //Mapper 인터페이스
public interface UserMapper {

    @Select("SELECT * FROM User WHERE id=#{id}")
    User getUser(@Param("id") String id); //전달된 id를 가지고 database에서 조회를해서 User 객체로 반환하는 api
    // @Param어노테이션 덕분에 매개변수 id가 #{id}에 매핑이 된다.

    @Select("SELECT * FROM User")
    List<User> getUserList();

    @Insert("INSERT INTO User VALUES(#{id}, #{name}, #{phone}, #{address})")
    int insertUser(@Param("id") String id, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Update("UPDATE User SET name=#{name}, phone=#{phone}, address=#{address} WHERE id=#{id}")
    int updateUser(@Param("id") String id, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Delete("DELETE FROM User WHERE id=#{id}")
    int deleteUser(@Param("id") String id);
}

```

- @Mapper

> 관계형 데이터베이스(RDBMS)를 자바의 객체 지향 모델로 매핑하게 도와주는 인터페이스다. 

<br>

- #{....}

> 사용자의 입력값을 객체와 데이터베이스를 매핑시키기 위해서 사용한다.

<br>

- @Param

> (ex) ->  @Select("SELECT * FROM User WHERE id=#{id}")
>                User getUser(@Param("id") String id);
>
> 
>
> @Param어노테이션 덕분에 매개변수 id가 #{id}에 매핑이 된다.

<br>

- UserController.java

```java
package com.example.mybatis_2;

import com.example.mybatis_2.mapper.UserMapper;
import mybatis.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    private UserMapper mapper;

    public UserController(UserMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id) {
        return mapper.getUser(id);
    }

    @GetMapping("/user/all")
    public List<User> getUserList() {
        return mapper.getUserList();
    }

    @PutMapping("/user/{id}") //데이터 생성
    public void putUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        mapper.insertUser(id, name, phone, address);
    }

    @PostMapping("/user/{id}") //데이터 수정
    public void postUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        mapper.updateUser(id, name, phone, address);
    }

    @DeleteMapping("/user/{id}") //데이터 삭제
    public void deleteUser(@PathVariable("id") String id){
        mapper.deleteUser(id);
    }

}

```

- @PathVariable

> mabatis 프로젝트에 설명



<br><br><br>

# 3. mybatis3 framework 이론

## [1] mybatis3 란?

데이터베이스 프로그래밍을 좀 더 쉽게 할 수 있게 도와주는 프레임워크

- 기존 JDBC(자바 프로그램이 데이터베이스와 연결되어 데이터를 주고 받을 수 있게 해주는 프로그래밍 인터페이스)를 조금 더 쉽고 유연하고 편하게 사용하기 위해 개발되었다.



## [2] mybatis3 의 장점

- 프로그램 코드와 <u>SQL쿼리의 분리</u>로 코드의 간결성 및 유지보수성 향상
- 빠른 개발이 가능하며 생산성이 향상된다.



## [3] mybatis3 구조

![image](https://user-images.githubusercontent.com/72078208/169668693-bfdace9a-ac87-4330-98e6-7093fdaafad4.png)

#### mybatis3의 구성

![image](https://user-images.githubusercontent.com/72078208/169668855-b5e1933c-dcb4-4ea7-ae7f-9da9172a17e4.png)

<br>

<br>

**<u>mybatis3의 구성</u>**은 괜히 말만 어려운것 같습니다.
 [typeAlias](./1), [enviroment](./2), [mapper](./3) 설명에 해당되는 코드들이니 확인하시면 좋을 것 같습니다.
