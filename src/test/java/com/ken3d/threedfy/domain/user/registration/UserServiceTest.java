package com.ken3d.threedfy.domain.user.registration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.IUserServiceTest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest extends IUserServiceTest {


  private List<Organization> mockOrganizationTable = new ArrayList<>();

  @Override
  protected IUserService givenUserService(IEntityRepository<AccountEntityBase> accountRepository,
      PasswordEncoder encoder) {
    setupMocks(accountRepository);
    return new UserService(accountRepository, encoder);
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
          return mockOrganizationTable.stream().filter(o -> o.getId() == 1).findFirst();
        }
    ).when(accountRepository).select(Organization.class, 1);

    doAnswer(invocation -> {
          return mockUserTable.stream().filter(u -> u.getId() == 1).findFirst();
        }
    ).when(accountRepository).select(User.class, 1);
  }
}
