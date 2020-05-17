package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.user.exceptions.CannotLoadSecurityContextException;
import com.ken3d.threedfy.domain.user.exceptions.UserWithoutDefaultOrganizationException;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserAuthenticationService implements UserDetailsService {

  private final IEntityRepository<AccountEntityBase> accountRepository;
  private final SecurityContextHelper securityContext;

  @Autowired
  public UserAuthenticationService(IEntityRepository<AccountEntityBase> accountRepository,
      SecurityContextHelper securityContext) {
    this.accountRepository = accountRepository;
    this.securityContext = securityContext;
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

    return new UserAuthDetails(user, authorities);
  }

  private Collection<Authority> buildAuthorities(User user) {
    Collection<Authority> authorities;
    final int defaultNumberOfCollaborationOrg = 1;
    Set<Organization> allUserOrganizations = user.getOrganizations();

    List<Organization> allCollaborativeNotOwnedOrg = getAllCollaborativeNotOwnedOrganization(user,
        allUserOrganizations);

    if (allCollaborativeNotOwnedOrg.size() == defaultNumberOfCollaborationOrg) {
      Organization defaultCollaborativeOrg = allCollaborativeNotOwnedOrg.get(0);
      OrganizationGroup highestGroup = getHighestOrgGroupForDefaultOrg(user,
          defaultCollaborativeOrg);
      authorities = buildAuthorities_internal(user.getRoles(), highestGroup,
          defaultCollaborativeOrg);
    } else {
      Organization defaultNonCollaborativeOrg = getDefaultNonCollaborativeOrganization(user,
          allUserOrganizations);
      authorities = buildAuthorities_internal(user.getRoles(), defaultNonCollaborativeOrg);
    }
    return authorities;
  }

  private List<Organization> getAllCollaborativeNotOwnedOrganization(User user,
      Set<Organization> allUserOrganizations) {
    return allUserOrganizations.stream()
        .filter(Organization::isCollaborative)
        .filter(o -> o.getOwner().getId() != user.getId())
        .collect(Collectors.toList());
  }

  private Organization getDefaultNonCollaborativeOrganization(User user,
      Set<Organization> allUserOrganizations) {
    Optional<Organization> userDefaultNonCollaborativeOrg = allUserOrganizations.stream()
        .filter(o -> !o.isCollaborative())
        .filter(o -> o.getOwner().getId() == user.getId())
        .findFirst();

    if (userDefaultNonCollaborativeOrg.isPresent()) {
      return userDefaultNonCollaborativeOrg.get();
    }

    throw new UserWithoutDefaultOrganizationException();
  }

  private OrganizationGroup getHighestOrgGroupForDefaultOrg(User user, Organization defaultOrg) {
    List<OrganizationGroup> userGroupsInDefaultOrg =
        user.getOrganizationGroups()
            .stream()
            .filter(og -> og.getOrganization().equals(defaultOrg))
            .collect(Collectors.toList());

    return Collections
        .max(userGroupsInDefaultOrg, Comparator.comparing(OrganizationGroup::getAuthorityLevel));
  }

  private Collection<Authority> buildAuthorities_internal(Collection<Role> roles,
      Organization loggedOrg) {
    return roles.stream().map(r -> new Authority(r, loggedOrg)).collect(Collectors.toSet());
  }

  private Collection<Authority> buildAuthorities_internal(Collection<Role> roles,
      OrganizationGroup orgGroup, Organization loggedOrg) {
    return roles.stream().map(r -> new Authority(r, orgGroup, loggedOrg))
        .collect(Collectors.toSet());
  }

  public Organization getCurrentUserLoggedOrganization() {
    Optional<UserAuthDetails> userDetails = securityContext.getCurrentContextAuthDetails();
    if (userDetails.isPresent()) {
      return userDetails.get().getLoggedOrganization();
    }
    throw new CannotLoadSecurityContextException();
  }

  public User getCurrentUser() {
    Optional<UserAuthDetails> userDetails = securityContext.getCurrentContextAuthDetails();
    if (userDetails.isPresent()) {
      return userDetails.get().getCurrentUser();
    }
    throw new CannotLoadSecurityContextException();
  }
}
