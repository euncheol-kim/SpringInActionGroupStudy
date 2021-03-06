> # ๐ Hypermedia Driven RESTful Web Services - Spring HATEOAS(ํค๋์ค์ค)
### ๐ ๋ชฉํ
- 1) HATEOAS๋ 
- 2) HATEOAS์ ๋ชฉ์ 
- 3) HATEOAS์ ์ฅ๋จ์ 

## ๐ก REST maturity model : REST ์ฑ์๋ ๋ชจ๋ธ
![image](https://user-images.githubusercontent.com/55049159/120597342-e235cf00-c47f-11eb-96e5-54a1d9b34c32.png)
- level 0 ~ 3 | ์ด์ ๊น์ง ๊ตฌํ ํ ๊ฒ์ level 2 
- level 1 : url ๋ท ๋ถ๋ถ์ ์ฐ์ฐ์ ๊ดํด ๋ฃ์ด์ค๋ค. ์ก์์ ๋ํด ๋ณ๋์ URL ์ ๋ฃ์ด์ค๋ค. 
- level 2 : Resouces +  ์ฐ์ฐ์ ํ๊ธฐ ์ํด HTTP method๋ฅผ ์ง์   (๋๋ถ๋ถ์ ํ๋ก์ ํธ๊ฐ level2์ ๊ธฐ๋ฐ)
- level 3 : HATEOAS (level 2 + extra links to navigate through API) request๋ ์ฐจ์ด๊ฐ ์์ง๋ง, ๋งํฌ๊ฐ ๋ค์ด๊ฐ๋ค. 
- ์๋ฒฝํ RESTful API๋ฅผ ๊ตฌํํ๊ธฐ ์ํด HATEOAS๋ฅผ ๊ณ ๋ คํด์ผ ํ๋ค.

## ๐ก What is HATEOAS?
- Hypermedia As The Engine Of Application State -
- One of the constraints of the REST architecture
- Server response in the form of JSON+HAL
- Such a resource consists of two parts: 
1) data
2) links to actions that are possible to be performed on a given resource
  ![image](https://user-images.githubusercontent.com/55049159/120597953-a818fd00-c480-11eb-9254-8d1dedce5e7d.png)
- Data content, Links ๋ก ๊ตฌ๋ถ  
- level 2 ์์๋ Data content ๊น์ง๋ง ์ ๋ฌํจ. 
1) self : ์ด์์ฒด (์๊ธฐ์์  ๋งํฌ)
2) direct_movies : ๋งํฌ๋ฅผ ์ซ์๊ฐ๋ฉด, ์ด ๊ฐ๋์ด ๋ง๋  ๋งํฌ๋ก ์ด๋ 
3) directors : ๋ชจ๋  ๊ฐ๋์ ๋ํ List ๋งํฌ๋ก ์ด๋ 
=> ์ฐ๊ด๋ ๋งํฌ๋ ํจ๊ป ๋ด์์ ๋ณด๋ด์ฃผ๋๊ฒ์ด Hypermedia. <br>
=> ์๋ต์ผ๋ก์ ์ํ์ ๋ณด ๋ฟ ์๋๋ผ Hypermedia๋ฅผ ๋งํฌ์ ๋ด์์ ๋ณด๋ด์ฃผ๋ ๊ฒ์ด HATEOAS๋ผ๊ณ  ๋ณด๋ฉด๋๋ค. 
 
<hr>

### โ  HATEOAS ๋? (Hypermedia = Data + Links )
- REST API ์์, HATEOAS๋ ์๋ต์ผ๋ก์ Hypermedia๋ฅผ ๋ณด๋ด์ฃผ๋๋ฐ HATEOAS๋ ๋ฐ์ดํฐ ์ ๋ณด ๋ฟ ์๋๋ผ, ์ฐ๊ด๋ ์ ๋ณด๋ ํจ๊ป ๋งํฌ์ ๋ฃ์ด์ ๋ณด๋ด์ค๋ค. 
- HATEOAS principle client์๊ฒ the next potential steps์ ๋ํ ๊ด๋ จ๋ ์ ๋ณด๋ฅผ ๋ฆฌํดํด์ค์ผ ํ๋ค. 
- Return ๊ฐ์ ๋ณด๋ฉด ํ๋์ ํ์์ด ๋๋ค.
- ์ข๋ ์๋ฒฝํ Rest Service๋ฅผ ๊ตฌํํ  ์ ์๋๋ก ๋์์ค๋ค. 

### โ  HATEOAS ๋ชฉ์ ? 
- ๋์ ์์ค์์ ํด๋ผ์ด์ธํธ, ์๋ฒ๋ฅผ ๋ถ๋ฆฌ์ํค๊ธฐ ์ํจ. ๋ถ๋ฆฌ์ํด์ผ๋ก์จ ๊ฐ๊ฐ ๋๋ฆฝ์ ์ผ๋ก "์งํ" ํ  ์ ์๋๋ก ํจ.
- ์๋ฒ์ ํด๋ผ์ด์ธํธ๋ ๊ณ์ ๊ฐ๋ฐ๋  ๊ฒ์ด๊ธฐ ๋๋ฌธ์, ์๋ฒ๋ฅผ ์์ ํ๋ฉด ํด๋ผ์ด์ธํธ๋ ๊ณ ์น๊ณ  ํด์ผ๋๋๋ฐ  url์ด ์๋ฒ์์ฅ์์ ๋ณ๊ฒฝ์ด ๋  ์ ์๋ค.
- ์๋ฒ๋ฅผ ๊ณ ์น๋ฉด, ํด๋ผ์ด์ธํธ๊ฐ ๋ณ๊ฒฝ๋๋ ๊ฒฝ์ฐ๋ฅผ ๋ง์. 
- url์ด ๋ณ๊ฒฝ๋์ด๋, client์์ฅ์์ ์ด๋ฆ์ ํตํด ์ ๊ทผํ๊ธฐ ๋๋ฌธ์ ํด๋ผ์ด์ธํธ์์ ์ํฅ์ด ์๋ค.  
=> Application์ ์ค๊ณ ? => ์ด๋ฐ๋ถ๋ถ์ ์ค๊ณ ํ๋ ๊ฒ. 

### HAL(Hypertext Application Language) Model 
![image](https://user-images.githubusercontent.com/55049159/120600176-6b9ad080-c483-11eb-86fc-5e1a3d67a26e.png)
- Resource : Data Content, Links(Relation + Href) => ์ด๋ฐ ์์ ํํํ๋ ๊ฒ์ด HAL๋ก ํํํ๋ค๊ณ  ํ๋ฉด๋๋ค.  

### HATEOAS์ ์ฅ์ 
- Resouces์ ๋ํด Client์ ์ํฅ์ ์ฃผ์ง ์์ผ๋ฉด์ ๋ณ๊ฒฝํ  ์ ์๋ค. 
- API๋ฅผ ์ฌ์ฉํ ๋, ์ฒ์์ ์ ๊ทผํ  ๋ ๋ณด๊ธฐ ์ด๋ ค์ด๋ฐ ์ข์ Doucmentation์ด ๋  ์ ์๋ค. - ์๋น์ค์ ๊ธฐ๋ฅ์ด ๋ญ์ง,, ๋ฑ๋ฑ ์ ์ ์์. 
- ์คํ๋ง์์๋ ์๋์ ์ผ๋ก  Documentation์ ๋ง๋ค์ด์ค. (Swagger)
- ํ๋ก ํธ์๋์์ ์กฐ๊ฑด์ ๋ง๋ค ๋ ๋จ์ํ๋ค. (๋งํฌ๊ฐ ์๋๋ ์๋๋์  ๋ฐ๋ผ ๋ฒํผ์ ํ์ฑํ์ํค๋ ๊ฒฝ์ฐ)
- ์์กด์ฑ์ ๋ฎ์ถ๊ธฐ ์ํจ. 

### HATEOAS์ ๋จ์ 
- ์ถ๊ฐ์ ์ธ ๋คํธ์ํฌ ์ค๋ฒํค๋๊ฐ ์๋ค. ๋ฐ์ดํฐ ์ ์ก์ฌ์ด์ฆ๊ฐ ์ปค์ง
- ์ปจํธ๋กค๋ฌ ๋ณต์กํด์ง

#### ์ฐธ๊ณ ์๋ฃ :  https://grapeup.com/blog/how-to-build-hypermedia-api-with-spring-hateoas/# 
