package com.example.classicwebapp.controllers;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }


 @PostMapping("/register")
  public String registerUser(User user, RedirectAttributes redir)
     throws UsernameRequiredException, PasswordRequiredException, UsernameAndPasswordRequiredException, UsernameIsTakenException {
    userService.register(user.getUsername(), user.getPassword());

   redir.addFlashAttribute("message",	"You successfully registered! You can now login");
   return "redirect:login";
  }

  @PostMapping("/login")
  public String loginUser(User user)
      throws IncorrectPasswordException, IncorrectUsernameException, UsernameRequiredException, PasswordRequiredException, UsernameAndPasswordRequiredException {
    userService.login(user.getUsername(), user.getPassword());

    return "redirect:index";
  }

}
