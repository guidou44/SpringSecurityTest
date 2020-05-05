package com.ken3d.threedfy.presentation.user;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.exceptions.UserAlreadyExistException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class IUserServiceTest {

  private static final String FIRST_EMAIL_ADDRESS = "test@test.com";
  private static final String SECOND_EMAIL_ADDRESS = "22@22.com";
  private static final String FIRST_USERNAME = "test123";
  private static final String SECOND_USERNAME = "somethingElse";
  protected List<User> mockUserTable = new ArrayList<>();
  private IEntityRepository<AccountEntityBase> accountRepository;
  private PasswordEncoder encoder;

  @BeforeEach
  void setUp() {
    encoder = new BCryptPasswordEncoder();
    accountRepository = mock(IEntityRepository.class);
  }

  @Test
  public void givenNewUser_whenRegistering_thenItRegisterNewUserProperly() {
    IUserService userService = givenUserService(accountRepository, encoder);
    UserDto user = givenUserDto();

    userService.registerNewUserAccount(user);
    Optional<User> registeredUser = accountRepository.select(User.class, 1);

    assertThat(registeredUser.isPresent()).isTrue();
    assertThat(registeredUser.get().getUsername()).isEqualTo(user.getUsername());
    assertThat(registeredUser.get().getEmail()).isEqualTo(user.getEmail());
    assertThat(registeredUser.get().getPasswordHash()).isNotEqualTo(user.getPassword());
  }

  @Test
  public void givenNewUser_whenRegistering_thenItCreatesNewOrganizationForUserProperly() {
    IUserService userService = givenUserService(accountRepository, encoder);
    UserDto user = givenUserDto();

    userService.registerNewUserAccount(user);
    Optional<Organization> organization = accountRepository.select(Organization.class, 1);

    assertThat(organization.isPresent()).isTrue();
    assertThat(organization.get().getName()).isEqualTo(user.getUsername());
    assertThat(organization.get().getOwner().getUsername()).isEqualTo(user.getUsername());
  }

  @Test
  public void givenAlreadyUsedEmail_whenRegistering_thenItThrowsProperException() {
    IUserService userService = givenUserService(accountRepository, encoder);
    UserDto user = givenUserDto();
    userService.registerNewUserAccount(user);
    willReturn(Arrays.asList(user)).given(accountRepository)
        .selectAll(any(Class.class), any(Predicate.class));
    user.setUsername(SECOND_USERNAME);
    user.setFirstName("somethingElse");
    user.setLastName("somethingElse");

    assertThrows(UserAlreadyExistException.class, () -> userService.registerNewUserAccount(user));
  }

  @Test
  public void givenAlreadyUsedUserName_whenRegistering_thenItThrowsProperException() {
    IUserService userService = givenUserService(accountRepository, encoder);
    UserDto user = givenUserDto();
    userService.registerNewUserAccount(user);
    willReturn(Arrays.asList(user)).given(accountRepository)
        .selectAll(any(Class.class), any(Predicate.class));
    user.setEmail(SECOND_EMAIL_ADDRESS);
    user.setFirstName("somethingElse");
    user.setLastName("somethingElse");

    assertThrows(UserAlreadyExistException.class, () -> userService.registerNewUserAccount(user));
  }

  private UserDto givenUserDto() {
    UserDto user = new UserDto();
    user.setEmail(FIRST_EMAIL_ADDRESS);
    user.setUsername(FIRST_USERNAME);
    user.setFirstName("Test");
    user.setLastName("Tester");
    user.setPassword("pwd123");
    user.setMatchingPassword("pwd123");
    return user;
  }

  protected abstract IUserService givenUserService(
      IEntityRepository<AccountEntityBase> accountRepository, PasswordEncoder encoder);
}
