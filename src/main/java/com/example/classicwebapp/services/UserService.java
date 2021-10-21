package com.example.classicwebapp.services;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.User;

public interface UserService {

  User register(String username, String password)
      throws UsernameAndPasswordRequiredException, UsernameRequiredException, PasswordRequiredException, UsernameIsTakenException;

  String login(String username, String password)
      throws UsernameAndPasswordRequiredException, UsernameRequiredException, PasswordRequiredException, IncorrectUsernameException, IncorrectPasswordException;

  User createNewUser(String username, String password);

  boolean isUserPresent(String name);

}
