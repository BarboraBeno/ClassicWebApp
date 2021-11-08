package com.example.classicwebapp.controllers;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.classicwebapp.models.DTO.UserRegisterRequestDTO;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.models.authentication.CustomUserDetails;
import com.example.classicwebapp.models.authentication.authenticationRequest;
import com.example.classicwebapp.repositories.UserRepository;
import com.example.classicwebapp.services.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void beforeStart() {
    objectMapper = new ObjectMapper();
  }

  @Test
  void registerUserSuccesfull() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("hana","123");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.username", is("hana")))
        .andExpect(jsonPath("$.userId", is(1)))
        .andExpect(jsonPath("$.status", is("Registered succesfully.")))
        .andDo(print());
  }

  @Test
  void registerUsernameAndPasswordRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("","");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username and password is required.")))
        .andDo(print());
  }

  @Test
  void registerUsernameRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("","123");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")))
        .andDo(print());
  }

  @Test
  void registerPasswordRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("hana","");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password is required.")))
        .andDo(print());
  }

  @Test
  void registrationUsernameIsTaken() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("hana", "12345678");
    User firstUser = new User("hana", "12345678");
    userRepository.save(firstUser);

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is already taken.")))
        .andDo(print());
  }
}