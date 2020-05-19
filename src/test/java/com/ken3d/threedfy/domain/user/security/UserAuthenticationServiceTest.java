package com.ken3d.threedfy.domain.user.security;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.user.exceptions.CannotLoadSecurityContextException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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
  private static final String ROLE_IN_ORG_GROUP_NAME = "ADVANCED_USER";
  private static final User USER = mock(User.class);
  private static final User OTHER_USER = mock(User.class);
  private static final Role ROLE_MANAGER = mock(Role.class);
  private static final Role ROLE_IN_ORG = mock(Role.class);
  private static final Role ROLE_USER = mock(Role.class);
  private static final Set<Role> ROLES = new HashSet<>(Arrays.asList(ROLE_MANAGER, ROLE_USER));
  private static final Organization ORGANIZATION_COLLABORATIVE = mock(Organization.class);
  private static final Organization ORGANIZATION_NON_COLLABORATIVE = mock(Organization.class);
  private static final Set<Organization> ORGANIZATIONS_OWNED =
      new HashSet<>(Collections.singletonList(ORGANIZATION_NON_COLLABORATIVE));
  private static final OrganizationGroup ORGANIZATION_GROUP = mock(OrganizationGroup.class);

  private SecurityContextHelper securityContextHelper = mock(SecurityContextHelper.class);
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

    willReturn(ORGANIZATIONS_OWNED).given(USER).getOrganizations();
    willReturn(2).given(OTHER_USER).getId();
    willReturn(1).given(USER).getId();
    willReturn(ORGANIZATION_COLLABORATIVE).given(ORGANIZATION_GROUP).getOrganization();
    willReturn(new HashSet<>(Collections.singletonList(ORGANIZATION_GROUP))).given(USER)
        .getOrganizationGroups();
    willReturn(ORG_GROUP_AUTH_LEVEL).given(ORGANIZATION_GROUP).getHighestAuthorityLevel();
    willReturn(ROLE_IN_ORG).given(ORGANIZATION_GROUP).getHighestRole();
    willReturn(ROLE_IN_ORG_GROUP_NAME).given(ROLE_IN_ORG).getName();

    willReturn(false).given(ORGANIZATION_NON_COLLABORATIVE).isCollaborative();
    willReturn(true).given(ORGANIZATION_COLLABORATIVE).isCollaborative();
    willReturn(USER).given(ORGANIZATION_NON_COLLABORATIVE).getOwner();
    willReturn(OTHER_USER).given(ORGANIZATION_COLLABORATIVE).getOwner();
  }

  @Test
  public void givenValidUsername_whenAuthUser_thenItSucceedsWithProperUserDetails() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));
    willReturn(new HashSet<>(Collections.emptyList())).given(USER).getOrganizationGroups();

    Authority managerAuthority = givenAuthority(ROLE_MANAGER, ORGANIZATION_NON_COLLABORATIVE);
    Authority userAuthority = givenAuthority(ROLE_USER, ORGANIZATION_NON_COLLABORATIVE);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

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
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    assertThrows(UsernameNotFoundException.class,
        () -> authService.loadUserByUsername(NOT_USERNAME));
  }

  @Test
  public void givenUserWithNoCollaborativeOrg_whenLoadUser_thenItLoadUserWithGlobalAuthority() {
    when(accountRepository.select(any(Class.class), any(Predicate.class)))
        .thenReturn(Optional.ofNullable(USER));
    willReturn(new HashSet<>(Collections.emptyList())).given(USER).getOrganizationGroups();

    Authority managerAuthority = givenAuthority(ROLE_MANAGER, ORGANIZATION_NON_COLLABORATIVE);
    Authority userAuthority = givenAuthority(ROLE_USER, ORGANIZATION_NON_COLLABORATIVE);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);
    List<Authority> authorities =
        userDetails.getAuthorities().stream().map(Authority.class::cast)
            .collect(Collectors.toList());
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

    Authority managerAuthority = givenAuthority(ROLE_MANAGER, ORGANIZATION_GROUP,
        ORGANIZATION_COLLABORATIVE);
    Authority userAuthority = givenAuthority(ROLE_USER, ORGANIZATION_GROUP,
        ORGANIZATION_COLLABORATIVE);
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    UserDetails userDetails = authService.loadUserByUsername(USERNAME);
    List<Authority> authorities =
        userDetails.getAuthorities().stream().map(Authority.class::cast)
            .collect(Collectors.toList());
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

  private Authority givenAuthority(Role role, Organization org) {
    return new Authority(role, org);
  }

  private Authority givenAuthority(Role role, OrganizationGroup orgGroup, Organization org) {
    return new Authority(role, orgGroup, org);
  }

  @Test
  public void givenUserWithNoCollaborativeOrg_whenGetLoggedOrg_thenItReturnsUserDefaultOrg() {
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_NON_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(Optional.ofNullable(authDetails)).given(securityContextHelper)
        .getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_NON_COLLABORATIVE);
  }

  @Test
  public void givenUserWithCollaborativeOrg_whenGetLoggedOrg_thenItReturnsProperOrg() {
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(Optional.ofNullable(authDetails)).given(securityContextHelper)
        .getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_COLLABORATIVE);
  }

  @Test
  public void givenNoUserAuthDetails_whenGetLoggedOrg_thenItThrowsProperException() {
    willReturn(Optional.empty()).given(securityContextHelper).getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    assertThrows(CannotLoadSecurityContextException.class,
        authService::getCurrentUserLoggedOrganization);
  }

  @Test
  public void givenLoggedUser_whenGetCurrentUser_thenItReturnsCurrentUser() {
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(USER).given(authDetails).getCurrentUser();
    willReturn(Optional.ofNullable(authDetails)).given(securityContextHelper)
        .getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    User currentUser = authService.getCurrentUser();

    assertThat(currentUser).isEqualTo(USER);
  }

  @Test
  public void givenNoUserAuthDetails_whenGetCurrentUser_thenItThrowsProperException() {
    willReturn(Optional.empty()).given(securityContextHelper).getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    assertThrows(CannotLoadSecurityContextException.class,
        authService::getCurrentUser);
  }

  @Test
  public void givenValidOrganization_whenUpdateOrganization_thenItUpdatesCurrentOrgProper() {
    Authentication authentication = mock(Authentication.class);
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn(USER).given(authDetails).getCurrentUser();
    SecurityContextHelper contextHelper = new SecurityContextHelper();
    SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        contextHelper);

    authService.setCurrentOrganization(ORGANIZATION_NON_COLLABORATIVE);
    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_NON_COLLABORATIVE);
  }

  @Test
  public void givenOrganizationNotOwned_whenUpdateOrganization_thenItUpdatesAuthLevelWithOrgGroup() {
    Authentication authentication = mock(Authentication.class);
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_NON_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn(USER).given(authDetails).getCurrentUser();
    SecurityContextHelper contextHelper = new SecurityContextHelper();
    SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        contextHelper);

    authService.setCurrentOrganization(ORGANIZATION_COLLABORATIVE);
    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();
    Optional<UserAuthDetails> userDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(userDetails.isPresent()).isTrue();
    List<Authority> authorities =
        userDetails.get().getAuthorities().stream().map(Authority.class::cast)
            .collect(Collectors.toList());
    Authority highestAuth = Collections
        .max(authorities, Comparator.comparing(Authority::getAuthorityLevel));

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_COLLABORATIVE);
    assertThat(highestAuth.getAuthorityLevel()).isEqualTo(ORG_GROUP_AUTH_LEVEL);
  }

  @Test
  public void givenOrganizationNotOwned_whenUpdateOrganization_thenItUpdatesAuthRoleWithOrgGroup() {
    Authentication authentication = mock(Authentication.class);
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_NON_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn(USER).given(authDetails).getCurrentUser();
    SecurityContextHelper contextHelper = new SecurityContextHelper();
    SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        contextHelper);

    authService.setCurrentOrganization(ORGANIZATION_COLLABORATIVE);
    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();
    Optional<UserAuthDetails> userDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(userDetails.isPresent()).isTrue();
    List<Authority> authorities =
        userDetails.get().getAuthorities().stream().map(Authority.class::cast)
            .collect(Collectors.toList());
    Authority highestAuth = Collections
        .max(authorities, Comparator.comparing(Authority::getAuthorityLevel));

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_COLLABORATIVE);
    assertThat(highestAuth.getAuthority()).isEqualTo(ROLE_IN_ORG_GROUP_NAME);
  }

  @Test
  public void givenOrganizationOwned_whenUpdateOrganization_thenItAuthorityDoNotChange() {
    Authentication authentication = mock(Authentication.class);
    UserAuthDetails authDetails = mock(UserAuthDetails.class);
    willReturn(ORGANIZATION_COLLABORATIVE).given(authDetails).getLoggedOrganization();
    willReturn(authDetails).given(authentication).getPrincipal();
    willReturn(USER).given(authDetails).getCurrentUser();
    SecurityContextHelper contextHelper = new SecurityContextHelper();
    SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        contextHelper);

    authService.setCurrentOrganization(ORGANIZATION_NON_COLLABORATIVE);
    Organization currentlyLoggedOrg = authService.getCurrentUserLoggedOrganization();
    Optional<UserAuthDetails> userDetails = contextHelper.getCurrentContextAuthDetails();

    assertThat(userDetails.isPresent()).isTrue();
    List<Authority> authorities =
        userDetails.get().getAuthorities().stream().map(Authority.class::cast)
            .collect(Collectors.toList());
    Authority highestAuth = Collections
        .max(authorities, Comparator.comparing(Authority::getAuthorityLevel));

    assertThat(currentlyLoggedOrg).isEqualTo(ORGANIZATION_NON_COLLABORATIVE);
    assertThat(highestAuth.getAuthorityLevel()).isEqualTo(MANAGER_AUTH_LEVEL);
  }

  @Test
  public void givenInvalidOrganization_whenUpdateOrganization_thenItUThrowsProperException() {
    willReturn(Optional.empty()).given(securityContextHelper).getCurrentContextAuthDetails();
    UserAuthenticationService authService = new UserAuthenticationService(accountRepository,
        securityContextHelper);

    assertThrows(CannotLoadSecurityContextException.class,
        () -> authService.setCurrentOrganization(ORGANIZATION_NON_COLLABORATIVE));
  }
}
