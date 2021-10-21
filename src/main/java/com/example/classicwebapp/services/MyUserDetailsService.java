package com.example.classicwebapp.services;

import com.example.classicwebapp.exceptions.UserExceptions.IncorrectUsernameException;
import com.example.classicwebapp.models.authentication.CustomUserDetails;
import com.example.classicwebapp.models.User;
import com.example.classicwebapp.repositories.UserRepository;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public MyUserDetailsService(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isEmpty()) {
      throw new IncorrectUsernameException("No user found.");
    }
    User user = optionalUser.get();
    return new CustomUserDetails(user);
  }
}
