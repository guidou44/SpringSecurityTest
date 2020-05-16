package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.user.exceptions.AuthorityWithoutLevelException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AuthorityLevelEvaluator extends SecurityExpressionRoot implements
    MethodSecurityExpressionOperations {

  public AuthorityLevelEvaluator(Authentication authentication) {
    super(authentication);
  }

  public boolean hasAtLeastAuthorityOf(int authLevel) {
    Optional<UserAuthDetails> authDetailsOpt = SecurityContextHelper
        .tryCast(this.getPrincipal(), UserAuthDetails.class);

    return authDetailsOpt
        .filter(userAuthDetails -> hasAuthorityGreaterOrEqualThan(userAuthDetails, authLevel))
        .isPresent();
  }

  public boolean hasExactlyAuthorityOf(int authLevel) {
    Optional<UserAuthDetails> authDetailsOpt = SecurityContextHelper
        .tryCast(this.getPrincipal(), UserAuthDetails.class);

    return authDetailsOpt
        .filter(userAuthDetails -> hasAuthorityEqualTo(userAuthDetails, authLevel))
        .isPresent();
  }


  public boolean hasMinimumRole(String role) {
    Optional<UserAuthDetails> authDetailsOpt = SecurityContextHelper
        .tryCast(this.getPrincipal(), UserAuthDetails.class);

    return authDetailsOpt
        .filter(userAuthDetails -> hasAuthority(userAuthDetails, role))
        .isPresent();
  }


  private boolean hasAuthorityGreaterOrEqualThan(UserAuthDetails authentication,
      int requiredLevel) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Authority highestAuthority = authorities.stream().map(Authority.class::cast)
        .max(Comparator.comparing(Authority::getAuthorityLevel)).orElseThrow(
            AuthorityWithoutLevelException::new);

    return highestAuthority.getAuthorityLevel() >= requiredLevel;
  }

  private boolean hasAuthorityEqualTo(UserAuthDetails authentication,
      int requiredLevel) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    return authorities.stream().map(Authority.class::cast)
        .filter(a -> a.getAuthorityLevel() == requiredLevel).collect(Collectors.toList()).size()
        > 0;
  }

  private boolean hasAuthority(UserAuthDetails authentication, String role) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Set<String> roles = authorities.stream()
        .map(Authority.class::cast)
        .map(Authority::getAuthority)
        .map(String::toUpperCase)
        .collect(Collectors.toSet());

    return roles.contains(role.toUpperCase());
  }

  @Override
  public Object getFilterObject() {
    return null;
  }

  @Override
  public void setFilterObject(Object o) {

  }

  @Override
  public Object getReturnObject() {
    return null;
  }

  @Override
  public void setReturnObject(Object o) {

  }

  @Override
  public Object getThis() {
    return null;
  }
}
