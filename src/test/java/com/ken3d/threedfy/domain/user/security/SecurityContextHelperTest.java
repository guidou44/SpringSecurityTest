package com.ken3d.threedfy.domain.user.security;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class SecurityContextHelperTest {

  private final Authentication authentication = mock(Authentication.class);
  private final Authentication otherAuthentication = mock(Authentication.class);
  private final UserAuthDetails otherAuthDetails = mock(UserAuthDetails.class);
  private final UserAuthDetails authDetails = mock(UserAuthDetails.class);
  private final User USER = mock(User.class);

  @BeforeEach
  void setUp() {
    willReturn(USER).given(authentication).getCredentials();
    willReturn(USER).given(otherAuthentication).getCredentials();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn(otherAuthDetails).given(otherAuthentication).getPrincipal();
    willReturn("test").given(authDetails).getUsername();
    willReturn("test2").given(otherAuthDetails).getUsername();
  }

  @Test
  public void givenKnownLoggedUser_whenGetCurrentSecurityContext_thenItReturnsProperUser() {
    SecurityContext securityContext = new SecurityContextImpl(authentication);
    SecurityContextHolder.setContext(securityContext);
    SecurityContextHelper contextHelper = new SecurityContextHelper();

    Optional<UserAuthDetails> currentAuthDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(currentAuthDetails.isPresent()).isTrue();
    assertThat(currentAuthDetails.get().getUsername()).isEqualTo(authDetails.getUsername());
  }

  @Test
  public void givenAuthCredentials_whenGetCurrentCredentials_thenItReturnsProperCredentials() {
    SecurityContext securityContext = new SecurityContextImpl(authentication);
    SecurityContextHolder.setContext(securityContext);
    SecurityContextHelper contextHelper = new SecurityContextHelper();

    Object credentials = contextHelper.getCurrentCredentials();

    assertThat(credentials).isEqualTo(authentication.getCredentials());
  }

  @Test
  public void givenNewAuth_whenUpdateCurrentAuthentication_thenItUpdatesProperly() {
    SecurityContext securityContext = new SecurityContextImpl(authentication);
    SecurityContextHolder.setContext(securityContext);
    SecurityContextHelper contextHelper = new SecurityContextHelper();

    contextHelper.updateContextAuthentication(otherAuthentication);
    Optional<UserAuthDetails> currentAuthDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(currentAuthDetails.isPresent()).isTrue();
    assertThat(currentAuthDetails.get().getUsername()).isEqualTo(otherAuthDetails.getUsername());
    assertThat(currentAuthDetails.get().getUsername()).isNotEqualTo(authDetails.getUsername());
  }

}
