----



### **๐จโ๐จโ๐ฆโ๐ฆ SpringInAction Group Study [์ด์ฑ๋ฏผ, ๊ถํ๊ตฌ, ์ต์ฌํธ, ๊ณฝํ๊ธฐ, ๊น์์ฒ ]**

**group - GIT : https://github.com/euncheol-kim/SpringInActionGroupStudy**

<br>

#### **์ด์ฑ๋ฏผ ๋ : https://github.com/CokeLee777**

#### **๊ถํ๊ตฌ ๋ : https://github.com/Hashtae9**

#### **์ต์ฌํธ ๋ : https://github.com/jaero0725**

#### **๊ณฝํ๊ธฐ ๋ : https://github.com/nicebyy**

#### **๊น์์ฒ  (๋ณธ์ธ): https://github.com/euncheol-kim**

<br>

#### ์์ฑ์ : ๊น์์ฒ  

<br>

-----

<br>

<br>

### goal

> view ํํ๋ฆฟ ๋ผ์ด๋ธ๋ฌ๋ฆฌ ์ ํํ๊ธฐ

<br>

# 1. ๋ทฐ ํํ๋ฆฟ ๋ผ์ด๋ธ๋ฌ๋ฆฌ ์ ํํ๊ธฐ

## [1] ์คํ๋ง์์ ์ง์ํ๋ ํํ๋ฆฟ

| ํํ๋ฆฟ(์์ง)          | ์คํ๋ง ๋ถํธ ์คํํฐ ์์กด์ฑ                             |
| --------------------- | ----------------------------------------------------- |
| FreeMarker            | spring-boot-starter-freemarker                        |
| Groovy ํํ๋ฆฟ         | spring-boot-starter-groovy-templates                  |
| JavaServer Pages(JSP) | ์์ (Tomcat์ด๋ Jetty ์๋ธ๋ฆฟ ์ปจํ์ด๋ ์์ฒด์์ ์ ๊ณต) |
| Mustache              | spring-boot-starter-groovy-mustache                   |
| **Thymeleaf**         | spring-boot-starter-thymeleaf                         |

<br>

## [2] Fackaging์ ๋ฐ๋ฅธ ํํ๋ฆฟ ํธํ ์ ๋ฌด

| Fackaging | ํธํ๋๋ ํํ๋ฆฟ                                  |
| --------- | ------------------------------------------------ |
| Jar       | JSP๋ฅผ ์ ์ธํ Springboot์์ ํธํํ๋ ๋ชจ๋  ํํ๋ฆฟ |
| War       | JSP๋ง์ ์ทจ๊ธ                                     |

> WAR๋ฐฐํฌ ์ฌ๋ก
>
> - SpringBoot JSP-WAR ๋ฐฐํฌ, https://vakhais.github.io/develop/Spring-boot-war%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0!/, ์๊ธ๋ฃจํก ๊ฐ๋ฐ์

<br>

<br>

# 2. ํํ๋ฆฟ ์บ์ฑ

๊ธฐ๋ณธ์ ์ผ๋ก ํํ๋ฆฟ์ ์ต์กฐ ์ฌ์ฉ๋  ๋ ํ ๋ฒ๋ง ํ์ฑ(์ฝ๋ ๋ถ์)๋๋ค.<br>

๊ทธ๋ฆฌ๊ณ  **<u>ํ์ฑ๋ ๊ฒฐ๊ณผ๋ ํฅํ ์ฌ์ฉ์ ์ํด ์บ์์ ์ ์ฅ๋๋ค.<br></u>**

ํ์ง๋ง <u>๊ฐ๋ฐ์ด ์ด๋ฃจ์ด์ง๋ ๋จ๊ณ์์ ์บ์ฑ๋ ํํ๋ฆฟ์ ๋งค๋ฒ ๋ถ๋ฌ์จ๋ค๋ฉด, ๊ฐ๋ฐ์์ ์์ ์ฌํญ์ด ๋ฐ๋ก ๋ฐ๋ก ์ ์ฉ์ด ๋์ง ์์ ์ฝ๋๊ฐ ์์ ๋์ด์ง ๋๋ง๋ค ์ ํ๋ฆฌ์ผ์ด์์ ๋ค์ ์คํํด์ผํ๋ค.</u><br>

<br>

## [1] ํํ๋ฆฟ ์บ์ฑ ๋นํ์ฑํ

์์ ๊ฐ์ ๋ฒ๊ฑฐ๋ก์์ ๋ง๊ธฐ ์ํด์๋ ๊ฐ ํํ๋ฆฟ์ ์บ์ฑ ์์ฑ๋ง false๋ก ์ค์ ํ๋ฉด ๋๋ค.<br>

๊ฐ๊ฐ์ ํํ๋ฆฟ์ ์บ์ฑ ๋นํ์ฑํ๋ ์๋ ํ์ ๊ฐ๋ค.<br>

| ํํ๋ฆฟ(์์ง)     | ์บ์ฑ ์์ฑ                    |
| ---------------- | ---------------------------- |
| FreeMarker       | spring.freemarker.cache      |
| Groovy Templates | spring.groovy.template.cache |
| Mustache         | spring.mustache.cache        |
| Thymeleaf        | spring.thymeleaf.cache       |

๋จ, ํ๋ก๋์์์ ์ ํ๋ฆฌ์ผ์ด์์ ๋ฐฐํฌํ  ๋๋ ๋ฐฉ๊ธ ์ถ๊ฐํ ์ค์ ์ ์ญ์ ํ๊ฑฐ๋ true๋ก ๋ณ๊ฒฝํด์ผํ๋ค.<br>

**ํ์ง๋ง**, <u>์คํ๋ง ๋ถํธ์ DevTools๋ฅผ ์ฌ์ฉํ๋ ๊ฒ์ด ํจ์ฌ ๋ ์ฝ๋ค</u>. SpringInAction์ 1์ฅ์์ ์ธ๊ธํ๋ฏ์ด, DevTools๋ฅผ ์์กด์ฑ ์ค์ ํด๋๋ค๋ฉด ๊ฐ๋ฐ ์์ ์ ๋ง์ ๋์์ ์ ๊ณตํ๋ฉฐ, ๋ชจ๋  ํํ๋ฆฟ ๋ผ์ด๋ธ๋ฌ๋ฆฌ์ ์บ์ฑ์ ๋นํ์ฑํ ํ๋ค.<br>

๊ทธ๋ฌ๋ ์ ํ๋ฆฌ์ผ์ด์์ด ์ค๋ฌด ์ด์์ ์ํด ๋ฐฐํฌ๋  ๋๋ DevTools ์์ ์ด ๋นํ์ฑํ ๋๋ฏ๋ก ํํ๋ฆฟ ์บ์ฑ์ด ํ์ฑํ ๋  ์ ์๋ค.<br>

<br>

<br>

<h1 style="color:blue">์ถ๊ฐ์๋ฃ</h1>

# [1] ํํ๋ฆฟ

## 1. Mustache, Thymeleaf

| Mustache  | ํน์ง                                                         |
| --------- | ------------------------------------------------------------ |
| Mustache  | - ๋ฌธ๋ฒ์ด ๋ค๋ฅธ ํํ๋ฆฟ ์์ง๋ณด๋ค ์ฌํํ๋ค.<br />- ๋ก์ง ์ฝ๋๋ฅผ ์ฌ์ฉํ  ์ ์์ด, View์ ์ญํ ๊ณผ ์๋ฒ์ ์ญํ ์ ๋ชํํ๊ฒ ๊ตฌ๋ถํ๋ค.<br />- ํ์ฅ์ : **๋ธ๋ผ๋ธ๋ผ**.mustache |
| Thymeleaf | - HTML ํ๊ทธ์ ์์ฑ์ผ๋ก ํํ๋ฆฟ ๊ธฐ๋ฅ์ ์ฌ์ฉํ๋ ๋ฐฉ์์ด ์ด๋ ค์ธ ์ ์์<br />- Vue.js ํ๊ทธ ์์ฑ ๋ฐฉ์๊ณผ ๋น์ทํ๋ค. |

<br>

##  2. ํํ๋ฆฟ ์์ง๊ณผ ๊ด๋ จ๋ ๊ธ

> ์ฐธ๊ณ ๋งํฌ : FreeMarker vs Groovy vs Mustache vs Thymeleaf, https://springhow.com/spring-boot-template-engines-comparison/, Raja Anbazhagan

<br>

## 3. ํธํ์ฑ๊ณผ ๊ด๋ จํ์ฌ

- thymeleaf๊ฐ ์งฑ์ด๋ผ๊ณ  ํฉ๋๋ค.

<br>

<br>

# [2] ๋ฐํ ํํธ์ ๋ชฐ๋๋ ๋ด์ฉ์ ๋ฆฌ(๋ฏธํกํ๋ ๋ถ๋ถ)

## [1] Maven, pom.xml

- \<dependency> ... \<dependency> ๋ด๋ถ ํ๊ทธ
  - groupId : ํ๋ก์ ํธ ๊ณ ์ ์๋ณ ID
  - artifactId 
    - ํ๋ก์ ํธ์ ๊ฐ ๊ธฐ๋ฅ๋ค์ ์๋ฏธํ๋ค๊ณ  ํ๋ค.
    - ๋ฒ์  ์๋ jarํ์ผ์ ์ด๋ฆ
    - ํน์ ๋ฌธ์๋ฅผ ์ฌ์ฉํ์ง ์๊ณ  ์๋ฌธ์๋ก๋ง ์์ฑ
  - version : ํ๋ก์ ํธ์ version

<br>

[์ ๋ฆฌ] groupId๋ ํ๋ก์ ํธ์ ํฐ ํ, artifactId๋ ํ๋ก์ ํธ์ ๊ฐ ๊ธฐ๋ฅ์ ์๋ฏธํ๋ค.

<br>

<br>
