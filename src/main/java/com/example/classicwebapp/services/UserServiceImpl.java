package com.example.classicwebapp.services;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectPasswordException;
import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.exceptions.UserExceptions.PasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameAndPasswordRequiredException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameIsTakenException;
import com.example.classicwebapp.exceptions.UserExceptions.UsernameRequiredException;
import com.example.classicwebapp.models.authentication.CustomUserDetails;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.repositories.UserRepository;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  public UserServiceImpl(UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      JwtService jwtService,
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public User register(String username, String password)
      throws UsernameAndPasswordRequiredException, UsernameRequiredException, PasswordRequiredException, UsernameIsTakenException {
    if(username.isEmpty() && password.isEmpty()){
      throw new UsernameAndPasswordRequiredException("Username and password is required.");
    }
    else if(username.isEmpty()){
      throw new UsernameRequiredException("Username is required.");
    }
    else if(password.isEmpty()){
      throw new PasswordRequiredException("Password is required.");
    }
    else if(isUserPresent(username)){
      throw new UsernameIsTakenException("Username is already taken.");
    }
    return createNewUser(username,password);
  }

  @Override
  public String login(String username, String password)
      throws UsernameAndPasswordRequiredException, UsernameRequiredException, PasswordRequiredException, IncorrectUsernameException, IncorrectPasswordException {
    if((username == null || username.isEmpty()) && (password == null || password.isEmpty())){
      throw new UsernameAndPasswordRequiredException("Username and password is required.");
    }
    else if((username == null || username.isEmpty())){
      throw new UsernameRequiredException("Username is required.");
    }
    else if(password == null || password.isEmpty()){
      throw new PasswordRequiredException("Password is required.");
    }

    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new IncorrectUsernameException("User not found. Username is not existing.");
    }
    User user = optionalUser.get();
    if (!(bCryptPasswordEncoder.matches(password, user.getPassword())) || !username
        .equals(user.getUsername())) {
      throw new IncorrectPasswordException("Password or username is incorrect.");
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(username);

    return jwtService.generateToken((CustomUserDetails) userDetails);
  }

  @Override
  public User createNewUser(String username, String password) {
    User user = new User(username,bCryptPasswordEncoder.encode(password));
    return userRepository.save(user);
  }

  @Override
  public boolean isUserPresent(String name) {
    var optionalUser = userRepository.findByUsername(name);
    return optionalUser.isPresent();
  }
}
