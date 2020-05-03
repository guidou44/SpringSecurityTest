package com.ken3d.threedfy.domain.user.registration;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.UserDto;
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
  public boolean registerNewUserAccount(UserDto userDto) {
    return false;
  }

  private boolean emailExist(String email) {
    return accountRepository.selectAll(User.class, u -> u.getEmail().equals(email)).size() > 0;
  }

  private boolean usernameExist(String username) {
    return accountRepository.selectAll(User.class, u -> u.getUsername().equals(username)).size() > 0;
  }
}
