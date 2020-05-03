package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.user.security.Authority;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthDetails implements UserDetails {

  private final boolean isAccountNonExpired = true;
  private final boolean isAccountNonLocked = true;
  private final boolean isCredentialNonExpired = true;

  private Collection<Authority> grantedAuthorities;
  private String userName;
  private String password;
  private boolean isEnabled;

  public UserAuthDetails(String userName, String password, boolean isEnabled,
      Collection<Authority> grantedAuthorities) {
    this.grantedAuthorities = grantedAuthorities;
    this.userName = userName;
    this.password = password;
    this.isEnabled = isEnabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isAccountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isAccountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isCredentialNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
