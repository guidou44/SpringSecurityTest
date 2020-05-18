package com.ken3d.threedfy.domain.user.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

  public Optional<UserAuthDetails> getCurrentContextAuthDetails() {
    return tryCast(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
        UserAuthDetails.class);
  }

  public Object getCurrentCredentials() {
    return SecurityContextHolder.getContext().getAuthentication().getCredentials();
  }

  public void updateContextAuthentication(Authentication updatedAuth) {
    SecurityContextHolder.getContext().setAuthentication(updatedAuth);
  }

  public static <T> Optional<T> tryCast(Object target, Class<T> toClass) {
    try {
      return Optional.ofNullable(toClass.cast(target));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
