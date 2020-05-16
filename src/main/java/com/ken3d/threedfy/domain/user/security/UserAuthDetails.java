package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.user.exceptions.MultipleLoggedOrganizationException;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public Organization getLoggedOrganization() {
    List<Organization> organizations = getAuthorities().stream()
        .map(Authority.class::cast)
        .map(Authority::getOrganization)
        .collect(Collectors.toList());

    if (organizations.stream().allMatch(o ->  o.equals(organizations.get(0)))) {
      return organizations.get(0);
    }

    throw new MultipleLoggedOrganizationException();
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
