

# 목차

> 1. 웹 애플리케이션 개발하기
>    1. 정보 보여주기
>       1. 컨트롤러
>       2. 뷰
>       3. 타코 클라우드 설계하기
>       4. 도메인의 객체인 식자재 정의
>       5. 타코 디자인을 정의하는 도메인 객체
>       6. 컨트롤러 클래스 생성하기
>          1. 타코 클라우드에서 필요한 컨트롤러
>          2. 요구사항에 부합하는 컨트롤러
>          3. 뷰 디자인하기
>       7. 뷰 디자인하기 (Thymeleaf framework이용함)
>          1. Thymeleaf의 장점
>          2. Thymeleaf의 특징
>          3. Thymeleaf 기본 표현식
>             1. view, Design.html 작성하기
>             2. 나의 삽질 기록
>             3. 의아한 부분
>             4. 의아한 부분 - 시도한 방법과 기록 & 추측
>       8. 폼 제출 처리하기 (제출시 Thymeleaf의 동작 및 바인딩도 포함)
>          1. DesignTacoContoller.java에 POST를 처리할 메소드 기입
>          2. 타코 주문 폼을 나타내는 컨트롤러 (Get + Post)
>          3. 타코 주문 정보를 갖는 도메인 객체
>          4. Order.html 작성하기
>          5. 나의 삽질 기록
>       9. 폼 입력 유효성 검사하기
>          1. 스프링 MVC에 유효성 검사를 적용 방식 & 종속성 설정
>          2. 유효성 검사 API의 애노테이션
>             1. 속성의 값과 null 유무 확인하기
>             2. 카드 지불과 관련한 유효성 검사
>             3. 기타 애노테이션 (교재에서 소개된 애노테이션로 정리)
>          3. Taco.java에 유효성 검사 애노테이션 삽입
>          4. Order.java에 유효성 검사 애노테이션 삽입
>       10. 폼과 바인딩 될 때 유효성 검사의 수행
>           1. 제출된 Taco의 유효성 검사의 수행
>           2. 제출된 Order의 유효성 검사의 수행
>       11. 유효성 검사 에러 보여주기
>       12. 뷰 컨트롤러로 작업하기
>           1. 뷰 컨트롤러 다른식으로 처리하기
>              1. WebMvcConfigurer들여다보기



# 1. 웹 애플리케이션 개발하기

> 스프링 MVC를 더 깊게 이해하며, 모델 데이터를 보여주고 사용자 입력을 처리하는 방법을 안다



# [1] 정보 보여주기

> 기본적으로 타코 클라우드는 온라인으로 타코를 주문할 수 있는 애플리케이션이다.
>
> 1. 타코의 식재료는 고객이 고를 수 있도록 한다.
>    - 개발자는 식자재를 보여주고 선택할 수 있도록 하는 페이지를 설계한다.
>    - 선택할 수 있는 식자재의 내역은 수시로 변경될 수 있다.
>    - 사용 가능한 식자재의 내역은 데이터베이스로부터 가져와서 고객이 볼 수 있도록 해당 페이지에 전달
>      - 해당 파트에서는 데이터 베이스를 다루지는 않는다.





## 1. 컨트롤러

> <u>데이터를 가져오고</u> <u>처리</u>하는 일을 담당한다.





## 2. 뷰

> 브라우저에 데이터를 HTML로 나타내는 일을 담당한다.





## 3. 타코 클라우드 설계하기

> - 타코 식자재의 속성을 정의하는 도메인<sub>domain</sub>클래스
> - 식자재 정보를 가져와서 뷰에 전달하는 스프링 MVC 컨트롤러 클래스
> - 식자재의 내역을 사용자의 브라우저에 보여주는 뷰 템플릿





## 4. 도메인의 객체인 식자재 정의

> **src/main/java/tacos/Ingredient.java**

![image](https://user-images.githubusercontent.com/72078208/172037476-35883eda-20b1-461f-ac61-69a705dd764c.png)

```java
package tacos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

### @Data

>1. 소스코드에 누락된 final 속성들을(필드들을) 초기화하는 생성자를 생성한다.
>   - 사실 @RequireArgsConstructor과 같은 기능인데, 교재에서 왜 명시해주었는지 모르겠음
>
>2. 속성들의 getter와 setter등을 생성하라고 Lombok에 알려준다.
>   - Lombok은 프로젝트 생성시에 종속성을 넣어줬었기에 pom.xml에 설정되어 있다.
>
>
>
>이미지의 왼쪽하단의 `구조`를 확인해보면 getter와 setter를 만들지 않았음에도 선언됨을 알 수 있다.

### @RequireArgsConstructor

>1. 소스코드에 누락된 final  속성들을(필드들을) 초기화하는 생성자를 생성한다.

### enum class

> 나의 포스팅 : <a href="https://kimeuncheol.tistory.com/116" target="_blank"> java :) enum, 열거상수(각각의 객체)를 가지는..</a> 내 블로그





# 5. 타고 디자인을 정의하는 도메인 객체

> **src/main/java/tacos/Taco.java**

```java
package tacos;

import lombok.Data;

import java.util.List;

@Data
public class Taco {
    private String name;
    private List<String> ingredient;
}
```





# 6. 컨트롤러 클래스 생성하기

> 컨트롤러는 스프링 **<u>MVC 프레임워크의 중심적인 역할</u>**을 수행한다.
> 컨트롤러는 **<u>HTTP 요청을 처</u>리**하고, **<u>브라우저에 보여줄 HTML을 뷰에 요청</u>**한다.
> 또는 **<u>REST 형태의 응답 몸체에 직접 데이터를 추가</u>**한다.



## 1 ] 타코 클라우드에서 필요한 컨트롤러

> 1. 요청 경로가 /design인 HTTP GET요청을 처리한다.
> 2. 식자재의 내역을 생성한다.
> 3. 식자재 데이터(식자재 내역)의 HTML 작성을 뷰 텔플릿에 요청하고, 작성된 HTML을 웹 브라우저에 전송한다.



## 2 ] 요구사항에 부합하는 컨트롤러

> **src/main/java/tacos/web/DesiginController.java**

```java
package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j // 자바에서 사용하는 Simple Logging Facade Logger를 생성한다.
@Controller // 스프링의 컨트롤러로 식별하게 하여, 스프링 컨텍스트 빈으로 DesignController의 인스턴스를 생성한다.
@RequestMapping("/design")
public class DesignTacoController {
    @GetMapping
    public String showDesignForm(Model model) { // Model -> Model은 컨트롤러와 데이터를 보여주는 뷰 사이에서 데이터를 운반하는 객체이다.
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

        Type[] types = Ingredient.Type.values(); // enum Type의 상수를 배열에 저장한다.

        for(Type type : types){
            // Model.addAttribute -> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/ui/Model.html
            // Model.addAttribute(key, value) -> key와 value형태로 값으로 view에 전달할 수 있다.
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());

        return "desigin";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}

```

#### @Sl4j

>자바에서 사용하는 **SL4J**<SUB>Simple Logging Facade Logger</SUB>를 생성한다.

#### @Controller

> 스프링의 컨트롤러로 식별하게 하여, 스프링 컨텍스트 빈으로 DesignController의 인스턴스를 생성한다.

#### @RequestMapping

> 클래스 수준으로 적용시에 해당 컨트롤러가 처리하는 요청의 종류를 나타낸다.
>
> - 여기서는 DesignTocoController에서 /design으로 시작하는 경로의 요청을 처리한다.

#### @GetMapping

> HTTP GET 요청을 처리한다.
>
>
> **참고하면 좋을 자료** 
>  <a href="https://www.javaguides.net/2018/11/spring-getmapping-postmapping-putmapping-deletemapping-patchmapping.html" target="_blank">Spring @GetMapping, @PostMapping, @PutMapping, @DeleteMapping and @PatchMapping</a>,  Java Guides -> [영문자료, HTTP 방식이 무엇이 있는지 코드와 설명이 같이 나와있다.]

#### 매개변수 Model

> 컨트롤러와 데이터를 보여주는 뷰 사이에서 데이터를 운반하는 객체이다.
>
> - 코드에서 사용된 model관련 메소드<sub>**addArrtibute()**</sub>는 아래 설명되어 있다.

#### enum.values()

> enum의 상수를 가져온다.

#### Model.addAttributekey, value)

>  Model.addAttribute(key, value) 
>
> - key와 value형태로 값으로 view에 전달할 수 있다. 
> - <sup>1</sup> 해당 메소드를 이용한 코드의 설명



####  <sup>1</sup> 코드 Model.addAttribute(key, value)에 대한 조금 상세한 설명 [교재외]

```java
...
 Type[] types = Ingredient.Type.values(); // enum Type의 상수를 배열에 저장한다.

        for(Type type : types){
            // Model.addAttribute -> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/ui/Model.html
            // Model.addAttribute(key, value) -> key와 value형태로 값으로 view에 전달할 수 있다.
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());

        return "Desigin";
    }
...
```

```java
private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
```

> Model은 컨트롤러<sub>controller</sub>와 뷰<sub>view</sub>사이에서 데이터를 옮길때 사용한다.
> 
>
> 코드에서 for문 안에서 model이 한 번, 또 다른 위치에서 한 번 총 두 번 사용이 되어지는 것을 알 수 있다.
> 
>
> Model.addAttribute(key, value)은 key와 value형식으로 모델 객체 안에 저장할 수 있음을 기억한다.
> 따라서, 위의 처리가 진행된다면 아래와 같이 Model객체에 담길것 이다.
> ![도로롱](https://user-images.githubusercontent.com/72078208/172042177-927d9a59-04da-43b5-ad0c-f81434d20d82.jpg)





# 7.  뷰 디자인하기(Thymeleaf framework 이용함)

> **Tymeleaf 내 포스팅** 
>
> - (my_url)
>
> 
>
> **참고자료**
>
> - <a href="https://cyberx.tistory.com/132" href="_blank">thymeleaf (server-side template engine) 사용법 정리 -1</a>, 유용한 정보, tistory [표현식이 잘 정리되어 있음]
> - <a href="https://www.geeksforgeeks.org/spring-boot-how-thymeleaf-works/">Spring Boot - How Thymelaf Works</a>, GeeksforGeeks [th:object내용이 우리의 프로젝트와 거의 동일]
>
> 
>
> Design.html (thymeleaf 사용)
>
> - Thymeleaf를 사용하기 위해서는 종속성을 설정해줘야한다.
> - 종속성과 관련한 설정을 찾으려면 구글에 maven repository 검색해서 이용한다.





## 1 ] Thymeleaf의 장점

> 1. 어떤 웹 프레임워크와도 사용이 가능하다.





## 2 ] Thymeleaf특징

> 1. Thymeleaf는 요청 데이터를 나타내는 요소 속성을 추가로 갖는 html이다.

#### 



## 3 ] Thymeleaf 기본 표현식

> <태그명 th:[속성] = "서버에서 받는 값 및 조건식">
>
> - (예) ```<p th:text="${message}"> placeholder message </p>```
>   - 이 경우는 서버에서 message라는 변수가 있을 경우, placeholder message의 자리를 변수 값으로 바꾼다.
>   - ${...} 연산자는 요청 속성(여기서는 message)의 값을 사용하라는 것이다.





### 1. view, Design.html 작성하기

> **src/main/resources/templates/Design.html**

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="EUC-KR">
    <title>Taco Cloud</title>
    <link rel="stylesheet" th:href="@{/styles.css}" />
</head>

<body>
<h1>Design your taco!</h1>
<img th:src="@{/images/TacoCloud.png}" />

<form method="POST" th:object="${taco}">
		<span class="validationError"
              th:if="${#fields.hasErrors('ingredients')}"
              th:errors="*{ingredients}">Ingredient Error</span>

    <div class="grid">
        <div class="ingredient-group" id="wraps">
            <h3>Designate your wrap:</h3>

            <div th:each="ingredient : ${wrap}">
                <input name="ingredients" type="checkbox"
                       th:value="${ingredient.id}" />
                <span th:text="${ingredient.name}">INGREDIENT</span><br />
            </div>
        </div>

        <div class="ingredient-group" id="proteins">
            <h3>Pick your protein:</h3>
            <div th:each="ingredient : ${protein}">
                <input name="ingredients" type="checkbox"
                       th:value="${ingredient.id}" />
                <span th:text="${ingredient.name}">INGREDIENT</span><br />
            </div>
        </div>

        <div class="ingredient-group" id="cheeses">
            <h3>Choose your cheese:</h3>
            <div th:each="ingredient : ${cheese}">
                <input name="ingredients" type="checkbox"
                       th:value="${ingredient.id}" />
                <span th:text="${ingredient.name}">INGREDIENT</span><br />
            </div>
        </div>

        <div class="ingredient-group" id="veggies">
            <h3>Determine your veggies:</h3>
            <div th:each="ingredient : ${veggies}">
                <input name="ingredients" type="checkbox"
                       th:value="${ingredient.id}" />
                <span th:text="${ingredient.name}">INGREDIENT</span><br />
            </div>
        </div>

        <div class="ingredient-group" id="sauces">
            <h3>Select your sauce:</h3>
            <div th:each="ingredient : ${sauce}">
                <input name="ingredients" type="checkbox"
                       th:value="${ingredient.id}" />
                <span th:text="${ingredient.name}">INGREDIENT</span><br />
            </div>
        </div>
    </div>

    <div>
        <h3>Name your taco creation:</h3>
        <input type="text" th:field="*{name}" />
        <span
                class="validationError" th:if="${#fields.hasErrors('name')}"
                th:errors="*{name}">Name Error</span>
        <br />
        <button>Submit your taco</button>
    </div>
</form>
</body>
</html>
```

####  큰 틀의 코드설명

> form태그의 method 속성이 POST임에도 action 속성을 주지 않았다.
>
> - from태그의 속성action은 폼 데이터(form data)를 서버로 보낼 때 해당 데이터가 도착할 URL을 명시
> - (예) **``<form action="/examples/media/action_target.php">``**
>
> 
>
> action을 주지 않은 경우, 폼이 제출<sub>submit</sub>되면 브라우저가 폼의 모든 데이터를 모아서 폼에 나타난 GET 요청과 같은 경로로 서버에 HTTP POST 요청을 전송한다. 따라서 POST요청을 처리하는 메소드를 DesignTacoController에 작성할 것이다.
>
> - `th:object` : form submit을 할 때, form의 데이터가 th:object에 설정해준 객체로 받아진다.
>
> 
>
>
> Thymeleaf의 자세한 설명은 추후에 포스팅을 통해 정리할 예정 



#### 나의 삽질 기록 [ Error Handling (3시간 + a)

> ![image](https://user-images.githubusercontent.com/72078208/172057411-8bfaf8c5-7b68-4b4c-b677-8745c67c96d1.png)
>
> ![image](https://user-images.githubusercontent.com/72078208/172057740-edea325a-627e-4720-935c-941c53aacf3c.png)
>
>
> **설명**
>
> - `500 Internal Server Error` : 요청을 처리하는 과정에서 서버가 예상하지 못한 상황에 놓임
> - `ParseException` : 구문 분석의 오류
> - `TemplateProcessingException` : 구분 분석하여 쿼리를 만들 때 발생하는 오류
>
> **에러대응 고민하기**
>
> - 첫 번째 고민 
>   **[ 경로문제? ]** :: 경로문제인줄 알았으나 눈을 씻고 찾아봐도 경로상의 문제는 아니었음
> - 두 번째 고민 
>   **[ design.html 문법이 잘못됐나? ]** :: thymeleaf문법을 잘 몰랐기에, 복붙코드라 문제없을 것으로 사료함
> - 세 번째 고민 
>   **[thymeleaf의 버전 문제..?]** :: maven repository에서 모든 버전 다 끌어다 써봤지만 해결되지 않음
> - 네 번째 고민
>   **[ (1)구문분석 오류... 오탈자? ]** 
>   - 다시 한 번 design.html 의심 -> 아니었음
>   - DesignTacoController 의심 -> 아니었음
> - 다섯번째 고민
>   **[설마, POST API를 설계 안 해서? (구현은 아래)]** -> 시도해봤으나 아니었고, 애초에 API를 GET으로 받아오는 것이기 때문에 오류가 날 일이 아니었다고 생각함
> - 여섯번째 고민
>   **[ (2)구분분석 오류... 오탈자? ]**
>   - 우선, <a href="https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html" target="_blank"> thymeleaf doc</a>에서 `fields.hasErrors`부를 찾아보았음
>   - 해당 필드에 대한 유효성 검사를 한다고 나와있었음
>   - 해당 구문까지 닿을 때까지는(동기처리로 생각) 영향을 받을 수 있는건 ` th:object="${taco}"`라고 생각했음.
>   -  `th:object="${taco}"`무엇인지 검색
>   - ![image](https://user-images.githubusercontent.com/72078208/172059421-ff2ee7df-ec89-4476-bb34-cdcf9b71a9ff.png)
>   - ![image](https://user-images.githubusercontent.com/72078208/172060245-4f0f8e84-013b-41c0-8cac-c3bf9f9153b7.png)
>   - 참고링크 : https://stackabuse.com/getting-started-with-thymeleaf-in-java-and-spring/
>   - 결국 ` th:if="${#fields.hasErrors('ingredients')`처리될 때, 필드를 찾지 못한것이다.
>   - taco의 오탈자 찾으러 ㄱㄱ
>
> 
>
> **해결 방법**
>
> - taco.java의 오탈자를 잡아주니, 해결이 됌



####  의아한 부분

> - error가 잡히나 애플리케이션 **빌드는 됩니다.**
>
> < Can not find resolve >

```html
<form method="POST" th:object="${taco}">
		<span class="validationError"
              th:if="${#fields.hasErrors('ingredients')}"
              th:errors="*{ingredients}">Ingredient Error</span>
```

**``` th:errors="*{ingredients}"```** 부분에서 빨간줄이 나오는데,  처음엔 단순히 인텔리 제이 버그로 생각했는데,

아닌 것 같아서 **```th:object="${taco}"```**에서 taco의 객체가 null이어서 그런가 생각했지만, 아닐 가능성이 높음
저 부분은 submit할 때form의 데이터가 **``th:object``**에 설정해준 객체로 넘겨주는 부분으로써 상관없을 것으로 보임 -> 추후 코드를 작성하며 언제 빨간줄(error)가 잡히는지 확인해볼 필요가 있음



#### **의아한 부분 - 시도한 방법과 기록** & 추측

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://thymeleaf.org" >
   <!--   xmlns:th="http://www.thymeleaf.org" > -->
```

1. 주석한 부분의 www를 빼고 `xmlns:th="http://thymeleaf.org" >`로 바꿨음 => 빨간줄 사라짐

   - 하지만, 속성값들을 읽어오지 못해버림... (다른 문제를 야기) <a href="https://stackoverflow.com/questions/38710585/spring-boot-thymeleaf-in-intellij-cannot-resolve-vars/44804086" targe="_blank">StackoverFlow</a>

2.  <추측 -> 아니었음> IntelliJ 버전문제

   - 버전을 낮춰보지는 않았지만, IntellJ의 버전문제가 아닐까 추측됨
   - 학습 코드를 그대로 가져와봤는데 아니었음

   





# 8. 폼 제출 처리하기 (제출시 Thymeleaf의 동작 및 바인딩도 포함)

> **참고자료**
>
> - <a href="https://www.geeksforgeeks.org/spring-boot-how-thymeleaf-works/">Spring Boot - How Thymelaf Works</a>, GeeksforGeeks [th:object내용이 우리의 프로젝트와 동일]

> 위의 코드에서, `th:action`이 기입되지 않았다.
> 이 경우의 `submit`시에 요청 데이터를 모두 모아서 
> 폼에 나타난 GET 요청과 같은 경로로 서버에 HTTP POST요청을한다.





## 1 ] DesignTacoContoller.java에 POST를 처리할 메소드 기입

> action이 없는 경로이므로, GET에 요청한 경로로 데이터를 보내게 되는데
> 현재 PostMapping이 기입되지 않았으므로 메소드를 아래처럼 추가해서 적어주자.

> **src/main/java/tacos/web/DesignTacoController.java**

```java
... (생략)
   @PostMapping
   public String processDesign(Taco desing) {
    // not yet :: 선택된 데이터를 저장하는 비즈니스로직 (3장 (다음포스팅)에서 진행)
    log.info("Processing design : " + design);
    return "redirect:orders/current";
}
...(생략)
```

### log.info

> 받아온 design의 데이터를 로그로 출력한다. 임시의 값과 선택, 기입을 하고 출력하면 아래와 같다.
>
> ![image](https://user-images.githubusercontent.com/72078208/172113167-90b15ec3-7787-43a4-a408-cb3e2b6a46e8.png)

### redirect:

> **<u>orders/current의 상대경로로 재접속이 되어진다.</u>**
>
> 이렇게 함으로써 타코를 생성한 사용자는 자신들의 타코를 받기 위해 주문을 처리하는 폼으로 접속이 가능하다. 그러나, 현재까지 진행한 시점에서는 /orders/current 경로의 요청을 처리할 컨트롤러가 아직 없다.





## 2 ] **타코 주문 폼을 나타내는 컨트롤러** (Get + Post)

> **src/main/java/tacos/web/OrderController.java**

```java
package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Order;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    @PostMapping
    public String processOrder(Order order) {
        log.info("Order submitted: " + order);
        return "redirect:/";
    }
}

```



## 3 ] 타코 주문 정보를 갖는 도메인 객체

> **src/main/java/tacos/Order.java**

```java
package tacos;

import lombok.Data;

@Data
public class Order {
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
}
```



## 4 ] Order.html 작성하기

> **src/main/resources/template/orderForm.html**

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="EUC-KR">
  <title>Taco Cloud</title>
  <link rel="stylesheet" th:href="@{/styles.css}" />
</head>
<body>

<form method="POST" th:action="@{/orders}" th:object="${order}">
  <h1>Order your taco creations!</h1>

  <img th:src="@{/images/TacoCloud.png}" /> <a th:href="@{/design}"
                                               id="another">Design another taco</a><br />

  <div th:if="${#fields.hasErrors()}">
			<span class="validationError"> Please correct the problems
				below and resubmit. </span>
  </div>

  <h3>Deliver my taco masterpieces to...</h3>

  <label for=" deliveryName">Name: </label>
  <input type="text" th:field="*{deliveryName}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('deliveryName')}"
        th:errors="*{deliveryName}">Name Error</span>
  <br />

  <label for="deliveryStreet">Street address: </label>
  <input type="text" th:field="*{deliveryStreet}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('deliveryStreet')}"
        th:errors="*{deliveryStreet}">Street Error</span>
  <br />

  <label for="deliveryCity">City: </label>
  <input type="text" th:field="*{deliveryCity}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('deliveryCity')}"
        th:errors="*{deliveryCity}">City Error</span>
  <br />

  <label for="deliveryState">State: </label>
  <input type="text" th:field="*{deliveryState}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('deliveryState')}"
        th:errors="*{deliveryState}">State Error</span>
  <br />

  <label for="deliveryZip">Zip code: </label>
  <input type="text" th:field="*{deliveryZip}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('deliveryZip')}"
        th:errors="*{deliveryZip}">Zip Error</span>
  <br />

  <h3>Here's how I'll pay...</h3>
  <label for="ccNumber">Credit Card #: </label>
  <input type="text" th:field="*{ccNumber}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('ccNumber')}"
        th:errors="*{ccNumber}">CC Num Error</span>
  <br />

  <label for="ccExpiration">Expiration: </label>
  <input type="text" th:field="*{ccExpiration}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('ccExpiration')}"
        th:errors="*{ccExpiration}">CC Num Error</span>
  <br />

  <label for="ccCVV">CVV: </label>
  <input type="text" th:field="*{ccCVV}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('ccCVV')}"
        th:errors="*{ccCVV}">CC Num Error</span>
  <br />

  <input type="submit" value="Submit order" />
</form>
</body>
</html>
```

### 표현식 *{...} ${...}

>
> 참고자료 : <a href="https://cyberx.tistory.com/132" target="_blank"> thymeleaf(sever-side template engine) 사용법 정리 - 1</a>

### th:field

> 전달하려는 객체의 필드와 매핑시켜준다.

### 삽질의 기록

> input의 id값과 labe의 for 속성의 명과 동일한 이름을 주지 않아서 애먹음
> 여기도 마찬가지로 빨간줄이 나오는데, 일단 무시- 빌드에는 전혀 문제가 없다 (아직 미해결 문제)







# 9. 폼 입력 유효성 검사하기

> 입력값들에 따른, <u>값에 대한 유효성을 검사하는 방법을 알아본다.</u>
>
> 해당 파트에서는 빈 유효성 검사<sub>bean validation</sub>API를 이용해, 애플리케이션에 추가 코드를 작성하지 않고 유효성 검사를 진행할 예정이다.
>
> 유효성 검사 API와 **이 API를 구현한 Hibernate 컴포넌트**는 스프링 부트의 웹 스타터 의존성으로 자동으로 추가된다.



## 1 ] 스프링 MVC에 유효성 검사를 적용 방식 & 종속성 설정

> 1. 유효성을 검사할 클래스(여기서는 Taco와 Order)에 **검사 규칙을 선언**한다.
> 2. 유효성 검사를 해야하는 컨트롤러 메서드에 **검사를 수행하는 것을 지정**한다.
>    1. 여기서는 DesiginTacoController의 processDesign()메소드와
>       OrederController의 processOrder()메소드가 해당된다.
> 3. 검사 에러를 보여주도록 폼 뷰를 수정한다.



**종속성 설정**

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```





## 2 ] 유효성 검사 API의 애노테이션

> 이 애노테이션들은 **검사 규칙을 선언하기 위해 <u>도메인 객체의 속성에 지정</u>할 수 있다.**
>
> - 유효성 검사 API를 구현한 **<u>Hibernate 컴포넌트에는 더 많은 유효성 검사 애노테이션이 추가</u>**되었다.





> **참고자료 : <a href = "https://docs.oracle.com/javaee/7/api/javax/validation/constraints/package-summary.html">Annotation doc API </a>**
> 애노테이션의 옵션과 관련해서는 API를 이용해 정확히 확인하도록 하자





### <1> 속성의 값과 null 유무 확인하기

> **import javax.validation.constraints.*;**

| 유효성 검사 애노테이션 | 속성(옵션)       | 설명                                    |
| ---------------------- | ---------------- | --------------------------------------- |
| @NotNull               | API doc 확인하기 | 해당 값은 null이면 안 된다.             |
| @Size                  | API doc 확인하기 | 필드의 size와 관련한 제한을 둘 수 있다. |





### <2> 카드 지불과 관련한 유효성 검사

> **import org.hibernate.validator.constraints.CredtCardNumber;** 
>
> - <a href="https://docs.jboss.org/hibernate/validator/5.1/api/org/hibernate/validator/constraints/CreditCardNumber.html" target="_blank">API : Hibernate Validator 5.1.3.Final</a>

| 유효성 검사 애노테이션 | 속성(옵션)       | 설명                          |
| ---------------------- | ---------------- | ----------------------------- |
| @CreditCardNumber      | API doc 확인하기 | 유효한 신용카드 번호인지 확인 |

**@CreaditCardNumber**은 Luhn(룬) <u>알고리즘 검사<sub>https://en.wikipedia.org/wiki/Luhn_algorithm</sub>에 합격한 유효한 신용 카드 번호이어야 한다는 것을 선언</u>한다. 이는 <u>사용자의 입력 실수나 고의적인 악성 데이터를 방지</u>해준다. **<u>하지만</u>** <u>입력된 신용 카드 번호가 실제로 존재하는 것인지, 또는 대금 지불에 사용될 수 있는지는 검사하지 못한다.</u> 
(이러한 검사까지 원한다면 실시간으로 금융망과 연동해야한다.)





### <3> 기타 애노테이션 (교재에서 소개된 애노테이션로 정리)

| More Actions유효성 검사 애노테이션 | 속성(옵션)       | 설명                                       |
| ---------------------------------- | ---------------- | ------------------------------------------ |
| @NotBlank                          | API doc 확인하기 | 사용자가 입력을 했는지 유무 확인           |
| @Pattern                           | API doc 확인하기 | 사용자 지정 값에 따르는지 유효성 검사한다. |
| @Digits                            | API doc 확인하기 | 입력값이 허용 범위 내인지 확인한다.        |





### 1. Taco.java에 유효성 검사 애노테이션 삽입

> desing에서 submit진행시, POST로 넘어갈 때의 데이터에 대한 유효성 검사가 진행된다.
>
> - 해당 코드를 넣고 빌드를 시킨 후, 유효하지 않은 상태의 값을 넣어 submit을 진행해도 넘어가짐
> - 왜냐하면 폼과 바인딩될 때 유효성 검사가 수행되도록 설정을 하지 않음 (아래파트에서 진행)



> **src/main/java/tacos/Taco.java**

```java
package tacos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {
    @NotNull
    @Size(min=5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min=1, message = "You must choose at least 1 ingredient")
    private List<String> ingredients;
}

```





### 2. Order.java에 유효성 검사 애노테이션 삽입

> 제출된 타코 주문의 유효성 검사를 하기 위해서는 Order 클래스에 관련 애노테이션을 적용해야한다.
>
> - 해당 코드를 넣고 빌드를 시킨 후, 유효하지 않은 상태의 값을 넣어 submit을 진행해도 넘어가짐
> - 왜냐하면 폼과 바인딩될 때 유효성 검사가 수행되도록 설정을 하지 않음 (아래파트에서 진행)



> **src/main/java/tacos/Order.java**

```java
package tacos;

import lombok.Data;

import org.hibernate.validator.constraints.CreditCardNumber;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Order {
    @NotBlank(message="Name is required")
    private String deliveryName;

    @NotBlank(message="Street is required")
    private String deliveryStreet;

    @NotBlank(message="City is required")
    private String deliveryCity;

    @NotBlank(message="State is required")
    private String deliveryState;

    @NotBlank(message="Zip is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction=0, message = "Invalid CVV")
    private String ccCVV;
}
```

#### @NotBlank

> 입력값을 반드시 입력하도록 설정한다.

#### @CreditCardNumber

> 유효한 신용카드인지 확인한다.
>
> - 단 실제로 있는 카드 번호인지, 지금대불 여부와 관련한 검사는 하지 못한다.

#### @Pattern

> 사용자 패턴에 맞는지 확인한다.

#### @Digits

> 지정 범위까지 한정되는지 확인한다.



# 10. 폼과 바인딩 될 때 유효성 검사의 수행

> 위의 코드에서 검사 규칙 선언이 끝났으므로, 각 폼이 POST 요청이 관련 메소드에서 처리될 때
> 유효성 검사가 수행되도록 컨트롤러를 수정해야한다.





## 1] 제출된 Taco의 유효성 검사의 수행

> DesignTacoController의 processDesign() 메소드 인자로 전달되는 Taco에 
> 자바 빈 유효성 검사 API의 @Valid 어노테이션을 추가한다.
>
> - **@Vaild : import javax.validation.Valid;**



> **src/main/java/tacos/web/DesignTacoController.java**

```java
...(생략)
import javax.validation.Valid;
import org.springframework.validation.Errors;
...(생략)
public class DesignTacoController {
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors) {
        if(errors.hasErrors()) {
            return "design";
        }

        // 이 지점에서 타코 디자인 (선택된 식자재 내역)을 저장한다.
        // 이 작업은 3장에서 진행
        log.info("Processing design : " + design);

        return "redirect:/orders/current";
    }

...(생략)

```

#### @Vaild

> @Vaild는 제출된 Taco 객체의 유효성 검사를 수행한다.
>
> - 제출된 폼 데이터와, Taco 객체가 바인딩 된 후, 그리고 rocessDesign()메소드의 코드가 실행되기 전에하라고 스프링 MVC에 알려준다.

#### Errors errors

> 어떤 검사 에러라도 있다면, 에러의 상세 내역이 Errors 객체에 저장되어 processDesign()으로 전달된다.
> 에러 발생시에 return "design"만을 했기 때문에, 별다른 에러 메시지없이 아래와 같은 이미지가 나온다.
>
> - 아래의 경우는 체크박스 조건에 충족하고, 제출한 경우의 이미지
>
> ![image](https://user-images.githubusercontent.com/72078208/172161464-70f22075-8e4f-4d5e-9fa5-42ea30d7748f.png)
>
> - 일반적인 진입(/design)체크박스가 없는 이유는, 수행할 작업이 없기 때문이다.
> - 단 하나 유의해야하는 것이 `Name must be at least 5 characters long`인데, 이 부분은
>   taco.java에서 @Size에서 옵션을 준 부분을 충족하지 Thymeleaf기능과 함께 동작하여 출력되는 결과 값이다. Thymeleaf기능과 관련해서는 [유효성 검사 에러 보여주기]를 참고하자
> - 만약 아무것도 체크하지 않고 name에 값을 입력하지 않고 제출했을 때는 아래와 같은 결과를 출력한다.
>
>  ![image](https://user-images.githubusercontent.com/72078208/172161464-70f22075-8e4f-4d5e-9fa5-42ea30d7748f.png)
>
> - `You must choose at least 1 ingredient`를 출력되기를 기대했으나 그러지 못했다.
> - 이유는, `th:if="${#fields.hasErrors('...')}"`부분과 `th:errors="*{...}"`이 정의가 되어있지 않기 때문이다. [ 유효성 검사 에러 보여주기 ]에서 자세히 설명한다.



### 2] 제출된 Order의 유효성 검사의 수행

> **src/main/java/tacos/web/DesignTacoController.java**

```java
...(생략)
import javax.validation.Valid;
import org.springframework.validation.Errors;
...(생략)
public class DesignTacoController {
@PostMapping
    public String processOrder(@Valid  Order order, Errors errors) {
        if(errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + order);
        return "redirect:/";
    }

...(생략)

```

#### Errors errors

> 설명생략
>
> 에러 발생시 보여지는 화면은 아래와 같다.
>
> ![image](https://user-images.githubusercontent.com/72078208/172162569-524a67d4-d787-4925-b332-86bca46ef8ee.png)
>
> - 빨간 글씨가 출력되는 이유는 Taco에서 에러가나서 출력되는 이유와 같다. 위를 참고하자





# 11. 유효성 검사 에러 보여주기

> Thymeleaf는 fields와 th:errors 속성을 통해서 Errors 객체의 편리한 사용 방법을 제공한다.

**(예시)**

```xml
<label for="ccNumber">Credit Card #: </label>
  <input type="text" id="ccNumber" th:field="*{ccNumber}" />
  <span class="validationError"
        th:if="${#fields.hasErrors('ccNumber')}"
        th:errors="*{ccNumber}">CC Num Error</span>
  <br />
```

>  **order의 일부를 발췌했으며 아래 설명은 위의 메커니즘을 따라가는 코드는 모두 동일하게 작동한다.**
>
> 
>
> **<u>th:if 속성</u>**은 \<span>을 보여줄지 말지 결정한다 (if문과 동일)
>
> 이때, fields 속성의 hasErrors() 메소드를 사용해서 ccNumber 필드에 에러가 있는지 검사한다.
> 만약, 에러가 있다면 \<span>이 일어나게 된다.
>
> **<u>th:errors 속성</u>**은 ccNumber 필드를 참조한다.
> 에러가 있다면, 사전 지정된 메시지 (CC Num Error)를 검사 에러 메시지로 교체한다.



# 12. 뷰 컨트롤러로 작업하기

> 사용자의 처리를 하지 않는 컨트롤러를 다른식으로 처리하기
>
> - 이를 **뷰 컨트롤러**라고 한다.
> - 중복되는 말이지만 뷰 컨트롤러는 다른식으로 작업처리가 가능하다.



> 현재까지 세 가지 컨트롤러를 작성했다. (HomeController, DesiginController, OrderController)
>
> 각 컨트롤러는 애플리케이션의 서로 다른 기능을 제공한다. 하지만 프로그래밍의 패턴은 아래와 동일하다.
>
> - 스프링 컴포넌트 검색에서 자동으로 찾은 후, 스프링 애플리케이션 컨텍스트의 빈으로 생성되는
>   컨트롤러 클래스임을 나타내기 위해 @Controller 애노테이션을 사용한다.
> - HomeController를 제외하고 자신이 처리하는 요청 패턴을 정의하기 위해서 @RequestMapping 사용
> - 메소드의 종류를 처리하기 위해서 @GetMapping @PostMapping이 지정된 하나 이상의 메소드 가짐



## 1. 뷰 컨트롤러 다른식으로 처리하기

> **src/main/java/taco/web/WebConfig.java**

```java
package tacos.web;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}

```

> **WebMvcConfigurer 인터페이스를 구현했다.**
>
> <SUP>**1 WebMvcConfigurer들여다보기** </SUP> WebMvcConfigurer 인터페이스는 <u>스프링 MVC를 구성하는 메소드를 정의하고 있다.</u> 그리고  **<SUP>1 WebMvcConfigurer들여다보기</SUP>**  <u>**인터페이스임에도 불구하고 정의된 모든 메소드의 기본적인 구현을 제공한다.**</u>
>
> addViewControllers()를 오바라이딩하고, <u>하나 이상의 뷰 컨트롤러를 등록하기 위하여</u> 
> <u>**ViewControllerRegistry**를 인자로 받는다.</u>
>
> Get요청을 처리하는 경로인 "/"를 인자로 전달하여 addViewController()를 호출한다.
> 이 메소드는 ViewControllerRegistration 객체를 반환하고, 그리고 "/" 경로의 요청이 전달되어야
> 하는 뷰로 home을 지정하기 위해 연달아 ViewControllerRegistration 객체의 setViewName()을 호출한다.
>
> 이로써 HomeController.java를 대체할 수 있다. 즉, HomeController를 삭제하더라도 빌드가 잘 된다.
> 추가적으로, HomeControllerTest에서 @WebMvcTest 애노테이션의 HomeController 참조만 삭제하면 테스트 클래스도 에러 없이 컴파일 될 수 있다.

### @Configuration

> @*Configuration* 이라고 하면 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션이다.



#### [1]  <sup> 1 </sup> WebMvcConfigurer들여다보기

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.web.servlet.config.annotation;

import java.util.List;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

public interface WebMvcConfigurer {
    default void configurePathMatch(PathMatchConfigurer configurer) {
    }

    default void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    }

    default void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    default void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    default void addFormatters(FormatterRegistry registry) {
    }

    default void addInterceptors(InterceptorRegistry registry) {
    }

    default void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    default void addCorsMappings(CorsRegistry registry) {
    }

    default void addViewControllers(ViewControllerRegistry registry) {
    }

    default void configureViewResolvers(ViewResolverRegistry registry) {
    }

    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    }

    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
    }

    default void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    default void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    default void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    }

    default void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    }

    @Nullable
    default Validator getValidator() {
        return null;
    }

    @Nullable
    default MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}

```

> **인터페이스의 디폴트 메소드(뿐만 아니라 인터페이스 내부 기입 내용)와 관련하여 참고하면 좋은 글**
>
> - <a href="https://limkydev.tistory.com/197" target="_blank"> [JAVA] 자바 인터페이스란?(Interface)_이 글 하나로 박살내자</a>, Limky 삽질 블로그
>
> default 메소드는 강제성을 가지지 않으며, 몸체를 가질 수 있다. 선택적 오버라이딩이 가능하다.
> 몸체를 가진다는 것은 위에서 말한, 기본적인 구현을 가진다라는 말과 완전 동일한 의미이다.
> 인터페이스와 관련해서 알더라도 위의 글을 한 번은 참고해서 반드시 복습하자









