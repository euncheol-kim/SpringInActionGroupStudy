# Chap03 데이터로 작업하기
## 📖 Chap03.1 에서 배우는 내용  
#### 🏁 [목표] Spring JDBC 개념과 사용법 익히기
#### 🏁 [익혀야 되는 개념]
> 1) Spring 에서 JDBC를 이용하여 Database를 다루는 큰 그림
> 2) Plain JDBC와 Spring JDBC의 차이, Spring JDBC를 사용하는 이유?
> 3) Datasource의 Connection Pool 개념
> 4) JdbcTemplate을 이용하여 CRUD동작 구현하기 
> 5) SimpleJdbcInsert 사용법 (+objectMapper 개념)

## 3.1.0 Spring에서 Database를 다루는 큰그림
![image](https://user-images.githubusercontent.com/55049159/169668555-346e2b04-6c06-4f1e-9c26-3f8b308c4b75.png)
client가 웹으로 request를 보내면 servlet으로 구현된 controller가 request를 받아, service 클래스를 호출한다. <br> 
비지니스 로직은 service에서 실행되고 그 결과를 controller에게 주고 controller가 받아 model에 넣어주고 그 model을 JSP에 넣어줘서 보내주거나 타임리프를 사용하여 보내주기도한다.

데이터베이스를 접근할때, DAO(repository)의 계층이 더 필요하다. sql을 만들어서 DB에 요청하고, 결과를 받아 객체를 리턴해주는 역할이라고
DB에 관련된것을 DAO에 몰아넣고 사용한다고 보면됨. 

![image](https://user-images.githubusercontent.com/55049159/169668538-65d3d66e-739d-47ed-af38-8a1c2da0e364.png)

DAO와 DB사이에 3가지 클래스가 존재한다. <br> 

DAO는 JDBC interface를 이용하기 위해 JDBC template를 이용한다. spring-jdbc 라이브러리서 제공하고 있는 하나의 클래스이다. <br> 

다양한 데이터베이스가 있을 수 있는데, JDBC template이 Jdbc Driver를 사용하여 DB를 접근한다. <br> 

데이터베이스를 접근하기 위해 connection이 필요한데, 그 connection을 만들기 위한 설정정보를 담고 있는 Configuration인 DataSource를JDBC template이 사용한다. <br> 

여기서는 내부 DB를 사용하여 Datasource를 사용하진 않지만 외부 DB를 사용하여 연결하려면 Datasource를 필수적으로 설정해야 한다. <br>

(1), (2), (3)은 libary를 제공받아 사용한다. <br>

## 3.1.1 Plain JDBC vs Spring JDBC
> <b>Plain JDBC</b> <br>
> 모든거를 개발자가 직접 다 해야함. 오류처리 커낵션 연결 등등.. 

```java
public void GettingRows() {
    Connection conn=null;
    Statement stmt=null;
    Resultset rset=null;

   try {
        conn = dataSource.getConnection();
        stmt = conn.createStatement (); 
        rset = stmt.executeQuery ("select count(*) from customers");
        while (rset.next()) {
	   count = rs.getInt(1);
        }

   } catch (SQLException e) {
	LOGGER.error(e); throw e;
    }
   finally {
             //code to clean up resources
   }
```

<br>

> <b>Spring JDBC</b> <br> 
> low levels에 대해 관심을 끄게 해줌 <br>
> connection을 열어줌<br>
> SQL statement를 준비하고 실행시켜줌<br>
> 에러처리 <br>
> 트랜잭션 핸들링<br>
> connection 닫아줌

```java
@Repository
public class SpringDaoImpl {
    private JdbcTemplate jdbcTemplate;

    @Autowired
     public void setDataSource(DataSource dataSource) {
         this.jdbcTemplate = new JdbcTemplate(dataSource);
     }

     public int getCustomerCount() {
         return jdbcTemplate.queryForInt("select count(*) from customers");
     }
}
```

![image](https://user-images.githubusercontent.com/55049159/169668591-c044c9e1-0e96-4f6f-8907-1de1ebe186c9.png)

#### 결국 Spring jdbc를 사용하는 우리가 해야하는 부분?
- 커넥션 파라미터 정의
- SQL statement 쓰기
- 파라미터의 선언과 파라미터 value 제공

## 3.1.2 Datasource 과 Connection Pool의 개념

- Database에 접근하기 위해선 connection을 사용해야 한다. 
- connection설정을 위해 spring에서는 Datasource를 사용한다. 
- DataSource(Interface) -> 구현체 : <b> BasicDataSource </b>, poolingDataSource, SingleConnectionDataSource, DriverManagerDatasource

![image](https://user-images.githubusercontent.com/55049159/169668576-f7d01c6e-d0d2-4143-b7aa-b84a5c23b8ed.png)
 
client으로 부터 request가 오면, tomcat이 request당 thread를 만든다. <br>
thread를 만들고 제거하는 비용이 크다. thread가 무변별하게 만들어지고 제거되는 상황에서 성능에 치명적인 영향을 준다. <br>
tomcat에서 thread pool을 만들어놔서, 미리 어느정도 만들어 논다. request가 올때 그떄그떄 생성하지 않고 만들어 놓고 준다. <br>
traffic이 몰리면 기다리는 경우, thread가 부족해서 waiting하는 경우라고 보면됨. <br><br>

DB connection Pool도 마찬가지의 개념이다. DB를 사용하려면 connection을 만들어야 하는데 open, close에 대한 overhead가 커서 미리 만들어놓고 꺼내서 쓰도록 한다.<br>
#### connetion pool의 갯수는 파라미터로 설정이 가능함. 너무 많이 만들어놓으면 메모리를 많이 잡아먹을 수 있기때문에 적절한 갯수만큼의 설정을 해주는 것이 좋다. 


## 3.1.3 JdbcTemplate을 이용하여 CRUD동작 구현하기

### 의존성 추가
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <!-- 사용할 DATABASE 관련  -->
    <dependency>
    	<groupId>mysql</groupId>
    	<artifactId>mysql-connector-java</artifactId>
    </dependency>

```

### 리포지터리 정의 (DAO) 
- 인터페이스를 만들고 그 함수들을 Override하는 방식으로 구현 



#### interface
```java
public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Ingredient findById(String id);
    Ingredient save(Ingredient ingredient);
}
```

### 구현 
```java
/*

# Jdbc Template을 활용한 select처리
Select 구분

1) query() : 쿼리문 수행결과가 한개 이상일 경우 -> list 로 반환 
2) queryForObjcet() : 쿼리문 수행결과가 한개 일경우 -> 객체 그대로 반환

query(sql, new Object[]{}, new RowMapper<Type>(){
		...
	});
	
뒤에 두 매개변수는 순서가 바뀌어도 된다. 
new Object []{값, 값, ...} : sql ?에 세팅할 값 / 매개변수가 없으면 필요없음. 
new RowMapper<Type>() : 조회 결과를 ResultSet으로 읽어 Type으로 변환한다. mapRow을 override해서 사용.
	
# update메서드 
update(sql, new Object[] {값,값,값}; : ?에 세팅할 값

String SQL = "insert into Student (name, age) values (?, ?)"; 
jdbcTemplateObject.update( SQL, new Object[]{"jaeho", 11} );

String SQL = "update Student set name = ? where id = ?"; 
jdbcTemplateObject.update( SQL, new Object[]{"jaeho", 10} );

String SQL = "delete  from Student where id = ?"; 
jdbcTemplateObject.update( SQL, new Object[]{20} );
*/

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(
                "select id, name, type from Ingredient", // 첫번째 인자 : sql문
                this::mapRowToIngredient); // 두번째 인자 : 결과값을 처리하는 부분. 

    }

//query 메서드는 두개의 인자를 받는다. 첫번째는 sql이며, 두번째는 RowMapper 인터페이스를 구현한 메서드이다.
//이부분에 대해서 익명함수를 사용해서 사용해도되지만, 여기서는 this::mapRowToIngredient를 사용하였다. 
/*

public int getCustomerCount(){
	return jdbcTemplate.queryForInt("select count(*) from customer");
}
=>
public int getCustomerCount(){
	return jdbcTemplate.queryForObject("select count(*) from customer", Integer.class);
}

### RowMapper 사용 
	String SQL = "select * from Student";
	
	List<Student> students = jdbcTemplate.query (
					SQL, 
					new RowMapper<Student>( ) {
	   public Student mapRow(ResultSet rs, int rowNum) throws SQLException { 
		Student student = new Student(); 

		student.setID(rs.getInt("id")); 
		student.setName(rs.getString("name")); 
		student.setAge(rs.getInt("age")); 

		return student; 
	    } 
	}
	
*/
    @Override
    public Ingredient findById(String id) {
        return jdbcTemplate.queryForObject(
                "select id, name, type from Ingredient where id=?",
                this::mapRowToIngredient, id);
    }
    
    //하나만 가져올때 queryForObject를 사용한다. 첫번쨰, 두번째 매개변수가 query와 같고, 세번째 매개변수로 id를 준다. 
    //세번째 매개변수가 ?를 교채하여 넣어준다. 

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
                "insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }
    //JdbcTemplate의 update 메서드는 결과 세트의 데이터를 객체로 생성할 필요가 없어 query()나 queryForObject보다 간단하다. 
    //?,?,? 에 들어갈 각 매개변수속성값만 지정하면된다. 
    private Ingredient mapRowToIngredient(ResultSet rs int rowNum) {
    	Ingredient ingredient new Ingerdient();
	
	ingredient.setId(rs.getString("id"));
	ingredient.setName(rs.getString("name"));
	Ingredient.Type.valueOf(rs.getString("type"));
	
	return Ingredient;
    }
}

```
### RowMapper 사용 
![image](https://user-images.githubusercontent.com/55049159/169673645-482da307-0b17-4305-a6ea-8a619cc199a1.png)

```java
@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTacoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);

        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco) {

        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory  preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory (
                        "insert into Taco (name, createdAt) values (?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP);

        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = preparedStatementCreatorFactory.new PreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        new Timestamp(taco.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbcTemplate.update(
                "insert into " +
                        "Taco_Ingredients (taco, ingredient) " +
                        "values (?, ?)",
                tacoId, ingredient.getId());
    }
```

#### 💡 update문을 사용하고 곧장 primary key를 return하고 싶을때는 기존의 jdbcTemplate을 사용하는 것보다 더 나은 방법이 존재한다. 

## 3.1.4 SimpleJdbcInsert 사용하기
- SimpleJdbcInsert는 간단하게 데이터를 저장하기 위해 만들어진 구현체이다.
- 기존의 의존성 주입으로 JdbcTemplate을 받는거와는 조금 다르게 설정을 해줘야 하는 부분이 있다.

- Map 자료구조로 필요한 파라미터 값들을 미리 대입시키고, SimpleJdbcInsert.executeAndReturnKey(Map).longValue() 를 수행하면 곧장 primary key 값을 얻어낼 수 있다.

- 이 동작이 가능하게 한 핵심은 생성자 쪽의 withTableName()과 usingGeneratedKeyColumns다.

- primary key 값을 얻어내는 과정이 이전보다 편리해짐. 또한 SqlParameterSource를 사용하는 방식도 존재하니 찾아보면 좋을 것같다. 

### 구현된 코드 

```java
@Repository
public class JdbcOrderRepository implements OrderRepository {

    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;
    private ObjectMapper objectMapper;
    
 // Object Mapper 는 java Object <->  json 으로 convert 해주는 역할을 자주하는데, 
 // 여기에서는 Map <-> java Object 하기 위해 사용됨. 
 
    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        // Order 테이블에 주문 정보 입력
        this.orderInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        // Taco_Order_Tacos 테이블에 해당 주문 id 및 연관된 타코들의 id 추가
        this.orderTacoInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order_Tacos");

        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        // Order 와 Taco 객체들을 저장하는 처리 총괄
        // 실제 저장하는 일은 saveOrderDetails(order), saveTacoToOrder(taco, orderId)
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);	// 아래 함수로
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId); // 아래함수로
        }

        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked") //try-catch로 안막음
        Map<String, Object> values =
                objectMapper.convertValue(order, Map.class);  
        values.put("placedAt", order.getPlacedAt());
	
        long orderId = orderInserter
                        .executeAndReturnKey(values)	
                        .longValue();		//이렇게 실행하면 곧장 primary key를 얻어낼 수 있. 
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
}

```
## 3.1.5 결론
최근 JPA 나 MyBatis 같은 더욱 강력한 도구들이 나와있지만, 간단한 초기 설정과 낮은 학습비용 덕분에 여전히 JDBC를 사용하는 곳이 존재한다. 상황에 맞게 기술을 사용하는것이 좋겠다. 
