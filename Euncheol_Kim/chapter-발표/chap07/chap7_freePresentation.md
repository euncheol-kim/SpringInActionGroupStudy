

# 1. 하이퍼미디어 사용하기

> hatoas
>
> - 서버가 클라이언트에게 하이퍼 미디어를 통해 정보를 동적으로 제공해주는 것이다.
> - 리소스의 상태에 따라 링크 정보가 바뀌며 동적으로 리소스를 구성한다.
>
> 
>
> [hatoas의 등장배경]
>
> - 운영되던 URL이 바뀐다면 클라이언트의 URL도 모두 수정해줘야하는 번거로움이 생긴다.
> - 이를 JSON을 활용해 클라이언트에게 리소스 전달시, 
>   관련있는 하이퍼 링크를 삽입하여 동적인 URL을 받게한다.
> - 이러한 방식은 API 관리에 수월하다.

<br>

# 2. 하이퍼링크 추가하기 [실습코드 적용]

> [리스트 6.4] 리소스에 하이퍼링크 추가하기
>
> - hatoas변경사항
> - 교재코드 (하드코딩 / 하드코딩의 개선 포함)
> - 현재 지원하는 메소드로 변경한 내용 (하드코딩 / 하드 코딩의 개선 포함)
> - 하드코딩 / 하드 코딩의 개선 포함 설명

<br>

## [1] hateoas 변경사항

> ### hateoas 변경사항
>
> ![image](https://user-images.githubusercontent.com/72078208/174451300-51bb5dff-033e-41ae-b80f-de61a2aae9ae.png)
>
>
> ![image](https://user-images.githubusercontent.com/72078208/174451323-8624fbdb-b63c-4121-84ea-dc17ed35fa39.png)
>
>
> ![image](https://user-images.githubusercontent.com/72078208/174451358-c4591c2b-46b7-4bc9-b25f-49381decebf9.png)

<br>

## [2] 소스코드

### **[교재내용]**

- **tacos/web/api/DesignTacoController.java**

```java
    @GetMapping("/recent")
    public Resources<Resource<Taco>> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
		
        // 하드 코딩
        recentResources.add( new Link("http://localhost:8080/design/recent", "recents"));
        
        //하드 코딩의 개선
        recentResources.add(
                ControllerLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );
        
        return recentResources;
    }
```

### **[현재 지원하는 메소드로 변경한 내용]** 

- **tacos/web/api/DesignTacoController.java**

```java
    @GetMapping("/recent")
    public CollectionModel<EntityModel<Taco>> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(tacos);
		
        // 하드코딩
        recentResources.add( new Link("http://localhost:8080/design/recent", "recents"));
        
        //하드 코딩의 개선
        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );
        
        
        return recentResources;
    }
```

## [3] 소스코드 설명 [변경된 코드로 설명]

<br>

### 1. PageRequest valiable = PageRequests.of(int page, int size, Sort sort)

> [참고자료] :<a href="https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageRequest.html" target="_blank"> API </a>
>
> `int page` 에 들어가는 페이지 번호부터, `int size` 페이지당 데이터 수를 반환합니다. `Sort sort`는 생략

<br>

### 2. class CollectionModel\<T>  &  class EntityModel\<T> & CollectionModel.wrap(Iterable\<S> content) 

> **[spring hateoas reference]** : <a href = "https://docs.spring.io/spring-hateoas/docs/2.0.0-SNAPSHOT/reference/html/" target="_blank" >hateoas reference</a>
> **[CollectionModel\<T> API]** : <a href="https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/CollectionModel.html" target="_blank"> CollectionModel\<T> API</a>
> **[EntityModel\<T> API]** : <a href="https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/EntityModel.html" target="_blank"> EntityModel\<T> API </a>
>
> <br>
> ![image](https://user-images.githubusercontent.com/72078208/174452132-a430d9f9-ce14-45e9-9f50-6f86f0b16194.png)
>
> <br>
>
>
> ```java
> CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(tacos);
> ```
>
> - CollectionModel.wrap()을 이용해서 recentTacos()의 반환 타입의 인스턴스로 타코 리스트를 래핑
> - 즉, CollectionModel의 객체를 만든다.



### 3. RepresentationModel.add(Iterable\<Link> links)

>  [참고자료] : <a href = "https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/RepresentationModel.html" target="_blank">API</a>
>
> - 주어진 모든 링크를 리소스에 추가한다.
>
> - ```java
>   recentResources.add( new Link("http://localhost:8080/design/recent", "recents"));
>   ```
>
> - 즉, 위 코드에서 new Link를 통해 이름이 두 번째 인자이고, URL이 첫번째 인자와 같은 링크를 추가한다.
>
> - 추가된 것은 아래와 같다.
>
> - ```json
>   "_links" : {
>   	"recents" : {
>   		"href": "http://localhost:8080/design/recent"
>   	}
>   }
>   ```

<br>

### 4. class Link

>[참고자료] : <a href="https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/Link.html" target="_blank" >API</a>
>
>![image](https://user-images.githubusercontent.com/72078208/174452376-e84132ee-951b-44ff-943c-ec6be31454fb.png)
>
>- ![image](https://user-images.githubusercontent.com/72078208/174452413-1b24980f-0cc8-469b-aec1-3265cff0832b.png)

<br>

## [4] 하드코딩의 개선

### 1. 첫 번째 개선

```java
        // 하드코딩
        recentResources.add( new Link("http://localhost:8080/design/recent", "recents"));
        
        //하드 코딩의 개선
        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );
```

> [참고 자료] : <a href = "https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/server/mvc/WebMvcLinkBuilder.html"> WebMvcLinkBuilder API </a>, <a href="https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/server/core/LinkBuilderSupport.html"> LinkBuilderSupport API</a>
>
> **WebMvcLinkBuilder를 이용한다**.
>
> - WebMvcLinkBuilder를 이용하면 하드코딩을 개선할 수 있다.
>   1. **linkTo()** : public static **WebMvcLinkBuilder** linkTo(Method method)
>   2. **slash()** : Adds the given object's [`String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html?is-external=true) representation as sub-resource to the current URI.
>      - 문자열 표현을 현재 URI에 하위 자원으로 추가한다.
>   3. **withRel()** : Creates the [`Link`](https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/Link.html) built by the current builder instance with the default self link relation.
>      - 현재 인스턴스에 해당하는 Link built를 만든다. (오역일 수 있습니다. 영어 못해서요 ...)
>
> <br>
>
> **[책의 내용 설명]** 
>
> WebMvcLinkBuilder는 컨트롤러의 기본 경로를 사용해서 Link 객체를 생성한다.
> .slash(인자) 를 이용해서 URL에 추가한다. 따라서 URL 경로는 /design/recent가 된다.
>
> ```json
> "_links" : {
> 	"recents" : {
> 		"href": "http://localhost:8080/design/recent"
> 	}
> }
> ```

<br>

### 2. 두 번째 개선 (방법을 찾지 못함)

<br>

# 3. 리소스 어셈블러 생성

> 실습에서 진행해야하는 것
>
> - 리스트에 포함된 각 타코 리소스에 대한 링크를 추가해야한다. [210페이지 참고]
>
> <br>
> <br>
>
>  **[전략]** 
> CollectionModel.wrap()에서 리스트의 각 타코를 생성하는 방법이아니라,
>
> Taco 객체를 새로운 TacoResource 객체로 변환하는 유틸리티 클래스를 정의할 것이다.
>
> <br>
>
>  **[업데이트 후 변경 사항]**
> 업데이트 이후, extends가 `ResourceSupport`에서 `RepresentatinModel`로 변경되었습니다.
>
> 코드는 바뀐 것으로 작성하겠습니다.



## [1] TacoResource의 생성

- **tacos/web/api/TacoResource.java**

```java
package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import tacos.Ingredient;
import tacos.Taco;

import java.util.Date;
import java.util.List;

public class TacoResource extends RepresentationModel<TacoResource> {
    @Getter
    private final String name;

    @Getter
    private final Date createdAt;

    @Getter
    private final List<Ingredient> ingredients;

    public TacoResource(Taco taco) {
        this.name = taco.getName();
        this.createdAt = taco.getCreatedAt();
        this.ingredients = taco.getIngredients();
    }
}

```

> Taco 객체와 유사하지만 TacoResource 객체는 링크를 추가로 가질 수 있습니다.
>
> - 링크를 추가로 가질 수 있는 것은 extends `RepresentationModel`덕분이다.
> - 즉, Link 객체 리스트와 이것을 관리하는 메소드 또한 상속받게 된다.

<br>

### 1. class RepresentationModel\<T>

> [참고자료] : <A href="https://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/RepresentationModel.html" target="_blank">API</a>
>
> - API 설명 : DTO가 링크를 수집하기 위한 기본 클래스
> - DTO란, 계층 간 데이터 교환을 하기 위해 사용하는 객체로 
>   DTO는 로직을 가지지 않는 순수한 객체만을 가짐
> - 즉, json에 입력될 것들

<br>

### 2. TacoResource 클래스 특징

> [TacoResource 클래스 특징]
>
> 1. id 속성을 가지지 않는다. -> Database에서만 필요한 id를 노출할 이유가 없다.
> 2. API 클라이언트 관점에서는 해당 리소스의 self 링크가 리소스 식별자 역할을 한다.
> 3. 해당 클래스는 생성자를 통해, 자신의 속성을 복사한다. 즉, Taco객체를 TacoResource로 쉽게 변환하는 것이다.
>
> 
>
> Taco객체를 TacoResource로 변환하기 위해 반복문이 필요하게 되는데,
> 이를 해결하기 위해 우리는 어셈블러 클래스를 생성한다.

<br>

## [2] Resource Assembler class 생성

### 1. 교재내용

- **tacos/web/api/TacoResourceAssembler.java**

```java
package tacos.web.api;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import tacos.Taco;

public class TacoResourceAssembler
       extends ResourceAssemblerSupport<Taco, TacoResource> {

  public TacoResourceAssembler() {
    super(DesignTacoController.class, TacoResource.class);
  }
  
  @Override
  protected TacoResource instantiateResource(Taco taco) {
    return new TacoResource(taco);
  }

  @Override
  public TacoResource toResource(Taco taco) {
    return createResourceWithId(taco.getId(), taco);
  }

}
```

<br>

### 2. 현재 코드로 바꾼 내용

- **tacos/web/api/TacoResourceAssembler.java**

```java
package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import tacos.Taco;
import tacos.web.DesignTacoController;

public class TacoResourceAssembler extends RepresentationModelAssemblerSupport<Taco, TacoResource>{

    public TacoResourceAssembler() {
        super(DesignTacoController.class, TacoResource.class);
    }

    @Override
    protected TacoResource instantiateModel(Taco taco) {
        return new TacoResource(taco);
    }

    @Override
    public TacoResource toModel(Taco taco) {
        return createModelWithId(taco.getId(), taco);
    }


}
```

#### 상속 생성자의 호출

> ```java
> public TacoResourceAssembler() {
>         super(DesignTacoController.class, TacoResource.class);
>     }
> ```
>
> - 상속 생성자를 호출해서 TacoResource를 생성하면서 만들어지는 링크에 포함되는 URL의 기본 경로를 결정하기 위해 DesignTacoController를 사용한다.

#### protected TacoResource instantiateModel(Taco taco)

> - 인자로 전달된 taco 객체로 TacoResource 인스턴스를 생성하도록 오버라이드한다.
> - Taco 객체로 TacoResource 인스턴스를 생성해야하기 때문에 오버라이드 해야한다.
> - 만약, TacoResource가 기본인자로 갖고 있다면 이 메소드는 생략이 가능하다.

#### public TacoResource toModel(Taco taco)

> `extends RepresentationModelAssemblerSupport`를 상속 받을 때 반드시 오버라이딩 해야한다. (필수)
>
> ```java
>     public TacoResource toModel(Taco taco) {
>         return createModelWithId(taco.getId(), taco);
>     }
> ```
>
> - Taco객체로 TcoResource 인스턴스를 생성하면서, Taco 객체의 id 속성 값으로 생성되는 self링크가 URL에 자동 지정된다.

<br>

<br>

### 3. web/api/DesigntacoController.java에 적용하기

- **변경 전**

```java
    @GetMapping("/recent")
    public CollectionModel<EntityModel<Taco>> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(tacos);
		
        // 하드코딩
        recentResources.add( new Link("http://localhost:8080/design/recent", "recents"));
        
        //하드 코딩의 개선
        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );
        
        
        return recentResources;
    }
```

- **변경 후**

```java
    @GetMapping("/recent")
    public CollectionModel<TacoResource> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());


        List<Taco> tacos = tacoRepo.findAll(page).getContent(); // 타코가져옴 그리고 리스트에 저장
        List<TacoResource> tacoResources = (List<TacoResource>) new TacoResourceAssembler().toModel((Taco) tacos); // 전달받음
        CollectionModel<TacoResource> recentResources = new CollectionModel<TacoResource>(tacoResources);
        
        
        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );

        return recentResources;
    }
```

<br>

<br>

# 4. embedded 관계 이름짓기

```json
{
	"_embedded" : {
		"tacoResourceList" : [ ... ]
	}
}
```

<br>

> tacoResourceList의 이름
>
> - 이 이름은 Resources 객체가 List\<TacoResource>로부터 생성되었다는 것을 나타낸다.
> - 만약 TacoResource 클래스의 이림이 변경된다면 Json의 필드도 변경이 될 것이다.
> - 따라서 변경 전의 이름을 사용하는 클라이언트 코드가 제대로 실행되지 않을 것이다.
>
> 
>
>
> 위와 같은 상황의 결합도를 낮추는 방법
>
> - @Relation 어노테이션을 사용한다.
> - 위의 상황을 다시 빌려와서 TacoResource에 @Relation을 추가하면 스프링 HATEOAS가 결과 JSON의 필드 이름을 짓는 방법을 지정할 수 있다.
>
> - ```java
>   @Relation(value = "taco", collectionRelation="tacos")
>   public class TacoResource extends RepresentationModelAssemblerSupport {...}
>   ```
>
> - 여기서 TacoResource객체 리스트가 CollectionModel 객체에 사용될 때 tacos라는 이름이 지정
>
> - 그리고 JSON에서는 TacoResource 객체가 taco로 참조된다.

<br><br>