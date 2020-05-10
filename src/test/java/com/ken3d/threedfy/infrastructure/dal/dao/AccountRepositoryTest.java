package com.ken3d.threedfy.infrastructure.dal.dao;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doAnswer;

import com.flextrade.jfixture.JFixture;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.dao.IEntityRepositoryTest;
import com.ken3d.threedfy.infrastructure.dal.dao.exceptions.InvalidEntityIdException;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountRepositoryTest extends IEntityRepositoryTest<AccountEntityBase> {

  private List<User> mockDatabase;
  private List<User> mockData;
  private User user1 = new User();
  private User user2;
  private User user3;
  private User user4;
  private User user5;
  private User user6;

  @BeforeEach
  void setUpMocks() {
    JFixture fixture = new JFixture();
    fixture.customise().circularDependencyBehaviour().omitSpecimen();
    fixture.customise().noResolutionBehaviour().omitSpecimen();
    user2 = fixture.create(User.class);
    user3 = fixture.create(User.class);
    user4 = fixture.create(User.class);
    user5 = fixture.create(User.class);
    user6 = fixture.create(User.class);
    user2.setEmail("test@test.com");
    user1.setFirstName("Tester");
    user1.setLastName("Tested");
    user1.setUsername("TeSt3000");
    user1.setId(1);
    user4.setLastName("Tested");
    mockDatabase = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
    mockData = Arrays.asList(user5, user6);

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      deleteMockDbEntity(args[0]);
      return null;
    }).when(SESSION).delete(any(User.class));

    doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          createMockDbEntity(args[0]);
          return null;
        }
    ).when(SESSION).saveOrUpdate(any(User.class));

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      return updateMockDbEntity(args[0]);
    }).when(SESSION).merge(any(User.class));

    willReturn(givenExistingDatabase()).given(QUERY).list();

    willReturn(null).given(SESSION).get(any(Class.class), not(eq(1)));
    willReturn(givenObjectToReturnById(1)).given(SESSION).get(User.class, 1);

  }

  @Test
  public void givenExistingDatabase_whenSelectById_thenItReturnsProperEntity() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    User expectedEntity = givenObjectToReturnById(1);

    Optional<User> actual = repository.select(User.class, 1);

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(expectedEntity);
  }

  @Test
  public void givenExistingDatabase_whenSelectByInvalidId_thenItReturnsOptionalEmpty() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    Optional<User> actual = repository.select(User.class, 33);

    assertThat(actual.isPresent()).isFalse();
    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  public void givenExistingDatabase_whenSelectWithFilter_thenItReturnsProperEntity() {
    final String wantedEmail = "test@test.com";
    user4.setEmail(null); //pre-state to fk things up
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    User expectedEntity = user2;

    Optional<User> actual = repository
        .select(User.class, u -> u.getEmail().equals("test@test.com"));

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(expectedEntity);
  }

  @Test
  public void givenExistingDatabase_whenSelectWithMultipleResultFilter_thenItReturnsOptionalEmpty() {
    final String wantedLastName = "Tested";
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    Optional<User> actual = repository
        .select(User.class, u -> u.getLastName().equals(wantedLastName));

    assertThat(actual.isPresent()).isFalse();
    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  public void givenExistingDatabase_whenSelectAll_thenItReturnsAllEntities() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    List<User> actual = repository.selectAll(User.class);

    assertThat(actual).containsExactlyElementsIn(givenExistingDatabase());
  }

  @Test
  public void givenExistingDatabase_whenSelectAllWithFilter_thenItReturnsProperEntities() {
    final String wantedLastName = "Tested";
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expected = Arrays.asList(user1, user4);

    List<User> actual = repository
        .selectAll(User.class, u -> u.getLastName().equals(wantedLastName));

    assertThat(actual).containsExactlyElementsIn(expected);
  }

  @Test
  public void givenExistingDatabase_whenSelectAllWithInvalidFilter_thenItReturnsEmptyList() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    List<User> actual = repository
        .selectAll(User.class, u -> u.getLastName().equals("404 NOT FOUND"));

    assertThat(actual).isEmpty();
  }

  @Test
  public void givenExistingDatabase_whenCreateEntity_thenItAddsEntityToDb() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    expectedDb.add(user5);

    repository.create(user5);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateExistingEntity_thenItOnlyUpdatesEntity() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());

    repository.create(user1);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateMany_thenItAddsEntitiesToDb() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    List<User> dataToAdd = mockData;
    expectedDb.addAll(mockData);

    repository.createMany(mockData);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateManyExisting_thenItOnlyUpdatesEntities() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    List<User> dataToAdd = mockData;

    repository.createMany(Arrays.asList(user1, user2));

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateManyMixed_thenItOnlyCreateProperEntitiesAndUpdatesRest() {
    long initialSize = mockDatabase.size();
    String user1InitialName = user1.getLastName();
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    expectedDb.add(user6);

    repository.createMany(Arrays.asList(user1, user6));

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
    assertThat(mockDatabase.size()).isEqualTo(initialSize + 1);
    assertThat(user1.getLastName()).isEqualTo(user1InitialName + "_1");
  }

  @Test
  public void givenExistingDatabase_whenUpdateEntity_thenItUpdatesEntityProperInDb() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    String updatedLastName = user1.getLastName() + "_UPDATED";
    user1.setLastName(updatedLastName);

    repository.update(user1);
    Optional<User> result = repository.select(User.class, 1);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getLastName()).isEqualTo(updatedLastName + "_1");
  }

  @Test
  public void givenExistingDatabase_whenUpdateMany_thenItUpdatesEntitiesProperInDb() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);
    List<User> expectedDb = new ArrayList<>(givenExistingDatabase());
    String updatedLastName1 = user1.getLastName() + "_UPDATED";
    String updatedLastName2 = user2.getLastName() + "_UPDATED";
    user1.setLastName(updatedLastName1);
    user2.setLastName(updatedLastName2);
    int indexOfUser1 = mockDatabase.indexOf(user1);
    int indexOfUser2 = mockDatabase.indexOf(user2);
    List<User> dataToUpdate = Arrays.asList(user1, user2);

    repository.updateMany(dataToUpdate);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
    assertThat(user1.getLastName()).isEqualTo(updatedLastName1 + "_1");
    assertThat(user2.getLastName()).isEqualTo(updatedLastName2 + "_1");
  }

  @Test
  public void givenExistingDatabase_whenDeleteEntity_thenItDeletesEntityProper() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    repository.delete(user1);

    assertThat(mockDatabase).doesNotContain(user1);
  }

  @Test
  public void givenExistingDatabase_whenDeleteById_thenItDeletesEntityProper() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    repository.delete(User.class, 1);

    assertThat(mockDatabase).doesNotContain(user1);
  }

  @Test
  public void givenExistingDatabase_whenDeleteByInvalidId_thenItThrowsProperException() {
    IEntityRepository<AccountEntityBase> repository = givenEntityRepository(SESSION);

    assertThrows(InvalidEntityIdException.class, () -> repository.delete(User.class, 69));
  }

  private User updateMockDbEntity(Object object) {
    User user = (User) object;
    Optional<User> mayBeUser = mockDatabase.stream().filter(u -> u.getId() == user.getId())
        .findFirst();

    if (mayBeUser.isPresent()) {
      user.setLastName(user.getLastName() + "_1");
      int index = mockDatabase.indexOf(mayBeUser.get());
      mockDatabase.set(index, user);
      return mockDatabase.get(index);
    }

    return null;
  }

  private void createMockDbEntity(Object object) {
    User user = (User) object;
    if (!mockDatabase.contains(user)) {
      mockDatabase.add(user);
    } else {
      updateMockDbEntity(user);
    }
  }

  private void deleteMockDbEntity(Object object) {
    User user = (User) object;
    mockDatabase.remove(user);
  }

  private User givenObjectToReturnById(int id) {
    return mockDatabase.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
  }

  private List<User> givenExistingDatabase() {
    return mockDatabase;
  }

  protected IEntityRepository<AccountEntityBase> givenEntityRepository(Session session) {
    AccountRepository repository = new AccountRepository();
    repository.setSession(session);
    return repository;
  }
}
