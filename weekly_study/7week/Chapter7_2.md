# 7.2 Traverson으로 REST API 사용하기

### Traverson의 특징

- **스프링 데이터 HATEOAS**에 같이 제공된다.
- 스프링 애플리케이션에서 **하이퍼미디어 API**를 사용할 수 있다.
- `돌아다닌다(traverse on)`의 의미로 붙여진 이름으로, **원하는 API를 이동하며 사용한다**는 의미로 붙여졌다.
- API에 리소스를 쓰거나 삭제하는 메서드를 지원하지 않는다. 즉, **읽기만 가능**하다.

### Traverson 기초 설정

Traverson을 사용할 때는 다음과 같이 우선 해당 API의 기본 URI를 갖는 객체를 생성해야 한다.

```java
Traverson traverson = new Traverson(
        URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
```

- 첫 번째 인자에는 각 링크의 **관계 이름**에 해당하는 URI 객체를 넣는다.
- 두 번째 인자에는 API가 반환하는 **미디어 타입을 지정**할 수 있다. 미디어 타입을 명시적으로 지정하지 않으면 메시지 컨버터가 적합한 타입으로 변환해주어 반환한다. (코드를 좀 뜯어본 결과 String 또는 JSON 타입이 기본 컨버터에 들어가 있다.)

Traverson을 어디서든 필요할 때는 다음과 같이 스프링 컨테이너에 빈으로 등록해주면 사용할 수 있다.

```java
@Bean
public Traverson traverson() {
  Traverson traverson = new Traverson(
      URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
  return traverson;
}
```

### Traverson 사용 예시

#### 1. 모든 식자재 리스트 가져오기

다음 소스코드는 모든 식자재 리스트를 가져오는 REST API를 설계한 코드이다. 이 API에서 명시하는 자원의 위치는 `http://localhost:8080/api/ingredients/{ingredientType}` 이 될 것이다. 또한 자바에서 제네릭은 **원소 타입을 컴파일 타임에만 검사하고 그 이후 런타임에는 해당 타입의 정보를 알 수 없기 때문에** `ParameterizedTypeReference`를 생성하여 리소스 타입을 지정해준다.

```java
  public Iterable<Ingredient> getAllIngredientsWithTraverson() {
    ParameterizedTypeReference<Resources<Ingredient>> ingredientType =
        new ParameterizedTypeReference<Resources<Ingredient>>() {};

    Resources<Ingredient> ingredientRes =
        traverson
          .follow("ingredients")
          .toObject(ingredientType);
    
    Collection<Ingredient> ingredients = ingredientRes.getContent();
          
    return ingredients;
  }
```

#### 2. 가장 최근에 생성된 타코들을 가져오기

다음 소스코드는 가장 최근에 생성된 타코들을 가져오는 REST API를 설계한 코드이다. 이 API에서 명시하는 자원의 위치는 `http://localhost:8080/api/tacos/recents/{tacoType}` 이 될 것이다.

```java
public Iterable<Taco> getRecentTacosWithTraverson() {
    ParameterizedTypeReference<Resources<Taco>> tacoType =
        new ParameterizedTypeReference<Resources<Taco>>() {};

    Resources<Taco> tacoRes =
        traverson
          .follow("tacos")
          .follow("recents")
          .toObject(tacoType);

    return tacoRes.getContent();
}
```

또한 두 개 이상의 관계 이름들이 존재한다면 다음과 같이 follow() 메서드에 인자를 여러개 주어 표현할 수 있다.

```java
public Iterable<Taco> getRecentTacosWithTraverson() {
    ParameterizedTypeReference<Resources<Taco>> tacoType =
        new ParameterizedTypeReference<Resources<Taco>>() {};

    Resources<Taco> tacoRes =
        traverson
          .follow("tacos", "recents")
          .toObject(tacoType);

    return tacoRes.getContent();
  }
```

### 정리

이렇게 Traverson을 사용하면 HATEOAS가 활성화된 API를 이용하면서 해당 API의 리소스를 쉽게 가져올 수 있다. 하지만 Traverson은 리소스를 **쓰거나 삭제하는 메서드를 제공하지 않는다.**

따라서 우리는 API의 이동 뿐만아니라 리소스의 변경 또는 삭제를 해야할 때 `RestTemplate`과 `Traverson`을 함께 사용해야 한다. 두 개의 기술을 같이 사용하는 예시를 들어보면 다음과 같다.

다음의 코드의 동작 과정은 다음과 같다.

1. Traverson을 이용해서 식자재를 추가할 수 있는 API로 이동한다.
2. RestTemplate을 이용해서 Traverson 으로부터 얻어온 링크를 지정하고 POST 방식으로 식자재를 추가한다.

```java
public Ingredient addIngredient(Ingredient ingredient) {
    String ingredientsUrl = traverson
        .follow("ingredients")
        .asLink()
        .getHref();
    return rest.postForObject(ingredientsUrl,
                              ingredient,
                              Ingredient.class);
}
```