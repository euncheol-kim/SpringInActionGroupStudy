## 1.3 스프링 애플리케이션 작성하기
###  1.3.1 웹 요청 처리하기
컨트롤러 <br>
웹 요청과 응답을 처리하는 컴포넌트<br>
웹브라우저 요청을 상대할 경우 컨트롤러는 선택적으로 모델데이터를 채워 응답하며 브라우저에 반환되는 HTML 을 생성하기 위해 응답의 웹요청을 뷰에 전달<br>

~~~java

@Controller
public class HomeController {

    @GetMapping("/") // 루트 경로 / 의 웹 요청 처리
    public String home() {
        return "home"; // 뷰 이름 반환
    }
}

~~~

###  1.3.2 뷰 정의하기
src/main/resources/templates 에 html file 생성<br>

### 1.3.3 컨트롤러 테스트하기

~~~java

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc 주입

    @Test
    public void testHomepage() throws Exception {
        mockMvc.perform(get("/")) // get 수행
            .andExpect(status().isOk()) // HTTP 200 OK 리턴
            .andExpect(view().name("home")) // home.html view 
            .andExpect(content().string(containsString("Welcome to..."))); // Welcome to... 텍스트 포함
    }
}

~~~

### 1.3.5 스프링 부트 DevTools 알아보기
개발자에게 편리한 도구 제공 <br>

- 코드 변경 시 자동으로 애플리케이션 다시 시작한다 
- 브라우저로 전송되는 리소스가 변경될 때 자동으로 브라우저 새로고침한다
- 템플릿 캐시를 자동으로 비활성화 한다
- h2 데이터베이스를 사용한다면 자동으로 h2 콘솔을 활성화 한다

### 1.3.6 리뷰하기
빌드 명세 <br>
Web, Thymeleaf 의존성은 일부 다른 의존성도 포함 시킨다<br>

- 스프링의 MVC 프레임워크
- 내장된 톰캣
- Thymeleaf, Thymeleaf레이아웃 dialect
- 스프링부트 자동-구성 라이브러리 개입
- 스프링 mvc 를 활성화하기 위해 컨텍스트에 관련된 빈들을 구성한다
- 내장된 톰캣 서버를 컨텍스트에 구성한다
- Thymeleaf 템플릿을 사용하는 스프링 MVC 뷰를 나타내기 위해 Thymeleaf 뷰 리졸버를 구성한다
