# Chapter7.1 RestTemplate

# RestTemplate

> **RestTemplate** 은 스프링3.0 이상 부터 지원하는 HTTP 통신,  그 중 **RestAPI 를 기준**으로 요청을 쉽게 할 수 있게 도와주는 **동기방식의 Rest 클라이언트**.
>Hateoas 의 하이퍼 미디어 링크를 삽입할 수 있게 도와주는 Traverson과 함께 사용된다. 
>
>Spring 5.0 부터는 비동기 방식도 지원하는 WebClient를 사용한다.
> 

![Untitled](https://user-images.githubusercontent.com/45655434/175786742-8a70837b-8fa3-4c03-b3b9-c6ef1913f18b.png)


**(서비스 ⇒  other 서비스) 로 필요한 데이터를 받아올 때 사용 하며 예를들어,
받아온 JSON 타입의 데이터를 Jackson2 MessageConverter을 통해 객체에 바인딩을 하여 우리가 쓸 수 있는 Object 형태로 받아 온다.**
&nbsp;

## RestTemplate 동작원리

![Untitled 1](https://user-images.githubusercontent.com/45655434/175786725-0766f308-2032-45c8-9326-85b59b0a602b.png)

**RestTemplate ⇒ HttpMessageConverter ⇒ RequestEntity ⇒ HttpMessageConverter ⇒ ResponseEntity ⇒ Object**

&nbsp;
**참고)** 
**RestTemplate 생성자**

```java
public RestTemplate() {
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(new StringHttpMessageConverter());
.............
.............
.............

if (jackson2Present) {
			this.messageConverters.add(new MappingJackson2HttpMessageConverter());
		}
		else if (gsonPresent) {
			this.messageConverters.add(new GsonHttpMessageConverter());
		}
		else if (jsonbPresent) {
			this.messageConverters.add(new JsonbHttpMessageConverter());
		}
		else if (kotlinSerializationJsonPresent) {
			this.messageConverters.add(new KotlinSerializationJsonHttpMessageConverter());
		}

		if (jackson2SmilePresent) {
			this.messageConverters.add(new MappingJackson2SmileHttpMessageConverter());
		}
		if (jackson2CborPresent) {
			this.messageConverters.add(new MappingJackson2CborHttpMessageConverter());
		}
.............
}
```

&nbsp;
**RestTemplate 메서드**

```java
..............
..............
@Override
	@Nullable
	public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
		RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
		HttpMessageConverterExtractor<T> responseExtractor =
				new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), logger);
		return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
	}

	@Override
	@Nullable
	public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
		HttpMessageConverterExtractor<T> responseExtractor =
				new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), logger);
		return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
	}

	@Override
	@Nullable
	public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
		RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
		HttpMessageConverterExtractor<T> responseExtractor =
				new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), logger);
		return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
	}
..............
..............
```

**RestTemplate 으로 요청을 보내면 필요한 처리들을 백그라운드에서 Callback 메서드들을 호출하여 처리 후 데이터를 받아오는 방식으로 동작.**

&nbsp;
## RestTemplate으로 Rest 엔드포인트 사용하기

> **RestTemplate** 클라이언트는 다른 API에 요청을 하기 위한 12개의 메서드들이 있다. 
파라미터들이 다른 오버로딩 된 메서드들을 합치면 총 41개의 메서드들이 있다.
> 

&nbsp;

| Method | Description | HTTP Method | ResponseType |
| --- | --- | --- | --- |
| delete(…) | HTTP Delete 요청 수행 | Delete | void |
| exchange(…) | HTTP 요청을 수행한 후 ResponseEntity<T>를 반환 | ALL | ResponseEntity<T> |
| excute(…) | HTTP 요청을 수행한 후 Object를 반환 | ALL | Object |
| getFor[Entity/Object] (…) | HTTP Get 요청을 수행한 후 [ResponseEntity / Object] 를 반환 | GET | ResponseEntity<T> / <T> T |
| headForHeaders(…) | HTTP Head 요청을 전송하며 헤더 정보를 반환 | HEAD | HttpHeaders |
| optionsForAllow(…) | HTTP Option 요청을 전송하며 Allow 헤더를 반환 ( HTTP 헤더 정보) | OPTIONS | Set<HttpMethod> |
| patchForObject(…) | HTTP Patch 요청을 전송하며 결과 객체를 반환 | PATCH | <T> T |
| postFor[Entity/Object] (…) | HTTP Post 를 요청을 수행하며 [ResponseEntity / Object] 를 반환 | Post | ResponseEntity<T> / <T> T |
| PostForLocation(…) | HTTP Post 를 수행한 후 생성되는 URI을 반환 | Post | URI |
| Put(…) | HTTP Put 을 수행 | PUT | void |

**기본적으로 HTTP Method 에 맞는 method 들이 하나씩 있으며 , exchange 와 excute 는 더 넓은 범위의 모든 요청을 수행할 수 있는 low 레벨 메서드이다.** 

&nbsp;
**RestTemplate Bean 을 주입받아 사용할 수도 있다.**

### GET 예시

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class TacoCloudClient {

    private final RestTemplate rest;
    private final Traverson traverson;

    //
    // GET examples
    //

    public Ingredient getIngredientById(String ingredientId) {
        return rest.getForObject("http://localhost:8080/ingredients/{id}",
                Ingredient.class, ingredientId);
    }

    // 모든 요청을 수행할 수 있는 메서드 이므로 요청 method 도 같이 파라미터에 지정
    public List<Ingredient> getAllIngredients() {
        return rest.exchange("http://localhost:8080/ingredients",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Ingredient>>() {})
                .getBody();
    }

    // Map 으로 키 지정
    public Ingredient getIngredientById(String ingredientId) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        return rest.getForObject("http://localhost:8080/ingredients/{id}",
                Ingredient.class, urlVariables);
    }

    // URI 객체를 생성하여 전송
    public Ingredient getIngredientById(String ingredientId) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        URI url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/ingredients/{id}")
                .build(urlVariables);
        return rest.getForObject(url, Ingredient.class);
    }

    // header 와 body 정보가 필요할 때 요청 후 ResponseEntity 로 반환
    public Ingredient getIngredientById(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity =
                rest.getForEntity("http://localhost:8080/ingredients/{id}",
                        Ingredient.class, ingredientId);
        log.info("Fetched time: " + responseEntity.getHeaders().getDate());
        return responseEntity.getBody();
    }
}
```

&nbsp;

### Post 예시

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class TacoCloudClient {

    private final RestTemplate rest;
    private final Traverson traverson;

    //
    // POST examples
    //
    public Ingredient createIngredient(Ingredient ingredient) {
        return rest.postForObject("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }

    // 생성된 URI 반환
    public URI createIngredient(Ingredient ingredient) {
        return rest.postForLocation("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity =
                rest.postForEntity("http://localhost:8080/ingredients",
                        ingredient,
                        Ingredient.class);
        log.info("New resource created at " + responseEntity.getHeaders().getLocation());
        return responseEntity.getBody();
    }
}
```

&nbsp;

### Put 예시

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class TacoCloudClient {

  private final RestTemplate rest;
  private final Traverson traverson;

  //
  // PUT examples
  //
  public void updateIngredient(Ingredient ingredient) {
    rest.put("http://localhost:8080/ingredients/{id}",
          ingredient, ingredient.getId());
  }
}
```

&nbsp;

### Delete 예시

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class TacoCloudClient {

  private final RestTemplate rest;
  private final Traverson traverson;

  //
  // DELETE examples
  //

  public void deleteIngredient(Ingredient ingredient) {
    rest.delete("http://localhost:8080/ingredients/{id}",
        ingredient.getId());
  }
}
```

&nbsp;

# RestTemplate의 한계?

> 스프링 5.0 부터는 **WebClient**를 지원하고, 이를 사용하도록 권고하고 있다. 그 동안 비동기 방식은 AsyncRestTemplate이 지원해 주었는데 deprecated되 었다. 
이어서 RestTemplate을 deprecated 될 가능성이 있다고 한다. 그 이유는..?
> 

참고) [동기 vs 비동기 , blocking vs non-blocking](https://velog.io/@nittre/%EB%B8%94%EB%A1%9C%ED%82%B9-Vs.-%EB%85%BC%EB%B8%94%EB%A1%9C%ED%82%B9-%EB%8F%99%EA%B8%B0-Vs.-%EB%B9%84%EB%8F%99%EA%B8%B0) 

| Restemplate | WebClient |
| --- | --- |
| Blocking | Non-Blocking |
| Sync | Async |
| MultiThread | SingleThread |

**RestTemplate 과 WebClient의 Blocking 이나 Non-Blocking 이냐 에 따라 성능차이가 동시 사용자가 많아 질수록 급격하게 차이가 난다고 한다.**

![Untitled 2](https://user-images.githubusercontent.com/45655434/175786731-f7ca0e64-3287-4bcb-8188-99d7b56ab591.png)

> Boot 1은 WebClient , Boot 2 는 SpringMVC의 RestTemplate 이다.

기존 (SpringMVC) Sync-Blocking 방식은 쓰레드 풀이 수용할 수 있는 요청만 동시적으로 처리하고 그 이상은 큐잉되어 대기하게 되는데 이 max-size 를 지속적으로 초과하게 된다면 전체 대기시간이 점점 늘어나게 된다. ( Thread poll hell 현상)

그래서 I/O 작업에 있어서 효율을 낼 수 있는 async & non-blocking 과 event-driven 방식을 통해 처리할 수 있는 webclient를 쓰도록 권장하는 것이다.
> 

참고 ) [Spring Webflux 는 어떻게 적은 리소스로 많은 트래픽을 감당할까?](https://alwayspr.tistory.com/44)