
# Chapter2.3 폼 입력 유효성 검사하기
&nbsp;
# Validation 개요

> 사용자가 만약 특정 로직을 수행 했는데 잘못된 입력으로 인해 어플리케이션이 오작동을 하면 안된다. 그러므로 **검증**을 통해 처리해 줘야 함.
> 
&nbsp;
# Validation 사용



검증을 수행하기 위해선 다음과 같은 의존성 모듈이 필요

```java
org.springframework.boot:spring-boot-starter-validation'
```

검증을 위한 라이브러리는 두 가지가 있다.

- `javax` 표준 스펙(jsr-303) Bean Validator : [공식문서](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html)
- `hibernate` validator : [공식문서](https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#section-builtin-constraints)

> `@Valid` 는 annotation 이 javax 표준 검증기이고 컨트롤러 메소드 유효성 검사만 가능.
>`@Validated` 는 Spring 에서 지원해주는 검증기이고 다른 계증에서도 사용이 가능. (AOP기반)
>
>`LocalValidatorFactoryBean` 이 annotation을 보고 검증을 수행.
실무에선 대부분 hibernate 의 validator를 사용한다고 한다.

&nbsp;
## 검사 규칙 선언하기 예시

```java
package springinaction.tacos.entity;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Order {

    @NotBlank(message = "Name is required")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
            message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;
}
```

```java
package springinaction.tacos.entity;

import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {

    @NotBlank
    @Size(min=5,message = "최소 5글자 필요")
    private String name;

    @Size(min=1, message = "최소 하나이상의 재료필요")
    private List<String> ingredients;
}
```

`@CreditCardNumber` 와 같은 검증은 hibernate 의 validator가 수행한다는 것을 알 수 있다.

&nbsp;&nbsp;
# 폼과 바인딩 그리고 검증


사용자가 입력한 form 을 통해 서버로 넘어왔다고 가정하면 `@Valid` annotation 이 붙어 있는 필드를 검증하게 된다. 그리고 검증이 실패할 시 `Errors` 에 담긴다.

```java
@PostMapping
    public String processDesign(@Valid Taco design, Errors errors) {
        if (errors.hasErrors()) {
            List<FieldError> list = errors.getFieldErrors();
            list.forEach(e->log.error(e.getDefaultMessage()));

            return "design";
        }

        log.info("Processing design: " + design);
        return "redirect:/orders/current";
    }
```

> 참고) 검증 결과를 담은 `BindingResult`와 `fieldError` `grobalError` 등이 있다.
>
>`fieldError`는 `messageConverter` 작동이 성공해서 정상적으로 바인딩을 하고 나서 필드 유효성을 검증한 뒤에 실패하면 `BingdingResult`에 담겨지고
`grobalError`는 비즈니스로직 관련해서 발생한 오류를 `BingdingResult`에 담는다.
( BindingResult는 Errors를 상속받은 인터페이스임 )
>
>**만약 바인딩 자체에 실패한다면 ..? 컨트롤러가 호출되지 않음 .. !**
>
>만약 검증에 실패시 `bindingResult.hasErrors()` 를 수행해서 예외 처리를 하는 방식으로 한다.
추후 뒷장에 더 상세하게 나와있으니 pass
> 

### 에러 메세지 출력해보기

```java
2022-05-15 02:27:10.705  INFO 11824 --- [nio-8080-exec-7] s.t.web.controller.DesignTacoController  : Processing design: Taco(name=12345, ingredients=[FLTO, GRBF, CHED, TMTO, SLSA])
2022-05-15 02:27:18.455 ERROR 11824 --- [nio-8080-exec-1] s.tacos.web.controller.OrderController   : Must be formatted MM/YY
2022-05-15 02:27:18.456 ERROR 11824 --- [nio-8080-exec-1] s.tacos.web.controller.OrderController   : Not a valid credit card number
```
&nbsp;

# 만약 검증에 실패한다면



> 사용자가 입력한 form 검증이 실패하면 다시 입력할 수 있도록 입력폼을 불러와야 하는데 이전의 입력한 값들을 유지하고 싶다면 `Thymeleaf` 에서 다음과 같이 수행할 수 있다.
> 

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="EUC-KR">
    <title>Taco Cloud</title>
    <link rel="stylesheet" th:href="@{/css/styles.css}"/>
</head>
<body>

<form method="POST" th:action="@{/orders}" th:object="${order}">
    <h1>Order your taco creations!</h1>

    <img th:src="@{/images/TacoCloud.png}"/> <a th:href="@{/design}" id="another">Design another taco</a><br/>

    <div th:if="${#fields.hasErrors()}">
			<span class="validationError"> Please correct the problems below and resubmit. </span>
    </div>

    <h3>Deliver my taco masterpieces to...</h3>

    <label for="deliveryName">Name: </label>
    <input type="text" id="deliveryName" th:field="*{deliveryName}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('deliveryName')}"
          th:errors="*{deliveryName}">Name Error</span>
    <br/>

    <label for="deliveryStreet">Street address: </label>
    <input type="text" id="deliveryStreet" th:field="*{deliveryStreet}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('deliveryStreet')}"
          th:errors="*{deliveryStreet}">Street Error</span>
    <br/>

    <label for="deliveryCity">City: </label>
    <input type="text" id="deliveryCity" th:field="*{deliveryCity}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('deliveryCity')}"
          th:errors="*{deliveryCity}">City Error</span>
    <br/>

    <label for="deliveryState">State: </label>
    <input type="text" id ="deliveryState" th:field="*{deliveryState}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('deliveryState')}"
          th:errors="*{deliveryState}">State Error</span>
    <br/>

    <label for="deliveryZip">Zip code: </label>
    <input type="text" id ="deliveryZip" th:field="*{deliveryZip}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('deliveryZip')}"
          th:errors="*{deliveryZip}">Zip Error</span>
    <br/>

    <h3>Here's how I'll pay...</h3>
    <label for="ccNumber">Credit Card #: </label>
    <input type="text" id ="ccNumber" th:field="*{ccNumber}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('ccNumber')}"
          th:errors="*{ccNumber}">CC Num Error</span>
    <br/>

    <label for="ccExpiration">Expiration: </label>
    <input type="text" id ="ccExpiration" th:field="*{ccExpiration}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('ccExpiration')}"
          th:errors="*{ccExpiration}">CC Num Error</span>
    <br/>

    <label for="ccCVV">CVV: </label>
    <input type="text" id ="ccCVV" th:field="*{ccCVV}"/>
    <span class="validationError"
          th:if="${#fields.hasErrors('ccCVV')}"
          th:errors="*{ccCVV}">CC Num Error</span>
    <br/>

    <input type="submit" value="Submit order"/>
</form>
</body>
</html>
```

tip) input 태그에 `id` 값을 추가로 넣어주면 해당 input 태그의 id 식별이 가능해서 추적이 용이함.

> `th:field = *{필드명}` 을 통해 field 를 식별하고 
`th:if="${#fields.hasErrors('필드명')}”` 을 통해 만약 그 field 에서 오류가 발생하면 
`th:errors="*{필드명}"` 과 같이 에러가 발생한 필드를 재 출력한다.

이 외 만약 에러가 발생했을 경우 별도로 css class를 적용할 수 있는 
`th : errorclass` 를 타임리프가 지원해준다.
>