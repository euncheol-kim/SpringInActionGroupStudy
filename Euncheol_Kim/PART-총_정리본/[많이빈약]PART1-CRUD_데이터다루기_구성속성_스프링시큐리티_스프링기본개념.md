# 1. Thymeleaf

## [1] xmlns 설정

```html
<html xmlns:th = "http://www.thymeleaf.org"> <!--1번-->
<html xmlns:th = "http://thymeleaf.org"> <!--2번-->
```

> 두 가지 방식으로 Thymeleaf를 설정하고 사용이 가능합니다.
>
> 
>
>
> ※ 저같은 경우는 두 방식 모두 문제를 가졌었는데 문제는 아래와 같았습니다.
>
> - 1번의 경우 : *{...} 표현식을 사용할 수 없었고 실행도 되지 않았습니다.
> - 2번의 경우 : thymeleaf의 속성값을 모두 가져올 수 없다는 경고가 나왔으나 실행은 문제가 없었습니다.
>
> 
>
> `<html xmlns:th = "http://www.thymeleaf.org">`로 시도한 방법 **[모두 실패]**
>
> - Thymeleaf의 모든버전을 가져와서 종속성 설정을 바꿔봤습니다.
> - 인텔리제이 버전의 문제로 생각했으나, 저자가 올린 git파일에서는 문제가 없었습니다 
>   (인텔리제이 동일)
>
> 
>
> 추측되는 한 가지
>
> ```xml
> <parent>
>         <groupId>org.springframework.boot</groupId>
>         <artifactId>spring-boot-starter-parent</artifactId>
>         <version>2.7.0</version>
>         <relativePath/> <!-- lookup parent from repository -->
> </parent>
> ```
>
> 저자가 올린 파일은 이 부분에서 version이 2.2.6이었는데, 제 파일은 2.7.0이었습니다.
> 2.2.6으로 낮춰봤지만, 호환성 문제 때문인지 낮추면 오류가 발생했습니다.



## [2] Thymeleaf 표현식

### ${....}

> 전달 받은 변수를 사용하는 표현식입니다.
>
> - 내부의 ... 은 Context로 말하기도 하는것 같습니다. 

### *{....}

> th:object로 정의된 변수가 있다면, <u>그 변수값에 포함된 값을 나타내며</u>
> th:object가 없다면 ${...}와 사용법이 같습니다.

### @{....}

> 경로를 나타낼 때 사용하는 표현식입니다.
>
> ```html
> <form th:action="@{/board/update/{id}(id = ${board.id})}" method="post">
> => /board/update/3
> => /board/update/4
> 
> <a th:href="@{/board/update/(id = ${board.id})}">
> => /board/update?id=3
> => /board/update?id=4
> ```

### #{....}

> 미리 정의된 message properties 파일이 존재하고 
> thymeleaf engine에 등록되어 있다면 #표현식으로 나타낼 수 있습니다.
>
> 
>
> - application.properties
>
> ```properties
> how.food="삼겹살"
> ```
>
> - thymeleaf.html
>
> ```html
> <p th:text = "#how.food">음식</p>
> => 삼겹살 (음식 텍스트 대신, 삼겹살로 대체)
> ```



## [3]thymeleaf 속성

### th:each = "listValue : ${key}"

> thymeleaf에서 태그 내부의 list형 데이터를 반복합니다.
>
> ```html
> <div th:each="ingredient : ${veggies}">
>             <input name="ingredients" type="checkbox"
>                    th:value="${ingredient.id}" />
>             <span th:text="${ingredient.name}">INGREDIENT</span><br />
> </div>
> ```

### th:if

> thymeleaf에서 if문 역할을 합니다.
>
> ```html
> <span class="validationError"
>               th:if="${#fields.hasErrors('ingredients')}"
>               th:errors="*{ingredients}">Ingredient Error</span>
> ```

### th:field

> th:object에 설정해 준 객체의 내부와 매칭해준다.
>
> ```html
>  <input type="text" th:field="*{name}" />
> ```

### th:object

> form에 설정되며, submit시 form의 데이터가 th:object에 설정한 객체로 받아진다.



# 2. 페이징처리 관련 클래스 및 메소드

## [1] Page : interface

> **사용예시**
>
> - Page<엔티티클래스>변수명 = JpaRepository상속받은_인터페이스.findAll()

| Page관련 메소드    | 설명                      |
| ------------------ | ------------------------- |
| getTotalPages()    | 총 몇 페이지인지 반환     |
| getTotalElements() | 객체의 전체 개수를 반환   |
| getNumber()        | 현재 페이지 번호를 반환   |
| getSize()          | 페이지당 데이터 개수 반환 |
| hasNext()          | 다음 페이지의 존재여부    |
| isFirst()          | 시작 페이지 여부          |

> 참고자료 
>
> - https://kgvovc.tistory.com/62, page설계한 방법을 a~z까지 기입
> - https://tychejin.tistory.com/27 , page설계한 방법을 a~z까지 기입



# * 스프링의 설계시, 패키지 구성

> 참고자료 : https://dodop-blog.tistory.com/264