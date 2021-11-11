package com.example.classicwebapp.configuration;

import com.example.classicwebapp.components.JwtRequestFilter;
import com.example.classicwebapp.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final MyUserDetailsService myUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurityConfig(
      MyUserDetailsService myUserDetailsService,
      JwtRequestFilter jwtRequestFilter,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.myUserDetailsService = myUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(myUserDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/login", "/resources/**", "/css/**", "/fonts/**", "/img/**").permitAll()
        .antMatchers("/register", "/resources/**", "/css/**", "/fonts/**", "/img/**", "/js/**").permitAll()
        .antMatchers("/users/addNew").permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedPage("/accessDenied")
        .and()
        .formLogin()
        .loginPage("/login").permitAll()
        .and()
        .logout().invalidateHttpSession(true)
        .clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login").permitAll();

  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(myUserDetailsService);
    provider.setPasswordEncoder(bCryptPasswordEncoder);
    return provider;
  }
}
