# Chapter6.2 하이퍼미디어 사용하기

# Hateoas

> 기존의 API 응답 방법에서 만약 특정 URL에 대한 요청을 했는데, 해당 URL이 바뀌었다면 404 NotFound를 반환하기 때문에 이를 동적으로 Hypermedia 링크를 삽입된 응답을 반환하기 위한 메커니즘이 Hateoas 이다.  

**Hateoas**(Hypermedia As The Engine Of Application State)는 **Hypermedia를 Application의 상태를 관리하기 위해 도입된 개념**. 즉 , 클라이언트는 서버와 동적으로 상호작용이 가능하도록 해야함.  ⇒ **요청에 필요한 URI를 응답에 포함시켜 반환**
> 

## RestAPI 구현레벨

![Untitled](https://user-images.githubusercontent.com/45655434/174455306-7d6087d3-5a95-4fb9-ad90-28d2e3dc7b82.png)

RestAPI에도 구현 레벨이 있는데 **Hateoas 는 마지막 Level3** 의 단계이다. [[참고링크](https://velog.io/@younge/REST-API-%EC%84%B1%EC%88%99%EB%8F%84-%EB%AA%A8%EB%8D%B8-Maturity-Model-eqqyjqff)]

### 의존성 추가하기

```java
implementation 'org.springframework.boot:spring-boot-starter-hateoas'
```

**참고)** **스프링부트 2.2 이상 부터는 기존 Resource 에서 EntityModel로 바뀌었음**

| 이전버전 | 2.2이후 |
| --- | --- |
| ResourceSupport | RepresentationalModel |
| Resource | EntityModel |
| Resource | CollectionModel |
| PagedResources | PagedModel |
| ResourceAssembler | RepresentationalModelAssembler |
| ControllerLinkBuilder | WebMvcLinkBuilder |

## 리소스에 하이퍼링크 추가하기

> 반환타입이 List 타입이므로 **CollectionModel.of()** 메서드를 이용해서 반환한다.

**linkTo** 메서드 안에 특정 Controller를 지정함으로써 해당 Controller의 요청에 대한 삽입할 Link를 생성할 수 있다. ⇒ URI 가 변경되어도 변경된 URI에 대한 링크가 반환이 된다.

linkTo 안에 methodOn을 이용해서 특정 메서드를 지정할 수도 있음
> 

**DesignTacoApiController.class**

```java
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/design",produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignTacoApiController {

    private final TacoRepository tacoRepository;

    @GetMapping("/recent")
    public CollectionModel<Taco> recentTacos(){
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();
        CollectionModel<Taco> recentResources = CollectionModel.of(tacos);

        Link link = linkTo(methodOn(DesignTacoApiController.class).recentTacos())
                .withRel("recents");
        recentResources.add(link);

        return recentResources;
    }
}
```

```json
{
    "_embedded": {
        "tacoList": [
            {
                "id": 2,
                "createdAt": "2022-06-18T19:07:13.816+00:00",
                "name": "taco123",
                "ingredients": [
                    {
                        "id": "FLTO",
                        "name": "Flour Tortilla",
                        "type": "WRAP"
                    },
                    {
                        "id": "GRBF",
                        "name": "Ground Beef",
                        "type": "PROTEIN"
                    },
                    {
                        "id": "JACK",
                        "name": "Monterrey Jack",
                        "type": "CHEESE"
                    },
                    {
                        "id": "LETC",
                        "name": "Lettuce",
                        "type": "VEGGIES"
                    },
                    {
                        "id": "SLSA",
                        "name": "Salsa",
                        "type": "SAUCE"
                    }
                ]
            }
        ]
    },
    "_links": {
        "recents": {
            "href": "http://localhost:8080/api/design/recent"
        }
    }
}
```

## 리소스 어셈블러

> taco가 만약 여러개라면 반복문을 통해 여러개의 Link 객체를 만들어 삽입해야 하므로 **RepresentationalModelAssembler**를 구현한 Assembler를 만들어서 해결한다.

게다가 TacoList를 보면 id값이 노출되어 있는데, 클라이언트 입장에서는 id값을 알 필요가 없기 때문에 별도의 **RepresentationalModel**을 상속받은 유틸리티 class를 생성하여 _self 링크가 이를 대체하게 한다.
> 

**TacoResources.class**

```java
@Relation(value = "taco",collectionRelation = "tacos")
public class TacoResources extends RepresentationModel<TacoResources> {

    @Getter
    private final String name;

    @Getter
    private final Date createAt;

    @Getter
    private final List<Ingredients> ingredients;

    public TacoResources(Taco taco) {
        this.name=taco.getName();
        this.createAt=taco.getCreatedAt();
        this.ingredients=taco.getIngredients();
    }
}
```

참고로, @Relation 으로 이름을 지정해주면 json안에 임의로 생성된 tacoList 변수명을 다른 변수명으로 지정해 줄 수 있음 

**어셈블러 TacoResourceAssembler.class**

```java
@Component
public class TacoResourceAssembler implements RepresentationModelAssembler<Taco, EntityModel<TacoResources>> {

    @Override
    public EntityModel<TacoResources> toModel(Taco taco) {

        return EntityModel.of(new TacoResources(taco),
                linkTo(methodOn(DesignTacoApiController.class).findTacoById(taco.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<TacoResources>> toCollectionModel(Iterable<? extends Taco> tacos) {
        return RepresentationModelAssembler.super.toCollectionModel(tacos).add(
                        linkTo(DesignTacoApiController.class).withSelfRel(),
                linkTo(methodOn(DesignTacoApiController.class).recentTacos()).withRel("recents")
        );
    }

}
```

단일 객체를 반환하는 toModel 에서는 taco ⇒ tacoResources로 변환하고 EntityModel에 링크를 삽입하여 반환한다.

컬렉션을 반환하는 toCollectionModel 에서는 tacos ⇒ CollectionModel로 변환하는데, 컬렉션으로 반환될 수 있는 모든 경우를 지정하여 각각에 맞는 Link가 삽입될 수 있도록 한다. 

**컨트롤러 DesignTacoApiController.class**

```java
@RestController
@RequestMapping(path = "/api/design",produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignTacoApiController {

    private final TacoRepository tacoRepository;
    private final TacoResourceAssembler assembler;

    @GetMapping("/recent")
    public CollectionModel<EntityModel<TacoResources>> recentTacos(){
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();

        return assembler.toCollectionModel(tacos);
    }

    @GetMapping
    public List<Taco> findAll(){
        return tacoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> findTacoById(@PathVariable Long id){
        Optional<Taco> findTaco = tacoRepository.findById(id);

        return findTaco.map(taco -> new ResponseEntity<>(taco, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
```

컨트롤러에서는 어셈블러를 컴포넌트로 주입받아 사용하면 된다.

```json
{
    "_embedded": {
        "tacos": [
            {
                "name": "taco1",
                "createAt": "2022-06-18T19:31:40.148+00:00",
                "ingredients": [
                    {
                        "id": "FLTO",
                        "name": "Flour Tortilla",
                        "type": "WRAP"
                    },
                    {
                        "id": "GRBF",
                        "name": "Ground Beef",
                        "type": "PROTEIN"
                    },
                    {
                        "id": "CHED",
                        "name": "Cheddar",
                        "type": "CHEESE"
                    },
                    {
                        "id": "TMTO",
                        "name": "Diced Tomatoes",
                        "type": "VEGGIES"
                    },
                    {
                        "id": "SLSA",
                        "name": "Salsa",
                        "type": "SAUCE"
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/design/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/design"
        },
        "recents": {
            "href": "http://localhost:8080/api/design/recent"
        }
    }
}
```

## 객체안의 객체에 링크를 삽입하기

> Taco 안에 Ingredients 객체에 대한 링크도 나타낼 수 있는데 기존 TacoResources라는 유틸리티 클래스에 Ingrerdients의 어셈블러를 넣어줘서 추가적인 링크를 삽입할 수 있다.
> 

```java
@Getter
@Setter
@Relation(value = "ingredient",collectionRelation = "ingredients")
public class IngredientResources extends RepresentationModel<IngredientResources> {

    private String name;
    private Type type;

    public IngredientResources(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
```

```java
@Relation(value = "taco",collectionRelation = "tacos")
public class TacoResources extends RepresentationModel<TacoResources> {

    private static final IngredientResourceAssembler ingredientResourceAssembler = new IngredientResourceAssembler();

    @Getter
    private final String name;

    @Getter
    private final Date createAt;

    @Getter
    private final CollectionModel<EntityModel<IngredientResources>> ingredients;

    public TacoResources(Taco taco) {
        this.name=taco.getName();
        this.createAt=taco.getCreatedAt();
        this.ingredients=ingredientResourceAssembler.toCollectionModel(taco.getIngredients());
    }
}
```

**List 타입의 컬렉션 필드 대신에 CollectionModel을 필드로 대체한다.**

**컨트롤러 IngredientController.class**

```java
@RestController
@RequestMapping(path="/ingredients", produces="application/json")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class IngredientController {

    private IngredientRepository ingredientRepository;

    @GetMapping
    public Iterable<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findIngredientById(@PathVariable String id){
        Optional<Ingredient> findIngredient = ingredientRepository.findById(id);

        return findIngredient.map(ingredient -> new ResponseEntity<>(ingredient, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}
```

**어셈블러 IngredientResourceAssembler.class**

```java
public class IngredientResourceAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<IngredientResources>> {

    @Override
    public EntityModel<IngredientResources> toModel(Ingredient ingredient) {
        return EntityModel.of(new IngredientResources(ingredient),
                linkTo(methodOn(IngredientController.class).findIngredientById(ingredient.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<IngredientResources>> toCollectionModel(Iterable<? extends Ingredient> ingredients) {

        return RepresentationModelAssembler.super.toCollectionModel(ingredients).add(
                linkTo(IngredientController.class).withSelfRel()
        );
    }
}
```

```json
{
    "_embedded": {
        "tacos": [
            {
                "name": "taco2",
                "createAt": "2022-06-18T19:49:45.909+00:00",
                "ingredients": {
                    "_embedded": {
                        "ingredients": [
                            {
                                "name": "Corn Tortilla",
                                "type": "WRAP",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/COTO"
                                    }
                                }
                            },
                            {
                                "name": "Ground Beef",
                                "type": "PROTEIN",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/GRBF"
                                    }
                                }
                            },
                            {
                                "name": "Monterrey Jack",
                                "type": "CHEESE",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/JACK"
                                    }
                                }
                            },
                            {
                                "name": "Diced Tomatoes",
                                "type": "VEGGIES",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/TMTO"
                                    }
                                }
                            },
                            {
                                "name": "Lettuce",
                                "type": "VEGGIES",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/LETC"
                                    }
                                }
                            },
                            {
                                "name": "Salsa",
                                "type": "SAUCE",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/SLSA"
                                    }
                                }
                            },
                            {
                                "name": "Sour Cream",
                                "type": "SAUCE",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/SRCR"
                                    }
                                }
                            }
                        ]
                    },
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/ingredients"
                        }
                    }
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/design/4"
                    }
                }
            },
            {
                "name": "taco1",
                "createAt": "2022-06-18T19:49:33.849+00:00",
                "ingredients": {
                    "_embedded": {
                        "ingredients": [
                            {
                                "name": "Flour Tortilla",
                                "type": "WRAP",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/FLTO"
                                    }
                                }
                            },
                            {
                                "name": "Ground Beef",
                                "type": "PROTEIN",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/GRBF"
                                    }
                                }
                            },
                            {
                                "name": "Carnitas",
                                "type": "PROTEIN",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/CARN"
                                    }
                                }
                            },
                            {
                                "name": "Cheddar",
                                "type": "CHEESE",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/CHED"
                                    }
                                }
                            },
                            {
                                "name": "Diced Tomatoes",
                                "type": "VEGGIES",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/TMTO"
                                    }
                                }
                            },
                            {
                                "name": "Lettuce",
                                "type": "VEGGIES",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/LETC"
                                    }
                                }
                            },
                            {
                                "name": "Salsa",
                                "type": "SAUCE",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/ingredients/SLSA"
                                    }
                                }
                            }
                        ]
                    },
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/ingredients"
                        }
                    }
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/design/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/design"
        },
        "recents": {
            "href": "http://localhost:8080/api/design/recent"
        }
    }
}
```

참고) [[스프링 Hateoas 공식문서](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)]