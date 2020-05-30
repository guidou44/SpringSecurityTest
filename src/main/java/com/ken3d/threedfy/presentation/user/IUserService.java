package com.ken3d.threedfy.presentation.user;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.VerificationToken;
import java.util.Optional;

public interface IUserService {

  User registerNewUserAccount(UserDto userDto);

  User getUserForToken(String verificationToken);

  void saveRegisteredUser(User user);

  void createVerificationToken(User user, String token);

  Optional<VerificationToken> getVerificationToken(String token);

  Organization getCurrentUserLoggedOrganization();

  User getCurrentUser();

  void updateCurrentOrganization(Organization organization);

  void createOrganizationForUserAndSetCurrent(Organization organization);
}
