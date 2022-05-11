# 2.1 정보 보여주기

## 2.1.1 도메인 설정하기

> `도메인` : 애플리케이션의 이해에 필요한 개념을 다루는 영역으로, 간단히 말하면 해결하고자 하는 문제의 영역을 말한다. 소프트웨어 입장에서 다시 해석하면 개발하고자 하는 소프트웨어의 요구사항, 문제영역 정도로 생각할 수 있다. 예를들어 쇼핑몰을 만든다고 했을 때, 게시글, 댓글, 결제, 정산 등을 도메인이라고 할 수 있다.

이러한 도메인의 개념으로 보았을 때, 타코 클라우드 애플리케이션의 도메인에는 다음과 같은 객체가 포함된다. 

- 고객이 선택한 타코 디자인
- 디자인을 구성하는 식자재
- 고객
- 고객의 타코 주문

#### 타코 식자재 정의

```java
@Data
@RequiredArgsConstructor
public class Ingredient {

    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

`@Data`와 `@RequiredArgsConstructor`는 롬복(Lombok) 라이브러리를 사용해서 우리가 원하는 메서드들을 자동으로 만들어주는 애노테이션이다. 각각의 애노테이션이 만들어주는 메서드들은 다음과 같다.

#### @Data

- 게터(Getter), 세터(Setter)
- toString() 재정의
- equals and hascode 재정의

#### @RequiredArgsConstructor

- `final` 키워드가 불어있는 필드 속성(Property)들에 대한 `생성자`를 자동으로 생성해준다.
- 생성자가 `@RequiredArgsConstructor`에 의해서 생성되었으니 기본생성자가 필요하면 따로 생성해줘야 한다.(@NoArgsConstructor)

```java
public Ingredient(String id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
}
```

## 2.1.2 컨트롤러 클래스 생성하기

### Controller

컨트롤러는 `스프링 MVC 프레임워크`의 중심적인 역할을 수행한다.

- **HTTP 요청** 처리
- 브라우저에 보여줄 HTML을 뷰에 요청
- REST 형태에 응답 메시지 바디에 데이터 추가

[스프링 MVC 동작원리](https://cokeprogrammer.tistory.com/25)

이제 타코 식자재 데이터 폼을 불러오는 간단한 컨트롤러를 만들어보자

**DesignTacoController.java**

```java
@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        // TODO : put ingredients to model that are filtered by type
        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors){
        if(errors.hasErrors()){
            return "design";
        }

        // TODO : Save taco design -> 3장에서 계속

        log.info("Processing design: " + design);

        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
    }
}
```

우선적으로 클래스레벨에 붙어있는 애노테이션이 하는 역할을 알아보자.

**@Slf4j**

- 롬복(Lombok)이 제공하는 `Logger`를 사용할 수 있게 해주는 애노테이션이다.
- log.info(), log.error() 과 같은 로그를 남길 수 있는 메서드들을 사용할 수 있다.

**@Controller**

- 해당 클래스가 컨트롤러 클래스임을 나타내는 애노테이션이다.
- `@Controller`에는 `@Component`가 내장되어 있어서 스프링이 **컴포넌트스캔**으로 해당 클래스를 빈으로 자동 등록한다.

**@RequestMapping**

- 해당 컨트롤러가 처리하는 요청의 종류를 나타낸다.

`@RequestMapping`에는 많은 속성들을 지정할 수 있는데 다음과 같다.

- value : 해당 컨트롤러(메서드)가 처리하는 **URL**을 지정
- path : 해당 컨트롤러(메서드)가 처리하는 **pathname**을 지정
- params : 해당 컨트롤러(메서드)가 처리하는 **쿼리파라미터**를 지정
- method : 해당 컨트롤러(메서드)가 처리할 수 있는 **HTTP Method**를 지정
- consumes : 해당 컨트롤러(메서드)가 처리할 수 있는 **Content-Type**을 지정

[@RequestMapping 부가 설명](https://cokeprogrammer.tistory.com/26)

## 2.1.3 뷰 디자인하기

`Thymeleaf`를 사용해서 타코 디자인 페이지를 디자인 해보자. 우선적으로 Thymeleaf를 html파일에서 사용하려면 의존관계를 pom.xml에 설정해줘야 하는것도 있지만, `html태그`에 `xmlns:th="http://www.thymeleaf.org"` 을 추가해야 한다.

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Taco Cloud</title>
    <link rel="stylesheet" th:href="@{/styles.css}"/>
</head>
<body>
    <h1>Design your taco!</h1>
    <!-- thymeleaf url 표준은 @{...} -->
    <img th:src="@{/images/TacoCloud.png}"/>

    <form method="post" th:object="${taco}">
        <!-- TODO : Validate ingredients field error -->
        <span class="validationError"
            th:if="${#fields.hasErrors('ingredients')}"
            th:errors="*{ingredients}">Ingredient Error</span>

        <div class="grid">
            <div class="ingredient-group" id="wraps">
                <h3>Designate your wrap:</h3>
                <div th:each="ingredient : ${wrap}">
                    <input name="ingredients" type="checkbox" th:value="${ingredient.id}"/>
                    <span th:text="${ingredient.name}">INGREDIENT</span><br/>
                </div>
            </div>

            <div class="ingredient-group" id="proteins">
                <h3>Pick your protein:</h3>
                <div th:each="ingredient : ${protein}">
                    <input name="ingredients" type="checkbox" th:value="${ingredient.id}"/>
                    <span th:text="${ingredient.name}">INGREDIENT</span><br/>
                </div>
            </div>

            <div class="ingredient-group" id="cheeses">
                <h3>Choose your cheese:</h3>
                <div th:each="ingredient : ${cheese}">
                    <input name="ingredients" type="checkbox" th:value="${ingredient.id}"/>
                    <span th:text="${ingredient.name}">INGREDIENT</span><br/>
                </div>
            </div>

            <div class="ingredient-group" id="veggies">
                <h3>Determine your veggies:</h3>
                <div th:each="ingredient : ${veggies}">
                    <input name="ingredients" type="checkbox" th:value="${ingredient.id}"/>
                    <span th:text="${ingredient.name}">INGREDIENT</span><br/>
                </div>
            </div>

            <div class="ingredient-group" id="sauces">
                <h3>Select your sauce:</h3>
                <div th:each="ingredient : ${sauce}">
                    <input name="ingredients" type="checkbox" th:value="${ingredient.id}"/>
                    <span th:text="${ingredient.name}">INGREDIENT</span><br/>
                </div>
            </div>
        </div>

        <div>
            <h3>Name your taco creation:</h3>
            <input type="text" th:field="*{name}"/>
            <span th:text="${#fields.hasErrors('name')}">XXX</span>
            <span class="validationError"
                  th:if="${#fields.hasErrors('name')}"
                  th:errors="*{name}">Name Error</span>
            <br/>

            <button>Submit your taco</button>
        </div>
    </form>
</body>
</html>
```

Thymeleaf 문법을 사용한 코드들을 하나씩 살펴보자.

- Variable Expressions(변수 표현식): ${...}
	- 모델에 담은 변수명을 담아서 표현할 수 있는 변수표현식이다.
	- ${#...} 과 같이 쓰면 해당 파일에서 쓸 수 있는 모든 라이브러리를 쓸 수 있다.
- Selection Variable Expressions(선택변수 표현식): *{...}
	- 상위 태그에 선언되어있는 변수에 대한 메서드를 바로 표현할 수 있는 변수표현식이다.
	- 순수 Java코드의 static import 한 것과 비슷하다.
	- 아래 두개의 thymeleaf 코드는 동일하게 동작한다.

```html
<input type="text" th:field="*{name}"/>
<input type="text" th:field="${taco.name}"/>
```

- Link URL Expressions: @{...}
	- 리소스를 찾는 URL이라면 static폴더 부터 시작해서 경로표현식을 사용한다.
	-  그 이외의 href 링크는 다음과 같다.

```html
<!-- /hello -->
<li><a th:href="@{/hello}">basic url</a></li>
<!-- /hello?param1=xxx&param2=yyy -->
<li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>
<!-- /hello/xxx/yyy -->
<li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
<!-- /hello/xxx?param2=yyy -->
<li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
```