package com.ken3d.threedfy.domain.user.registration;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.UserDto;
import com.ken3d.threedfy.presentation.user.exceptions.UserAlreadyExistException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  private final IEntityRepository<AccountEntityBase> accountRepository;

  @Autowired
  public UserService(IEntityRepository<AccountEntityBase> accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public void registerNewUserAccount(UserDto userDto) {
    if (emailExist(userDto.getEmail()) || usernameExist(userDto.getUsername())) {
      throw new UserAlreadyExistException();
    }
  }

  private boolean emailExist(String email) {
    return accountRepository.selectAll(User.class, u -> u.getEmail().equals(email)).size() > 0;
  }

  private boolean usernameExist(String username) {
    return accountRepository.selectAll(User.class, u -> u.getUsername().equals(username)).size() > 0;
  }

  private User from(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setPasswordHash(userDto.getPassword());
    user.setEnabled(true);

    setBasicUserRole(user);
  }

  private void setBasicUserRole(User user) {
    Optional<Role> userRole = accountRepository.select(Role.class, r -> r.getAuthorityLevel() == 0);
    userRole.ifPresent(role -> user.setRoles(new HashSet<>(Collections.singletonList(role))));
  }
}
