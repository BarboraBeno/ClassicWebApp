package com.example.classicwebapp.exceptions.globalExceptionHandler;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.DTO.ErrorDTO;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler extends ResponseEntityExceptionHandler {

 private final Logger logger;

  public UserGlobalExceptionHandler(Logger logger) {
    this.logger = logger;
  }

  @ExceptionHandler(UsernameAndPasswordRequiredException.class)
  public ResponseEntity<Object> usernameAndPasswordRequired(
      UsernameAndPasswordRequiredException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameRequiredException.class)
  public ResponseEntity<Object> usernameRequired(
      UsernameRequiredException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PasswordRequiredException.class)
  public ResponseEntity<Object> passwordRequired(
      PasswordRequiredException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameIsTakenException.class)
  public ResponseEntity<Object> usernameIsTaken(
      UsernameIsTakenException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IncorrectUsernameException.class)
  public ResponseEntity<Object> incorrectUsername(
      IncorrectUsernameException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IncorrectPasswordException.class)
  public ResponseEntity<Object> incorrectPassword(
      IncorrectPasswordException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

}
