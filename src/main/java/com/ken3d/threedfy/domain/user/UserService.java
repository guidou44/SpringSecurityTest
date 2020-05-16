package com.ken3d.threedfy.domain.user;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.user.exceptions.InvalidVerificationTokenException;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.VerificationToken;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.UserDto;
import com.ken3d.threedfy.presentation.user.exceptions.UserAlreadyExistException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {

  private final IEntityRepository<AccountEntityBase> accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(IEntityRepository<AccountEntityBase> accountRepository,
      PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User registerNewUserAccount(UserDto userDto) {
    if (emailExist(userDto.getEmail()) || usernameExist(userDto.getUsername())) {
      throw new UserAlreadyExistException();
    }

    User user = from(userDto);
    setBasicUserRole(user);
    user = accountRepository.create(user);

    createNewOrganization(user);

    return user;
  }

  @Override
  public User getUser(String verificationToken) {
    Optional<VerificationToken> tokenEntity = getVerificationToken(verificationToken);
    if (tokenEntity.isPresent()) {
      return tokenEntity.get().getUser();
    }
    throw new InvalidVerificationTokenException();
  }

  @Override
  public void saveRegisteredUser(User user) {
    accountRepository.update(user);
  }

  @Override
  public void createVerificationToken(User user, String token) {
    VerificationToken newAuthToken = new VerificationToken();
    newAuthToken.setToken(token);
    newAuthToken.setUser(user);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    newAuthToken.setExpiryDate(cal.getTime());
    accountRepository.create(newAuthToken);
  }

  @Override
  public Optional<VerificationToken> getVerificationToken(String token) {
    return accountRepository.select(VerificationToken.class, vt -> vt.getToken().equals(token));
  }

  private boolean emailExist(String email) {
    return accountRepository.selectAll(User.class, u -> u.getEmail().equals(email)).size() > 0;
  }

  private boolean usernameExist(String username) {
    return accountRepository.selectAll(User.class, u -> u.getUsername().equals(username)).size()
        > 0;
  }

  private User from(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
    user.setEnabled(false);
    return user;
  }

  private void setBasicUserRole(User user) {
    Optional<Role> userRole = accountRepository.select(Role.class, r -> r.getAuthorityLevel() == 0);
    userRole.ifPresent(role -> user.setRoles(new HashSet<>(Collections.singletonList(role))));
  }

  private void createNewOrganization(User owner) {
    Organization organization = new Organization();
    organization.setCollaborative(false);
    organization.setName(owner.getUsername());
    organization.setOwner(owner);
    accountRepository.create(organization);
  }
}
