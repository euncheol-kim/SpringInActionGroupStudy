### goal

> Spring을 시작하기전에 알아야할 내용을 숙지한다.



### keyword

> Spring Container( == Spring Application Context)
>
> IoC (Inversion of Control) = 제어의 역전
>
> DI(Dependency Injection) = 의존성 주입
>
> DL(Dependency Lookup) = 의존성 검색
>
> Bean = Spring에서 관리하는 자바 오브젝트 (IoC/DI와 깊은 관련이 있다.)



### 요약

| keyword                                       | 설명                                                         |
| --------------------------------------------- | ------------------------------------------------------------ |
| Spring Container (Spring Application Context) | - 설정정보를 참고해서 애플리케이션을 구성하는 오브젝트를 `생성`/`관리`<br />- <u>Spring에서 관리하는 Object</u>를 `Bean`이라고 하는데, <u>Bean의 생성/관계설정 같은 제어 담당</u> |
| IoC (Inversion of Control) 제어의 역전        | 쉽게 생각하면 <u>우리가 처음 프로그래밍을 배울 때</u>, <u>클래스의 인스턴스를 생성하고 사용하는 방식으로 프로그래밍</u> 한다. <u>어찌보면 개발자인 우리가 인스턴스를 제어하는 것이라 볼 수 있는데, 이를 **다른 주체에 위임하여 제어를 맡기는 것**</u>이다. |
| DI (Dependency Injection) 의존성 주입         | 메소드를 통해 `Object(인스턴스)`를 주입받는것                |
| DL (Dependency Lookup) 의존성 검색            | 메소드 내부적으로 `Object(인스턴스)`를 가져다 쓰는 것        |
| Bean                                          | Spring에서 관리하는 자바 오브젝트 (IoC/DI와 깊은 관련이 있다.) |





#  * DI와 DL (의존성 주입과 제어의 역전)



###  `DI와 DL(DL은 마지막에 잠깐 나옵니다.) 설명하기 전에`

우리가 처음 프로그래밍을 해왔을 때, 아래와 같은 비슷한 프로그램을 작성한 경험이 있을 것이다.



- **TestCode.java**

```java
public class TestCode {
    public static void main(String[] args) {
        Calculator cal = new Calculator();
        Scanner sc = new Scanner(System.in);
        int num1 = sc.nextInt();
        int num2 = sc.nextInt();
        cal.add(num1, num2);
    }
}

class Calculator {
    public int add(int num1, int num2) {
        return num1 + num2;
    }
    public int sub(int num1, int num2) {
        return num1 - num2;
    }
}
```

간단한 계산기 프로그램이다. BeginnersCode 클래스에서의 `main메소드`에서는 Calculator 클래스를 instance(object)로 생성하여 기능에 맞는 메소드를 가져다 쓴다. 우리는 해당 프로그램을 Naver와 Google에 판매했다고 가정하고 코드를 작성하겠다.



- **Naver.java**

```java
package springinaction;

import java.util.Scanner;

public class Naver {
    public static void main(String[] args){
        Calculator calculator = new Calculator();

        int num1, num2;
        Scanner sc = new Scanner(System.in);
        num1 = sc.nextInt();
        num2 = sc.nextInt();

        calculator.add(num1, num2);

    }
}
```



- **Google.java**

```java
package springinaction;

import java.util.Scanner;

public class Google {
    public static void main(String[] args){
        Calculator calculator = new Calculator();

        int num1, num2;
        Scanner sc = new Scanner(System.in);
        num1 = sc.nextInt();
        num2 = sc.nextInt();

        calculator.add(num1, num2);

    }
}
```



이렇게 판매를 하고, <u>시간이 조금 지나서 Naver측에서는 `공학용 계산기`도 만들어달라고 요청</u>한다.

Naver의 요청을 위해서 `공학용계산기.java`파일을 생성했다고 하자. 그러면 `Naver.java`에서는 공학용 계산기를 사용할 수 있게 하는 방법은 뭐가 있을까? 간단하다. Naver에 `공학용계산기 instance`를 생성하게 하면 된다. 이런식으로 Naver가 요구하는 계산기가 다양해져서 각자 다른 기능을 하는 계산기 프로그램을 1000개 생성했다고 하자. (예로 `cal1.java`,`cal2.java`,`cal3.java`,`cal4.java`,.......이렇게 1000개가 있는 상태) <u>Naver 측에서는 기업 특성상 한 업무를 볼 때 하나의 계산기만 사용한다.</u> `Naver.java`파일에는 <u>각각의 기능을 담당하는 instance가 생성</u>되었을 텐데, <u>이는 메모리에 큰 부담을 가져올 것</u>이다. instance뿐이아니다. 애초에 이렇게 설계를 한다면 <u>비슷한 기능을 하는`getAdd메소드`와 `getSub메소드`와 같이 직관적인 method이름이 겹치는 경우도 생길 수 있다.</u> 그렇다고해서 각 클래스의 메소드 이름을 `getAdd1`, `getSub2`라고 이름을 짓는다면 유지보수시에 큰 어려움을 겪게 될 것이다. **이때 우리는 DI와 IoC를 사용하게 된다.**



##### ※ 위의 코드를 근거로 Naver.java와 Google.java는 Calculator클래스에 의존해 있다. 라고 말 할 수 있다.

- 쉽게, Naver.java는 런타임 과정에서 반드시 Calculator를 반드시 인스턴스로 가진다.와 같은 말이다.





## 1. DI, 의존성 주입

- 의존성을 주입하는 방법은 다양하다. 대표적인 하나의 방법이 메소드를 통해서 instace를 넘기는 것이다.
  - <u>아래의 코드는 위에서 나온 계산기 예시와 관련없습니다.</u>


```java
public class DI {
    private Calculator calculator;
    public DI(Calculator calculator){
        this.calculator = calculator;
    }
}
```

##### 설명

`DI 클래스`는 instance가 생성될 때 반드시` Calculator클래스`의 객체가 `주입`되어야한다.

- **주입이라는 단어가 다소 낯설을 수 있는데**,
- 여기서는 <u>매개변수로 instance를 던진다! 라고 생각</u>한다.
- 의존성 주입의 방법은 다양하다고합니다.



### [1] DI, 의존성 주입의 장점

1. **의존성 주입을 하지 않는다면 특정 instance생성에 `의존`해야하는 경우가 생기는데 이를 방지할 수 있다.**

   - **의존을 해야한다**라는 의미란, 내부코드에서 특정 인스턴스를 만들어 사용하는 것 (**의존성 검색**이라 함)

   - 위에서 설명한 것 처럼, 1000개의 인스턴스를 내부에 구현했는데 1개만 사용한다면 이런 비극이 어디있으랴... 메모리 낭비에, 시스템 성능저하도 가져올 것이다.

2. **<u>의존성을 주입 받는 클래스는</u> 자신의 임무에 충실할 수 있게 된다.**
   - 의존성을 주입 받는 클래스 입장에서, 어떤 객체를 사용할지 고민하지 않아도 된다는 것이다.
   - 이는 클래스 구현에 있어서 기능을 한쪽으로 집중시켜 하나의 개발에 집중할 수 있도록 해준다.
   - 또한 이렇게 집중이 되어 개발 되었다면 의존성 주입을 받은 클래스의 기능이 추가/삭제 되어질 때,  의존성 주입을 받는 클래스 부분만 수정하면 된다.
   - 즉, 다른 기능까지 고려하면서 유지보수를 할 필요가 없다는 것이다.

3. **필요한 기능(메소드)를 구현하여 재사용이 가능하다.**
   - 완성된 프로그램에 덧셈을 담당하는 메소드가 있고 우리는 완성된 프로그램에 추가 기능을 탑재한다. 
   - 그 기능은 `(a/b) + (a*b)`의 계산을 한다고 가정하겠다.
   -  덧셈을 담당하는 메소드를 새로 추가해줄 필요없이 가져다 쓰면 되는 것이다.

4. **상속을 이용해서 프로그래밍이 가능하다**
   - 바로 아랫 부분에 설명하겠습니다.

5. *<u>추가할 사항이 있으면 꼭 말씀해주시면 감사하겠습니다.</u>*



#### [2] DI, 의존성 주입의 장점 : 상속을 이용한 프로그래밍

<u>의존성 주입을 하지 않고 설계를 했을 때</u>, 네이버와 계산기 모형도(?)를 그려보면 아래와 같은 그림이 될 것이다. 

- 설명을 위해서 가상의 계산기들을 만들었습니다. 

![123](https://user-images.githubusercontent.com/72078208/167260608-9b4d3118-abb8-49d0-ace1-3ddcd6ec78d6.jpg)

그림의 설명을 조금 돕자면, Naver는 각각의 계산기를 쓰기위해 각각의 java파일로 생성된 class파일의 인스턴스를 가져와야한다.

만약, **<u>이를 상속을 통한 의존성 주입을 한다고하면 어떨까?</u>** 각각의 계산기는 `덧셈`과 `뺄셈`이 공통적으로 사용된다고 가정하고 interfac와 각각의 계산기 클래스의 관계를 맺어주면 아래와 같은 코드가 될 것이며, UML또한 아래와 같아질 것이다.



- **CommonCalculator.java** 및 **여하계산기.java** (interface와 class는 각각의 java파일에 생성되어 있다고 생각합시다.)

```java
public interface CommonCalculator {
    public int add(num1, num2);
    public int sub(num1, num2);
}

public class Calculator implements CommonCalculator {
    public int add(num1, num2) { 
        return num1 + num2;
    }
    public int sub(num1, num2) { 
        return num1 - num2;
    }
    
   public void mul(num1, num2) {
       return num1 * num2;
   }
    
   //여하기능추가
}

public class 화학용_계산기 implements CommonCalculator {
    public int add(num1, num2) { 
        return num1 + num2;
    }
    public int sub(num1, num2) { 
        return num1 - num2; 
    }
    
    //여하 기능 추가
}

public class 건축용_계산기 implements CommonCalculator {
    public int add(num1, num2) { 
        return num1 + num2;
    }
    public int sub(num1, num2) { 
        return num1 - num2;
    }
    
    //여하 기능 추가
}

public class 생명공학_계산기 implements CommonCalculator {
    public int add(num1, num2) { 
        return num1 + num2;
    }
    public int sub(num1, num2) { 
        return num1 - num2;
    }
    
    //여하 기능 추가
}
```



- Naver와 Calculators들의 관계도 변형

![1213124](https://user-images.githubusercontent.com/72078208/167261371-93c90db1-0788-4108-b097-4d8f994decd8.jpg)



이렇게 되었을 때 `Naver.java`가 `일반계산기(Calculator라고 하겠다.)를 사용한다고 했을때 아래와 같아진다.`

- Naver.class

```java
public class Naver {
    public static void main(String[] args){
        CommonCalculator commonCalculator = new Calculator();

        int num1, num2;
        Scanner sc = new Scanner(System.in);
        num1 = sc.nextInt();
        num2 = sc.nextInt();

        commonCalculator.add(num1, num2);
        //다운캐스팅으로 mul메소드 사용
        Calculator calculator = (Calculator) commonCalculator; 
        calculator.test(); 

    }
}
```

이렇게, `new Calculator()`부분에 `new 화학용_계산기()`, `new 건축용_계산기()`, `new 생명공학용_계산기()`로 바꿔준다면  Naver가 한 업무당 하나씩 계산기를 사용하는 조건에 충족되며 인스턴스를 하나 사용해 메모리 사용의 낭비 없이 사용하게 된다. 예시의 이해를 돕기 위해서 `main메소드`에 ` CommonCalculator commonCalculator = new Calculator();` 에서 Calculator 인스턴스를 바로 작성했는데 사실은 이것도 `사용자의 책임`으로 넘겨 다른 인스턴스를 받아올 수 있게끔 만들 수 있을 것이다. 그리고 하나 더 넘어갈 것이, 내부적으로 어쩔 수 없이 객체를 불러와서 사용하는 것을 `의존성 검색`이라고 한다.







# * IoC, 제어의 역전

- **프로그래밍의 흐름을 거꾸로 뒤집는 것**

**일반적으로는** <u>main()메소드에서 어떤 객체를 사용할지 결정하고 생성하고, 객체의 메소드를 호출하고, 메소드 안에서 사용할 것을 결정하는 식의 호출 방법이 반복된다.</u>(**<u>객체를 구성하는 작업에 능동적인 참여</u>**) 하지만 `제어의 역전`은 이러한 흐름을 거꾸로 뒤집는 것이다.

![13124235](https://user-images.githubusercontent.com/72078208/167268364-e2fc3fcf-4deb-490a-aa42-88443fa41bd3.jpg)





# * Spring Bean

Spring Bean은 스프링이 직접 제어를 통해 직접 만들고 관계를 부여하는 오브젝트

- 즉, Spring의 java object이다.
- **Spring Bean은 !제어의 역전이 적용된! 오브젝트를 가리킨다.**



# * Spring Application Context ( == Spring Container)

빈의 생성과 관계설정 같은 제어를 담당

- 애플리케이션 전반에 걸쳐 모든 구성요소의 제어 작업을 담당하는 IoC엔진이다.
  - 별도의 정보를 참고해서 빈의 생성, 관계설정등의 제어 작업을 총괄
  - 여기서 별도의 작업이란 @Configulation이나 @Bean과 같은 annotaion을 의미한다.