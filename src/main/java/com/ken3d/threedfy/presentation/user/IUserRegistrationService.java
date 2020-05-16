package com.ken3d.threedfy.presentation.user;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.VerificationToken;
import java.util.Optional;

public interface IUserRegistrationService {

  User registerNewUserAccount(UserDto userDto);

  User getUser(String verificationToken);

  void saveRegisteredUser(User user);

  void createVerificationToken(User user, String token);

  Optional<VerificationToken> getVerificationToken(String token);

}
