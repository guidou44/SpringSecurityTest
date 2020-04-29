package com.ken3d.threedfy.domain.user;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.user.security.UserAuthenticationService;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserAuthenticationServiceTest {

  private static final String USERNAME = "USER";
  private static final String NOT_USERNAME = "NOBODY";
  private static final String PASSWORD = "pSSword";
  private static final String ROLE_MANAGER_NAME = "MANAGER";
  private static final String ROLE_USER_NAME = "USER";
  private static final User USER = mock(User.class);
  private static final Role ROLE_MANAGER = mock(Role.class);
  private static final Role ROLE_USER = mock(Role.class);
  private static final Set<Role> ROLES = new HashSet<>(Arrays.asList(ROLE_MANAGER, ROLE_USER));

  private IEntityRepository<AccountEntityBase> accountRepository = mock(IEntityRepository.class);

  @BeforeEach
  void setUp() {
    willReturn(USERNAME).given(USER).getUsername();
    willReturn(PASSWORD).given(USER).getPasswordHash();
    willReturn(ROLES).given(USER).getRoles();
    willReturn(ROLE_MANAGER_NAME).given(ROLE_MANAGER).getName();
    willReturn(ROLE_USER_NAME).given(ROLE_USER).getName();
  }

  @Test
  public void givenValidUsername_whenAuthUser_thenItSucceedsWithProperUserDetails() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);

    assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
    assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    assertThat(
        userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet()))
        .contains("ROLE_" + ROLE_MANAGER_NAME);
    assertThat(
        userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet()))
        .contains("ROLE_" + ROLE_USER_NAME);
    assertThat(userDetails.getAuthorities().size()).isEqualTo(ROLES.size());
  }

  @Test
  public void givenInvalidUsername_whenAuthUser_thenItThrowsProperException() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.empty());
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository);

    assertThrows(UsernameNotFoundException.class,
        () -> authService.loadUserByUsername(NOT_USERNAME));
  }

}
