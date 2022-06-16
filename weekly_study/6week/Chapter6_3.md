# 6.3 데이터 기반 서비스 활성화하기

우리는 6.2장에서 REST API를 설계하는 방법을 배웠다. 그렇게 REST API를 설계할 수도 있지만 스프링 데이터에는 애플리케이션의 API를 정의하는 데 도움을 줄 수 있는 기능도 있다.

스프링 데이터 REST 는 스프링 데이터의 또 다른 모듈이며, 스프링 데이터가 생성하는 리퍼지터리의 REST API를 자동 생성한다. 따라서 스프링 데이터 REST 의존성을 POM.xml 파일에 추가하면 우리가 정의한 각 리퍼지터리 인터페이스를 사용하는 API를 얻을 수 있다.

**스프링 데이터 REST 의존성 추가**

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```
이렇게 의존성만 지정하면 이미 스프링 데이터를 사용중인 프로젝트에서 REST API를 노출시킬 수 있다. 즉, 스프링 데이터가 생성한 모든 레퍼지토리의 REST API가 자동 생성될 수 있도록 스프링 데이터 REST가 자동-구성 된다.

이렇게 설정한 다음에 애플리케이션을 실행시키고 다음과 같이 API를 호출하면 작동한다. 이렇게 엔드포인트에 대한 모든 HTTP Method를 적용할 수도 있다. 즉, 스프링 데이터 REST가 생성한 엔드포인트들은 GET, POST, PUT, PATCH, DELETE 의 모든 메서드를 지원한다.

- `http://localhost:8080/ingredients` : 모든 식자재 데이터에 대한 엔드포인트
- `http://localhost:8080/ingredients/FLTO` : Flour Tortilla 식자재 데이터에 대한 엔드포인트

스프링 데이터 REST가 자동 생성한 API와 관련해서 한 가지 할 일은 해당 API의 기본경로를 설정하는 것이다. 왜냐하면 해당 API의 엔드포인트가 우리가 작성한 모든 다른 컨트롤러와 충돌하지 않게 해야하기 때문이다. 

따라서 이렇게 API 기본경로가 충돌이 나지않게 하기위한 한 가지 방법은 `application.properties` 파일에 다음과 같이 설정하면 된다. 이렇게 설정하면 식자재의 엔드포인트는 `http://localhost:8080/api/ingredients`가 된다.

```yml
spring:
   data:
      rest:
         base-path: /api
```

또는 다음과 같이 REST DATA 구성 클래스를 만들어서 `RepositoryRestMvcConfiguration` 클래스를 상속받아 메서드를 재정의하는 방법도 있다.

```java
@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestDataConfig  extends RepositoryRestMvcConfiguration {

  @Override
  protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    super.configureRepositoryRestConfiguration(config);
    try {
      config.setBaseUri(new URI("/data"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
```

## 6.3.1 리소스 경로와 관계 이름 조정하기

스프링 데이터 레퍼지토리의 엔드포인트를 생성할 때, 스프링 데이터 REST는 해당 엔드포인트와 관련된 엔티티 클래스 이름의 복수형을 사용한다.

이렇게 기본값으로 복수형을 사용하기 때문에 우리가 원하는 이름의 엔드포인트를 얻지 못하는 경우가 발생한다. 예를들면 우리는 `/tacos` 라는 엔드포인트를 원하는데 스프링 데이터 REST는 `/tacoes` 로 지정하기 때문에 타코 리스트의 API 요청을 수행하려면 `/api/tacoes` 로 요청해야 하는 문제점이 존재한다.

이러한 문제점에 대한 해결책은 간단히 엔드포인트를 바꾸고 싶은 엔티티에 애노테이션 하나를 추가해주면 된다.

```java
@Data
@Entity
@RestResource(rel="tacos", path="tacos")
public class Taco { ... }
```

이렇게 **관계 이름**과 **경로**를 구체적으로 지정하여 변경할 수 있다.

## 6.3.2 페이징과 정렬

홈 리소스의 모든 링크는 선택적 매개변수인 page, size, sort를 제공한다. 기본값으로는 한 페이지당 20개의 항목이 반환된다는 특징이 있다.

이 떄, 페이지 번호와 페이지 크기를 바꾸고 싶다면 page와 size 매개변수를 지정해서 요청하면 된다. 또한 정렬을 하고싶다면 정렬할 속성명과 어느 방향으로 정렬할 것인지도 매개변수를 지정해서 요청하면 된다.

예를들어 **한 페이지당 10개**의 항목을 반환하고 이 중에서 **2번 페이지**에 해당하는 타코 리스트 항목을 요청하고 싶다면 다음과 같은 URL로 요청하면 된다.

```
"localhost:8080/api/tacos?page=2&size=10"
```

또한 **최근 타코가 만들어진 순서대로 정렬**해서 보고싶다면 다음과 같이 sort 속성과 함께 요청하면 된다.

```
"localhost:8080/api/tacos?page=2&size=10&sort=createdAt,desc"
```

## 6.3.3 커스텀 엔드포인트 추가하기

스프링 데이터 REST는 스프링 데이터 리포지토리의 CRUD 작업을 수행하는 엔드포인트를 자동으로 생성한다. 그런데 우리는 기본적인 CRUD API 이외에 커스텀한 API를 생성하고 싶을때가 있다.

이 때, @RestController 애노테이션이 지정된 컨트롤러에 커스텀하게 API를 구현하여 스프링 데이터 REST가 자동 생성하는 엔드포인트에 보충할 수도 있다. 그러나 이렇게 커스텀하게 구현하면 다음과 같은 문제점이 발생한다.

- `@RestController`에서 구현한 엔드포인트는 스프링 데이터 REST의 **기본 경로(/api)**로 매핑되지 않는다.
	- 즉, @RestController에 기본 경로까지 달아주어야 한다.
	- 기본 경로가 바뀌면 커스텀 엔드포인트도 모두 바꿔야하는 매우 귀찮은 작업이 생긴다.
- 우리가 정의한 커스텀 엔드포인트는 스프링 데이터 REST 엔드포인트에서 반환되는 리소스의 하이퍼링크에 자동으로 포함되지 않는다.

### @RepositoryRestController - 커스텀 엔드포인트 기본경로 해결

스프링 데이터 REST는 @RepositoryRestController를 포함한다. 이것은 스프링 데이터 REST 엔드포인트에 구성되는 것과 동일한 기본 경로로 매핑되는 컨트롤러 클래스에 지정하는 애노테이션이다.

즉, 우리가 커스텀한 API를 만들 컨트롤러에 @RepositoryRestController 애노테이션만 부착하면 스프링 데이터 REST의 기본 경로가 동일하게 적용된다는 것이다.

앞서 말했던 최신순으로 타코 리스트를 정렬해서 가져오는 API는 다음과 같이 커스텀하게 구현할 수 있다.

```java
@RepositoryRestController
public class RecentTacosController {

  private TacoRepository tacoRepo;

  public RecentTacosController(TacoRepository tacoRepo) {
    this.tacoRepo = tacoRepo;
  }

  @GetMapping(path="/tacos/recent", produces="application/hal+json")
  public ResponseEntity<Resources<TacoResource>> recentTacos() {
    PageRequest page = PageRequest.of(
                          0, 12, Sort.by("createdAt").descending());
    List<Taco> tacos = tacoRepo.findAll(page).getContent();

    List<TacoResource> tacoResources = 
        new TacoResourceAssembler().toResources(tacos);
    Resources<TacoResource> recentResources = 
            new Resources<TacoResource>(tacoResources);
    
    recentResources.add(
        linkTo(methodOn(RecentTacosController.class).recentTacos())
            .withRel("recents"));
    return new ResponseEntity<>(recentResources, HttpStatus.OK);
  }

}
```

이렇게 구현한 엔드포인트는 `"localhost:8080/api/tacos/recent"`가 될 것이다.

## 6.3.4 커스텀 하이퍼링크를 스프링 데이터 엔드포인트에 추가하기

최근에 생성된 타코의 엔드포인트가 `/api/tacos`에서 반환된 하이퍼링크 중에 없다면 클라이언트가 가장 최근 타코들을 가져오는 방법을 어떻게 알 수 있을까? 페이징과 정렬 매개변수를 사용하거나 엔드포인트를 하드코딩 해야하는 문제점이 발생할 것이다.

이러한 문제점을 해결할 수 있는 방법은 리소스 프로세서 빈을 생성하는 것이다. 리소스 프로세서 빈을 생성하면 스프링 데이터 REST가 자동으로 포함시키는 링크리스트에 해당 링크를 추가할 수 있다.

스프링 데이터 HATEOAS는 ResourceProcessor를 제공하는데, 이것은 API를 통해서 리소스가 반환되기 전에 리소스를 조작하는 인터페이스이다.

여기서 PagedResources<Resource<Taco>> 타입의 리소스에 recents 링크를 추가하는 ResourceProcessor를 구현해야 한다. 구현하는 코드는 다음과 같다.

```java
  @Bean
  public ResourceProcessor<PagedResources<Resource<Taco>>>
    tacoProcessor(EntityLinks links) {

    return new ResourceProcessor<PagedResources<Resource<Taco>>>() {
      @Override
      public PagedResources<Resource<Taco>> process(
                          PagedResources<Resource<Taco>> resource) {
        //recents 링크 추가
        resource.add(
            links.linkFor(Taco.class)
                 .slash("recent")
                 .withRel("recents"));
        return resource;
      }
    };
  }
```