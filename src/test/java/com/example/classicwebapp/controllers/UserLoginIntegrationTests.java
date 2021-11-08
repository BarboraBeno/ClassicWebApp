package com.example.classicwebapp.controllers;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.classicwebapp.models.User;
import com.example.classicwebapp.models.authentication.CustomUserDetails;
import com.example.classicwebapp.models.authentication.authenticationRequest;
import com.example.classicwebapp.repositories.UserRepository;
import com.example.classicwebapp.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
public class UserLoginIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private JwtService jwtService;

  @MockBean
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @MockBean
  private UserRepository userRepository;


  @BeforeEach
  void beforeStart() {
    objectMapper = new ObjectMapper();
  }

  @Test
  void loginSuccesfull() throws Exception {
    authenticationRequest request = new authenticationRequest("hana", "123");

    User user = new User(request.getUsername(), request.getPassword());

    when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));
    when(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

    Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jwt",
            is(jwtService.generateToken(new CustomUserDetails(user)))))
        .andExpect(jsonPath("$.status", is("ok")))
        .andDo(print());
  }

  @Test
  void noParameters() throws Exception {

    authenticationRequest request = new authenticationRequest("", "");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username and password is required.")));
  }

  @Test
  void usernameRequired() throws Exception {

    authenticationRequest request = new authenticationRequest("", "123");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  void passwordRequired() throws Exception {

    authenticationRequest request = new authenticationRequest("hana", "");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password is required.")));
  }

  @Test
  void notCorrectPassword() throws Exception {

    authenticationRequest request = new authenticationRequest("hana", "123");
    String username = "hana";
    String password = "1234";

    User user = new User(username, password);
    when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));
    when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password or username is incorrect.")));
  }

  @Test
  void notCorrectUsername() throws Exception {

    authenticationRequest request = new authenticationRequest("dana", "123");
    String username = "hana";
    String password = "123";

    User user = new User(username, password);
    when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));
    when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password or username is incorrect.")));
  }

}
