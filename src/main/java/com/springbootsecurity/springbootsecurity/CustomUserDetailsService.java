package com.springbootsecurity.springbootsecurity;

import com.springbootsecurity.springbootsecurity.User;
import com.springbootsecurity.springbootsecurity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(
        () ->
          new UsernameNotFoundException("User not found with email: " + email)
      );

    return org
      .springframework.security.core.userdetails.User.withUsername(
        user.getEmail()
      )
      .password(user.getPassword())
      .roles("USER") // or user.getRole() if you have role info
      .build();
  }
}
