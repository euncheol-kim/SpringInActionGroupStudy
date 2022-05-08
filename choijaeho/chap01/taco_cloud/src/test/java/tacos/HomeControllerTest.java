package tacos;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import tacos.controller.HomeController;

@WebMvcTest(HomeController.class)   	// <1>	어떤 클래스를 테스트하는지 
public class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;   			// <2>	mockMvc주입

  @Test
  public void testHomePage() throws Exception {
    mockMvc.perform(get("/"))    		// <3>	get("/") 을 받으면 
      .andExpect(status().isOk())  		// <4> 200 ok 를 받고
      .andExpect(view().name("home"))   // <5> view name은 home이어야 하고
      .andExpect(content().string(      // <6> content 값이 저거여야 한다는 test
          containsString("Welcome to...")))
      .andDo(print());
  }
}