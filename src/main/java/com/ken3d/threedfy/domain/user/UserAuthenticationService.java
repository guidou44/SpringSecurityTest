package com.ken3d.threedfy.domain.User;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Optional;
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
      return toUserDetails(user.get());
    }
    throw new UsernameNotFoundException(userName);
  }

  private UserDetails toUserDetails(User user) {
    String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        .password(user.getPasswordHash())
        .roles(roles)
        .build();
  }
}
