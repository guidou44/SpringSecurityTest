package com.ken3d.threedfy.domain.user.security;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.domain.user.exceptions.MultipleLoggedOrganizationException;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserAuthDetailsTest {

  private static final String USERNAME = "testUserName";
  private static final String PASSWORD = "testPassword";
  private static final Organization ORGANIZATION = mock(Organization.class);

  private Collection<Authority> grantedAuthorities;

  @BeforeEach
  void setUp() {

  }

  @Test
  public void givenAuthoritiesWithoutOrgGroup_whenGetOrganization_itReturnsProperOrg() {
    grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));

    UserAuthDetails authDetails = new UserAuthDetails(mock(User.class), grantedAuthorities);

    assertThat(authDetails.getLoggedOrganization()).isEqualTo(ORGANIZATION);
  }

  @Test
  public void givenAuthoritiesWithOrgGroup_whenGetOrganization_itReturnsProperOrg() {
    grantedAuthorities = new ArrayList<>();
    grantedAuthorities
        .add(new Authority(mock(Role.class), mock(OrganizationGroup.class), ORGANIZATION));
    grantedAuthorities
        .add(new Authority(mock(Role.class), mock(OrganizationGroup.class), ORGANIZATION));
    grantedAuthorities
        .add(new Authority(mock(Role.class), mock(OrganizationGroup.class), ORGANIZATION));

    UserAuthDetails authDetails = new UserAuthDetails(mock(User.class), grantedAuthorities);

    assertThat(authDetails.getLoggedOrganization()).isEqualTo(ORGANIZATION);
  }

  @Test
  public void givenAuthoritiesWithDifferentOrg_whenGetOrg_thenItThrowsProperException() {
    final Organization otherOrg = mock(Organization.class);
    grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), otherOrg));

    UserAuthDetails authDetails = new UserAuthDetails(mock(User.class), grantedAuthorities);

    assertThrows(MultipleLoggedOrganizationException.class, authDetails::getLoggedOrganization);
  }

  @Test
  public void givenUser_whenCreateAuthDetails_thenItSetsCurrentLoggedUserProperly() {
    grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    User user = new User();
    user.setUsername(USERNAME);
    user.setPasswordHash(PASSWORD);
    user.setEnabled(false);

    UserAuthDetails authDetails = new UserAuthDetails(user, grantedAuthorities);

    assertThat(authDetails.getUsername()).isEqualTo(user.getUsername());
    assertThat(authDetails.getPassword()).isEqualTo(user.getPasswordHash());
    assertThat(authDetails.isEnabled()).isEqualTo(user.isEnabled());
  }

  @Test
  public void givenAuthDetailsForUser_whenGetCurrentUser_thenItReturnsCurrentUser() {
    grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    grantedAuthorities.add(new Authority(mock(Role.class), ORGANIZATION));
    User user = new User();
    user.setUsername(USERNAME);
    user.setPasswordHash(PASSWORD);
    user.setEnabled(false);

    UserAuthDetails authDetails = new UserAuthDetails(user, grantedAuthorities);

    assertThat(authDetails.getCurrentUser()).isEqualTo(user);
  }

}
