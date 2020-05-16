package com.ken3d.threedfy.domain.user.security;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelperTest {

  private final SecurityContext securityContext = mock(SecurityContext.class);
  private final Authentication authentication = mock(Authentication.class);
  private final UserAuthDetails authDetails = mock(UserAuthDetails.class);

  @BeforeEach
  void setUp() {
    willReturn(authentication).given(securityContext).getAuthentication();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn("test").given(authDetails).getUsername();
  }

  @Test
  public void givenKnownLoggedUser_whenGetCurrentSecurityContext_thenItReturnsProperUser() {
    SecurityContextHolder.setContext(securityContext);
    SecurityContextHelper contextHelper = new SecurityContextHelper();

    Optional<UserAuthDetails> currentAuthDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(currentAuthDetails.isPresent()).isTrue();
    assertThat(currentAuthDetails.get().getUsername()).isEqualTo(authDetails.getUsername());
  }

}
