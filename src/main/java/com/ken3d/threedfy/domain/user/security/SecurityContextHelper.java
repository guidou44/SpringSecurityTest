package com.ken3d.threedfy.domain.user.security;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

  public Optional<UserAuthDetails> getCurrentContextAuthDetails() {
    return tryCast(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
        UserAuthDetails.class);
  }

  public static <T> Optional<T> tryCast(Object target, Class<T> toClass) {
    try {
      return Optional.ofNullable(toClass.cast(target));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
