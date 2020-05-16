package com.ken3d.threedfy.domain.user.security;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserAuthenticationServiceTest {

  private static final int MANAGER_AUTH_LEVEL = 3;
  private static final int ORG_GROUP_AUTH_LEVEL = 2;
  private static final int USER_AUTH_LEVEL = 0;
  private static final String USERNAME = "USER";
  private static final String NOT_USERNAME = "NOBODY";
  private static final String PASSWORD = "pSSword";
  private static final String ROLE_MANAGER_NAME = "MANAGER";
  private static final String ROLE_USER_NAME = "USER";
  private static final User USER = mock(User.class);
  private static final User OTHER_USER = mock(User.class);
  private static final Role ROLE_MANAGER = mock(Role.class);
  private static final Role ROLE_USER = mock(Role.class);
  private static final Set<Role> ROLES = new HashSet<>(Arrays.asList(ROLE_MANAGER, ROLE_USER));
  private static final Organization ORGANIZATION_COLLABORATIVE = mock(Organization.class);
  private static final Organization ORGANIZATION_NON_COLLABORATIVE = mock(Organization.class);
  private static final Set<Organization> ORGANIZATIONS = new HashSet<>(
      Arrays.asList(ORGANIZATION_COLLABORATIVE, ORGANIZATION_NON_COLLABORATIVE));
  private static final OrganizationGroup ORGANIZATION_GROUP = mock(OrganizationGroup.class);
  private IEntityRepository<AccountEntityBase> accountRepository = mock(IEntityRepository.class);

  @BeforeEach
  void setUp() {
    willReturn(USERNAME).given(USER).getUsername();
    willReturn(PASSWORD).given(USER).getPasswordHash();
    willReturn(ROLES).given(USER).getRoles();
    willReturn(ROLE_MANAGER_NAME).given(ROLE_MANAGER).getName();
    willReturn(ROLE_USER_NAME).given(ROLE_USER).getName();
    willReturn(MANAGER_AUTH_LEVEL).given(ROLE_MANAGER).getAuthorityLevel();
    willReturn(USER_AUTH_LEVEL).given(ROLE_USER).getAuthorityLevel();

    willReturn(ORGANIZATIONS).given(USER).getOrganizations();
    willReturn(2).given(OTHER_USER).getId();
    willReturn(1).given(USER).getId();
    willReturn(ORGANIZATION_COLLABORATIVE).given(ORGANIZATION_GROUP).getOrganization();
    willReturn(new HashSet<>(Collections.singletonList(ORGANIZATION_GROUP))).given(USER)
        .getOrganizationGroups();
    willReturn(ORG_GROUP_AUTH_LEVEL).given(ORGANIZATION_GROUP).getAuthorityLevel();

    willReturn(false).given(ORGANIZATION_NON_COLLABORATIVE).isCollaborative();
    willReturn(true).given(ORGANIZATION_COLLABORATIVE).isCollaborative();
    willReturn(USER).given(ORGANIZATION_NON_COLLABORATIVE).getOwner();
    willReturn(OTHER_USER).given(ORGANIZATION_COLLABORATIVE).getOwner();
  }

  @Test
  public void givenValidUsername_whenAuthUser_thenItSucceedsWithProperUserDetails() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));
    Authority managerAuthority = givenAuthority(ROLE_MANAGER);
    Authority userAuthority = givenAuthority(ROLE_USER);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);

    assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
    assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    assertThat(userDetails).isInstanceOf(UserAuthDetails.class);
    assertThat(userDetails.getAuthorities()).contains(managerAuthority);
    assertThat(userDetails.getAuthorities()).contains(userAuthority);
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

  @Test
  public void givenUserWithNoCollaborativeOrg_whenLoadUser_thenItLoadUserWithGlobalAuthority() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));
    willReturn(new HashSet<>(Collections.singletonList(ORGANIZATION_NON_COLLABORATIVE))).given(USER)
        .getOrganizations();

    Authority managerAuthority = givenAuthority(ROLE_MANAGER);
    Authority userAuthority = givenAuthority(ROLE_USER);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);
    List<Authority> authorities =
        userDetails.getAuthorities().stream().map(a -> (Authority) a).collect(
            Collectors.toList());
    Authority highestAuth = Collections
        .max(authorities, Comparator.comparing(Authority::getAuthorityLevel));

    assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
    assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    assertThat(userDetails).isInstanceOf(UserAuthDetails.class);
    assertThat(userDetails.getAuthorities()).contains(managerAuthority);
    assertThat(userDetails.getAuthorities()).contains(userAuthority);
    assertThat(userDetails.getAuthorities().size()).isEqualTo(ROLES.size());
    assertThat(highestAuth.getAuthorityLevel()).isEqualTo(MANAGER_AUTH_LEVEL);
  }

  @Test
  public void givenUserWithCollaborativeOrg_whenLoadUser_thenItLoadUserWithOrgGroupAuthority() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));

    Authority managerAuthority = givenAuthority(ROLE_MANAGER, ORGANIZATION_GROUP);
    Authority userAuthority = givenAuthority(ROLE_USER, ORGANIZATION_GROUP);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);
    List<Authority> authorities =
        userDetails.getAuthorities().stream().map(a -> (Authority) a).collect(
            Collectors.toList());
    Authority highestAuth = Collections
        .max(authorities, Comparator.comparing(Authority::getAuthorityLevel));

    assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
    assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    assertThat(userDetails).isInstanceOf(UserAuthDetails.class);
    assertThat(userDetails.getAuthorities()).contains(managerAuthority);
    assertThat(userDetails.getAuthorities()).contains(userAuthority);
    assertThat(userDetails.getAuthorities().size()).isEqualTo(ROLES.size());
    assertThat(highestAuth.getAuthorityLevel()).isEqualTo(ORG_GROUP_AUTH_LEVEL);
  }

  private Authority givenAuthority(Role role) {
    return new Authority(role);
  }

  private Authority givenAuthority(Role role, OrganizationGroup orgGroup) {
    return new Authority(role, orgGroup);
  }
}
