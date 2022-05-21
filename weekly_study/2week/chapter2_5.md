----



### **👨‍👨‍👦‍👦 SpringInAction Group Study [이채민, 권태구, 최재호, 곽현기, 김은철]**

**group - GIT : https://github.com/euncheol-kim/SpringInActionGroupStudy**

<br>

#### **이채민 님 : https://github.com/CokeLee777**

#### **권태구 님 : https://github.com/Hashtae9**

#### **최재호 님 : https://github.com/jaero0725**

#### **곽현기 님 : https://github.com/nicebyy**

#### **김은철 (본인): https://github.com/euncheol-kim**

<br>

#### 작성자 : 김은철 

<br>

-----

<br>

<br>

### goal

> view 템플릿 라이브러리 선택하기

<br>

# 1. 뷰 템플릿 라이브러리 선택하기

## [1] 스프링에서 지원하는 템플릿

| 템플릿(엔진)          | 스프링 부트 스타터 의존성                             |
| --------------------- | ----------------------------------------------------- |
| FreeMarker            | spring-boot-starter-freemarker                        |
| Groovy 템플릿         | spring-boot-starter-groovy-templates                  |
| JavaServer Pages(JSP) | 없음 (Tomcat이나 Jetty 서블릿 컨테이너 자체에서 제공) |
| Mustache              | spring-boot-starter-groovy-mustache                   |
| **Thymeleaf**         | spring-boot-starter-thymeleaf                         |

<br>

## [2] packaging에 따른 템플릿 호환 유무

| packaging | 호환되는 템플릿                                  |
| --------- | ------------------------------------------------ |
| Jar       | JSP를 제외한 Springboot에서 호환하는 모든 템플릿 |
| War       | JSP만을 취급                                     |

> WAR배포 사례
>
> - SpringBoot JSP-WAR 배포, https://vakhais.github.io/develop/Spring-boot-war%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0!/, 월급루팡 개발자

<br>

<br>

# 2. 템플릿 캐싱

기본적으로 템플릿은 최조 사용될 때 한 번만 파싱(코드 분석)된다.<br>

그리고 **<u>파싱된 결과는 향후 사용을 위해 캐시에 저장된다.<br></u>**

하지만 <u>개발이 이루어지는 단계에서 캐싱된 템플릿을 매번 불러온다면, 개발자의 수정사항이 바로 바로 적용이 되지 않아 코드가 수정되어질 때마다 애플리케이션을 다시 실행해야한다.</u><br>

<br>

## [1] 템플릿 캐싱 비활성화

위와 같은 번거로움을 막기 위해서는 각 템플릿의 캐싱 속성만 false로 설정하면 된다.<br>

각각의 템플릿의 캐싱 비활성화는 아래 표와 같다.<br>

| 템플릿(엔진)     | 캐싱 속성                    |
| ---------------- | ---------------------------- |
| FreeMarker       | spring.freemarker.cache      |
| Groovy Templates | spring.groovy.template.cache |
| Mustache         | spring.mustache.cache        |
| Thymeleaf        | spring.thymeleaf.cache       |

단, 프로덕션에서 애플리케이션을 배포할 때는 방금 추가한 설정을 삭제하거나 true로 변경해야한다.<br>

**하지만**, <u>스프링 부트의 DevTools를 사용하는 것이 훨씬 더 쉽다</u>. SpringInAction의 1장에서 언급했듯이, DevTools를 의존성 설정해둔다면 개발 시점에 많은 도움을 제공하며, 모든 템플릿 라이브러리의 캐싱을 비활성화 한다.<br>

그러나 애플리케이션이 실무 운영을 위해 배포될 때는 DevTools 자신이 비활성화 되므로 템플릿 캐싱이 활성화 될 수 있다.<br>

<br>

<br>

<h1 style="color:blue">추가자료</h1>

# [1] 템플릿

## 1. Mustache, Thymeleaf

| Mustache  | 특징                                                         |
| --------- | ------------------------------------------------------------ |
| Mustache  | - 문법이 다른 템플릿 엔진보다 심플하다.<br />- 로직 코드를 사용할 수 없어, View의 역할과 서버의 역할을 명확하게 구분한다.<br />- 확장자 : **블라블라**.mustache |
| Thymeleaf | - HTML 태그에 속성으로 템플릿 기능을 사용하는 방식이 어려울 수 있음<br />- Vue.js 태그 속성 방식과 비슷하다. |

<br>

##  2. 템플릿 엔진과 관련된 글

> 참고링크 : FreeMarker vs Groovy vs Mustache vs Thymeleaf, https://springhow.com/spring-boot-template-engines-comparison/, Raja Anbazhagan

<br>

## 3. 호환성과 관련하여

- thymeleaf가 짱이라고 합니다.

<br>

<br>

# [2] 발표 파트의 몰랐던 내용정리(미흡했던 부분)

## [1] Maven, pom.xml

- \<dependency> ... \<dependency> 내부 태그
  - groupId : 프로젝트 고유식별 ID
  - artifactId 
    - 프로젝트의 각 기능들을 의미한다고 한다.
    - 버전 없는 jar파일의 이름
    - 특수 문자를 사용하지 않고 소문자로만 작성
  - version : 프로젝트의 version

<br>

[정리] groupId는 프로젝트의 큰 틀, artifactId는 프로젝트의 각 기능을 의미한다.

<br>

<br>
