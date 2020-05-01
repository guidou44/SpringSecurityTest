package com.ken3d.threedfy;

import com.ken3d.threedfy.domain.user.Authority;
import com.ken3d.threedfy.domain.user.UserAuthDetails;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestWebSecurityConfiguration {

  @Bean
  @Primary
  public UserDetailsService userDetailsService() {
    User basicUser = new User();
    basicUser.setFirstName("Jonh");
    basicUser.setLastName("Wick");
    basicUser.setPasswordHash("pWord");
    basicUser.setUsername("user");
    basicUser.setRoles(basicRoles());
    Collection<Authority> authoritiesUser = from(basicUser.getRoles());

    UserAuthDetails basicUserDetails = new UserAuthDetails(basicUser.getUsername(),
        basicUser.getPasswordHash(), true, authoritiesUser);

    User managerUser = new User();
    managerUser.setFirstName("Phil");
    managerUser.setLastName("Wick");
    managerUser.setPasswordHash("pWord2");
    managerUser.setUsername("manager");
    managerUser.setRoles(basicRoles());
    Collection<Authority> authoritiesManager = from(managerUser.getRoles());

    UserAuthDetails managerUserDetails = new UserAuthDetails(managerUser.getUsername(),
        managerUser.getPasswordHash(), true, authoritiesManager);

    return new InMemoryUserDetailsManager(Arrays.asList(
        basicUserDetails, managerUserDetails
    ));
  }

  private Set<Role> basicRoles() {
    Role basicRole = new Role();
    basicRole.setAuthorityLevel(0);
    basicRole.setName("USER");
    return new HashSet<>(Collections.singletonList(basicRole));
  }

  private Set<Role> managerRoles() {
    Role managerRole = new Role();
    managerRole.setAuthorityLevel(1);
    managerRole.setName("MANAGER");
    Set<Role> roles = basicRoles();
    roles.add(managerRole);
    return roles;
  }

  private Collection<Authority> from(Set<Role> roles) {
    return roles.stream().map(Authority::new).collect(Collectors.toSet());
  }

}
