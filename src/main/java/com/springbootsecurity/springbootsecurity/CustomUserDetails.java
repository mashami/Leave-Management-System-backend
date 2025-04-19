package com.springbootsecurity.springbootsecurity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public class CustomUserDetails implements UserDetails {
  private User user;

  public CustomUserDetails() {
    // Default constructor
  }

  public CustomUserDetails(User user) {
    this.user = user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user == null || user.getRole() == null) {
      return Collections.emptySet();
    }
    return Collections.singleton(
      new SimpleGrantedAuthority(user.getRole().toString())
    );
  }

  @Override
  public String getPassword() {
    return user != null ? user.getPassword() : null;
  }

  public String getEmail() {
    return user != null ? user.getEmail() : null;
  }

  @Override
  public String getUsername() {
    return user != null ? user.getEmail() : null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return user != null;
  }

  @Override
  public boolean isAccountNonLocked() {
    return user != null;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user != null;
  }

  @Override
  public boolean isEnabled() {
    return user != null && user.getIsActive();
  }
}
