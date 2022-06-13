package springinaction.tacos.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ordersForUser() throws Exception {

        mockMvc.perform(get("/orders/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderList"))
                .andExpect(content().string(
                        containsString("Welcome to...")));
    }
}