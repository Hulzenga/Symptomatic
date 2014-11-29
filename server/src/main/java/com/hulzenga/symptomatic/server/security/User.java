package com.hulzenga.symptomatic.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by jouke on 11/7/14.
 */

public class User implements UserDetails{

  public static UserDetails create(UserType type, String username, String password, String... authorities) {
    return new User(type, username, password, authorities);
  }

  public enum UserType {
    PATIENT, DOCTOR, ADMIN
  }

  private String username;
  private String password;
  //private String salt;

  private final Collection<GrantedAuthority> authorities;
  private UserType type;


  public User(UserType type, String username, String password, String... authorities ) {
    this.type = type;
    this.username = username;
    this.password = password;
    this.authorities = AuthorityUtils.createAuthorityList(authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
