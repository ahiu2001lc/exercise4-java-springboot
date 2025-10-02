package com.example.exercise4;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
class PublicApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPing() throws Exception {
        mockMvc.perform(get("/public/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pong", is(true)));
    }

    @Test
    void testHello() throws Exception {
        mockMvc.perform(get("/public/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Java Spring-boot!"));
    }

    @Test
    void testBye() throws Exception {
        mockMvc.perform(get("/public/bye"))
                .andExpect(status().isOk())
                .andExpect(content().string("See you again: Alice!"));
    }

    @Test
    void testTime() throws Exception {
        mockMvc.perform(get("/public/time"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The time is: ")));
    }
}
