package com.tcs.customermanagementapi.controller;


import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tcs.customermanagementapi.repository.CustomerRepository;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
    }

    @Test
    void testCreateAndGetCustomer() throws Exception {
        String json = "{" +
                "\"name\":\"Test User\"," +
                "\"email\":\"testuser@example.com\"," +
                "\"annualSpend\":1500," +
                "\"lastPurchaseDate\":\"2025-05-01T00:00:00\"}";
        MvcResult result = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.tier").value("Gold"))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        String id = response.split("\"id\":\"")[1].split("\"")[0];
        mockMvc.perform(get("/customer/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        String json = "{" +
                "\"name\":\"Update User\"," +
                "\"email\":\"updateuser@example.com\"}";
        MvcResult result = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        String id = response.split("\"id\":\"")[1].split("\"")[0];
        String updateJson = "{" +
                "\"name\":\"Updated Name\"," +
                "\"email\":\"updated@example.com\"}";
        mockMvc.perform(put("/customer/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testInvalidEmail() throws Exception {
        String json = "{" +
                "\"name\":\"Invalid Email\"," +
                "\"email\":\"invalidemail\"}";
        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid email format")));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        String json = "{" +
                "\"name\":\"Delete User\"," +
                "\"email\":\"deleteuser@example.com\"}";
        MvcResult result = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        String id = response.split("\"id\":\"")[1].split("\"")[0];
        mockMvc.perform(delete("/customer/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Customer deleted")));
    }
}
