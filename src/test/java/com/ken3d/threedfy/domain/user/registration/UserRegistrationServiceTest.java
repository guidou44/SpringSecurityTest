package com.ken3d.threedfy.domain.user.registration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.VerificationToken;
import com.ken3d.threedfy.presentation.user.IUserRegistrationService;
import com.ken3d.threedfy.presentation.user.IUserRegistrationServiceTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRegistrationServiceTest extends IUserRegistrationServiceTest {


  private List<Organization> mockOrganizationTable = new ArrayList<>();

  @Override
  protected IUserRegistrationService givenUserService(IEntityRepository<AccountEntityBase> accountRepository,
      PasswordEncoder encoder) {
    setupMocks(accountRepository);
    return new UserRegistrationService(accountRepository, encoder);
  }

  private void setupMocks(IEntityRepository<AccountEntityBase> accountRepository) {

    doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Organization organization = (Organization) args[0];
          organization.setId(1);
          mockOrganizationTable.add(organization);
          return organization;
        }
    ).when(accountRepository).create(any(Organization.class));

    doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          User user = (User) args[0];
          user.setId(1);
          mockUserTable.add(user);
          return user;
        }
    ).when(accountRepository).create(any(User.class));

    doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          VerificationToken token = (VerificationToken) args[0];
          token.setId(1);
          mockTokenTable.add(token);
          return token;
        }
    ).when(accountRepository).create(any(VerificationToken.class));

    doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          User user = (User) args[0];

          Optional<User> alreadyThere = mockUserTable.stream()
              .filter(u -> u.getUsername().equals(user.getUsername())).findFirst();
          if (alreadyThere.isPresent()) {
            int index = mockUserTable.indexOf(alreadyThere.get());
            mockUserTable.set(index, user);
          }

          return user;
        }
    ).when(accountRepository).update(any(User.class));

    doAnswer(invocation -> {
          return mockOrganizationTable.stream().filter(o -> o.getId() == 1).findFirst();
        }
    ).when(accountRepository).select(Organization.class, 1);

    doAnswer(invocation -> {
          return mockTokenTable.stream().filter(o -> o.getId() == 1).findFirst();
        }
    ).when(accountRepository).select(VerificationToken.class, 1);

    doAnswer(invocation -> {
          return mockUserTable.stream().filter(u -> u.getId() == 1).findFirst();
        }
    ).when(accountRepository).select(User.class, 1);
  }
}
