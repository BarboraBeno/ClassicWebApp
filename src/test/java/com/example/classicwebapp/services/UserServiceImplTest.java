package com.example.classicwebapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.models.authentication.CustomUserDetails;
import com.example.classicwebapp.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceImplTest {

  private UserServiceImpl userService;
  private UserRepository fakeUserRepository;
  private BCryptPasswordEncoder fakeBCryptPasswordEncoder;
  private UserDetailsService userDetailsService;
  private JwtService jwtService;
  private AuthenticationManager authenticationManager;

  @BeforeEach
  void beforeStart(){
    jwtService = new JwtService();
    fakeBCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    fakeUserRepository = Mockito.mock(UserRepository.class);
    authenticationManager = Mockito.mock(AuthenticationManager.class);
    userDetailsService = Mockito.mock(UserDetailsService.class);
    userService = new UserServiceImpl(fakeUserRepository, fakeBCryptPasswordEncoder, jwtService,
        authenticationManager, userDetailsService);
  }

  @Test
  void registerSuccess() {
    String username = "hana";
    String password = "12345678";

    User user = new User(username, password);

    when(fakeUserRepository.save(any())).thenReturn(user);

    assertEquals("hana", userService.createNewUser(username,password).getUsername());
  }

  @Test
  void registerUsernameAndPasswordIsRequired() {
    Exception e = assertThrows(UsernameAndPasswordRequiredException.class,
        () -> userService.register("",""));
    assertEquals("Username and password is required.", e.getMessage());
  }

  @Test
  void registerUsernameIsRequired() {
    Exception e = assertThrows(UsernameRequiredException.class,
        () -> userService.register("","123456"));
    assertEquals("Username is required.", e.getMessage());
  }

  @Test
  void registerPasswordIsRequired() {
    Exception e = assertThrows(PasswordRequiredException.class,
        () -> userService.register("hana",""));
    assertEquals("Password is required.", e.getMessage());
  }

  @Test
  void registerUsernameIsTaken() {

    User user = new User("hana","123");

    Mockito.when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User("hana", null)));
    Exception e = assertThrows(UsernameIsTakenException.class,
        () -> userService.register(user.getUsername(),user.getPassword()));
    assertEquals("Username is already taken.", e.getMessage());
  }

  @Test
  void login()
      throws IncorrectPasswordException, IncorrectUsernameException, UsernameRequiredException, PasswordRequiredException, UsernameAndPasswordRequiredException {

    String username = "hana";
    String password = "12345678";
    User user = new User(username, password);

    Mockito.when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password)));

    Mockito.when(fakeBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

    Mockito.when(fakeUserRepository.findByUsername(any())).thenReturn(Optional.of(user));

    Mockito.when(fakeBCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

    Mockito.when(userDetailsService.loadUserByUsername(any()))
        .thenReturn(new CustomUserDetails(user));

    assertEquals(jwtService.generateToken(new CustomUserDetails(user)), userService.login(user.getUsername(), user.getPassword()));

  }

  @Test
  void loginUsernameAndPasswordRequiredException() {
    Exception e = assertThrows(UsernameAndPasswordRequiredException.class,
        () -> userService.login("", ""));
    assertEquals("Username and password is required.", e.getMessage());
  }

  @Test
  void loginUsernameRequired() {
    Exception e = assertThrows(UsernameRequiredException.class, () -> userService.login("", "123"));
    assertEquals("Username is required.", e.getMessage());
  }

  @Test
  void loginPasswordRequired() {
    Exception e = assertThrows(PasswordRequiredException.class,
        () -> userService.login("hana", ""));
    assertEquals("Password is required.", e.getMessage());
  }

  @Test
  void loginIncorrectPasswordException() {

    when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User("hana", "1234")));

    Exception e = assertThrows(IncorrectPasswordException.class,
        () -> userService.login("hana", "123"));
    assertEquals("Password is incorrect.", e.getMessage());
  }

  @Test
  void loginUserNotFound() {
    when(fakeUserRepository.findByUsername(any())).thenReturn(Optional.empty());

    Exception e = assertThrows(IncorrectUsernameException.class, () -> userService.login("hana", "123"));
    assertEquals("User not found. Username is not existing.", e.getMessage());
  }
}