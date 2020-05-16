package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {


  private IEntityRepository<AccountEntityBase> accountRepository;

  @Autowired
  public UserAuthenticationService(IEntityRepository<AccountEntityBase> accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Optional<User> user = accountRepository
        .select(User.class, u -> u.getUsername().equals(userName));
    if (user.isPresent()) {
      return buildUserDetails(user.get());
    }

    user = loadUserByEmail(userName);
    if (user.isPresent()) {
      return buildUserDetails(user.get());
    }

    throw new UsernameNotFoundException(userName);
  }

  private Optional<User> loadUserByEmail(String email) {
    return accountRepository.select(User.class, u -> u.getEmail().equals(email));
  }

  private UserDetails buildUserDetails(User user) {
    Collection<Authority> authorities = buildAuthorities(user);

    return new UserAuthDetails(user.getUsername(), user.getPasswordHash(), user.isEnabled(),
        authorities);
  }

  private Collection<Authority> buildAuthorities(User user) {
    Collection<Authority> authorities;
    final int defaultNumberOfCollaborationOrg = 1;
    Set<Organization> allUserOrganizations = user.getOrganizations();

    List<Organization> allCollaborativeNotOwnedOrg =
        allUserOrganizations.stream()
            .filter(Organization::isCollaborative)
            .filter(o -> o.getOwner().getId() != user.getId())
            .collect(Collectors.toList());

    if (allCollaborativeNotOwnedOrg.size() == defaultNumberOfCollaborationOrg) {
      OrganizationGroup highestGroup = getHighestOrgGroupForDefaultOrg(user,
          allCollaborativeNotOwnedOrg);
      authorities = buildAuthorities_internal(user.getRoles(), highestGroup);
    } else {
      authorities = buildAuthorities_internal(user.getRoles());
    }
    return authorities;
  }

  private OrganizationGroup getHighestOrgGroupForDefaultOrg(User user,
      List<Organization> allCollaborativeNotOwnedOrg) {
    Organization defaultOrg = allCollaborativeNotOwnedOrg.get(0);
    List<OrganizationGroup> userGroupsInDefaultOrg =
        user.getOrganizationGroups()
            .stream()
            .filter(og -> og.getOrganization().equals(defaultOrg))
            .collect(Collectors.toList());

    return Collections
        .max(userGroupsInDefaultOrg, Comparator.comparing(OrganizationGroup::getAuthorityLevel));
  }

  private Collection<Authority> buildAuthorities_internal(Collection<Role> roles) {
    return roles.stream().map(Authority::new).collect(Collectors.toSet());
  }

  private Collection<Authority> buildAuthorities_internal(Collection<Role> roles,
      OrganizationGroup orgGroup) {
    return roles.stream().map(r -> new Authority(r, orgGroup)).collect(Collectors.toSet());
  }

}
