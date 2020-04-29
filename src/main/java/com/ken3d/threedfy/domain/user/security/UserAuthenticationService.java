package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.user.Authority;
import com.ken3d.threedfy.domain.user.UserAuthDetails;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Collection;
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
    throw new UsernameNotFoundException(userName);
  }

  private UserDetails buildUserDetails(User user) {
    Collection<Authority> authorities = buildAuthorities(user.getRoles());
    return new UserAuthDetails(user.getUsername(), user.getPasswordHash(), true, authorities);
  }

  private Collection<Authority> buildAuthorities(Collection<Role> roles) {
    return roles.stream().map(Authority::new).collect(Collectors.toSet());
  }

}
