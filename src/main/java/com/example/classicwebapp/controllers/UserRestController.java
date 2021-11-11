package com.example.classicwebapp.controllers;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.DTO.UserRegisterRequestDTO;
import com.example.classicwebapp.models.DTO.UserRegisterResponseDTO;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.models.authentication.authenticationRequest;
import com.example.classicwebapp.models.authentication.authenticationResponse;
import com.example.classicwebapp.services.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

  private final UserService userService;
  private final Logger logger;

  @Autowired
  public UserRestController(UserService userService, Logger logger) {
    this.userService = userService;
    this.logger = logger;
  }

 /* @PostMapping("/register")
  public ResponseEntity registerUser(@RequestBody UserRegisterRequestDTO request)
      throws UsernameRequiredException, PasswordRequiredException, UsernameAndPasswordRequiredException,
      UsernameIsTakenException {
    User user = userService.register(request.getUsername(),request.getPassword());
    logger.info("User succesfully registered.");
    return ResponseEntity.status(HttpStatus.CREATED).body(new UserRegisterResponseDTO(user.getUsername(),user.getUserId(),"Registered succesfully."));
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody authenticationRequest request)
      throws IncorrectPasswordException, IncorrectUsernameException, UsernameRequiredException,
      PasswordRequiredException, UsernameAndPasswordRequiredException {
    String username = request.getUsername();
    String password = request.getPassword();

    String jwt = userService.login(username,password);
    logger.info("User is logged in and authorized.");
    return ResponseEntity.status(HttpStatus.OK).body(new authenticationResponse(jwt,"ok"));
  }*/

}
