package com.ken3d.threedfy.infrastructure.dal.dao;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doAnswer;

import com.flextrade.jfixture.JFixture;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.dao.IEntityRepositoryTest;
import com.ken3d.threedfy.infrastructure.dal.entities.User;
import com.ken3d.threedfy.infrastructure.dal.entities.baseentity.MonitoringEntityBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonitoringRepositoryTest extends IEntityRepositoryTest<MonitoringEntityBase> {

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

    doAnswer(i -> givenExistingDatabase().remove(givenObjectToDelete())).when(SESSION)
        .delete(givenObjectToDelete());

    doAnswer(i -> mockDatabase.add(getFirstObjectToUpdate())).when(SESSION)
        .saveOrUpdate(getFirstObjectToUpdate());
    doAnswer(i -> mockDatabase.add(getSecondObjectToUpdate())).when(SESSION)
        .saveOrUpdate(getSecondObjectToUpdate());

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      updateFirstObject(args[0]);
      return getFirstObjectToUpdate();
    }).when(SESSION).merge(getFirstObjectToUpdate());

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      updateSecondObject(args[0]);
      return getSecondObjectToUpdate();
    }).when(SESSION).merge(getSecondObjectToUpdate());

    willReturn(givenExistingDatabase()).given(QUERY).list();

    willReturn(null).given(SESSION).get(any(Class.class), not(eq(1)));
    willReturn(givenObjectToReturnById(1)).given(SESSION).get(User.class, 1);

  }

  @Test
  public void givenExistingDatabase_whenSelectById_thenItReturnsProperEntity() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    User expectedEntity = givenObjectToReturnById(1);

    Optional<User> actual = repository.select(User.class, 1);

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(expectedEntity);
  }

  @Test
  public void givenExistingDatabase_whenSelectByInvalidId_thenItReturnsOptionalEmpty() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);

    Optional<User> actual = repository.select(User.class, 33);

    assertThat(actual.isPresent()).isFalse();
    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  public void givenExistingDatabase_whenSelectWithFilter_thenItReturnsProperEntity() {
    final String wantedEmail = "test@test.com";
    user4.setEmail(null); //pre-state to fk things up
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    User expectedEntity = user2;

    Optional<User> actual = repository
        .select(User.class, u -> u.getEmail().equals("test@test.com"));

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(expectedEntity);
  }

  @Test
  public void givenExistingDatabase_whenSelectWithMultipleResultFilter_thenItReturnsOptionalEmpty() {
    final String wantedLastName = "Tested";
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);

    Optional<User> actual = repository
        .select(User.class, u -> u.getLastName().equals(wantedLastName));

    assertThat(actual.isPresent()).isFalse();
    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  public void givenExistingDatabase_whenSelectAll_thenItReturnsAllEntities() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);

    List<User> actual = repository.selectAll(User.class);

    assertThat(actual).containsExactlyElementsIn(givenExistingDatabase());
  }

  @Test
  public void givenExistingDatabase_whenSelectAllWithFilter_thenItReturnsProperEntities() {
    final String wantedLastName = "Tested";
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expected = Arrays.asList(user1, user4);

    List<User> actual = repository
        .selectAll(User.class, u -> u.getLastName().equals(wantedLastName));

    assertThat(actual).containsExactlyElementsIn(expected);
  }

  @Test
  public void givenExistingDatabase_whenSelectAllWithInvalidFilter_thenItReturnsEmptyList() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);

    List<User> actual = repository
        .selectAll(User.class, u -> u.getLastName().equals("404 NOT FOUND"));

    assertThat(actual).isEmpty();
  }

  @Test
  public void givenExistingDatabase_whenCreateEntity_thenItAddsEntityToDb() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    expectedDb.add(getFirstObjectToUpdate());

    repository.create(getFirstObjectToUpdate());

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateExistingEntity_thenItOnlyUpdatesEntity() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();

    repository.create(user1);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateMany_thenItAddsEntitiesToDb() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    List<User> dataToAdd = mockData;
    expectedDb.addAll(mockData);

    repository.createMany(mockData);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateManyExisting_thenItOnlyUpdatesEntities() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    List<User> dataToAdd = mockData;

    repository.createMany(Arrays.asList(user1, user2));

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenCreateManyMixedExistingAndNot_thenItOnlyCreateProperEntities() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    expectedDb.add(getSecondObjectToUpdate());

    repository.createMany(Arrays.asList(user1, getSecondObjectToUpdate()));

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
  }

  @Test
  public void givenExistingDatabase_whenUpdateEntity_thenItUpdatesEntityProperInDb() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    User entitySelectableById = user1;
    String updatedLastName = user1.getLastName() + "_UPDATED";
    user1.setLastName(updatedLastName);

    repository.update(user1);
    Optional<User> result = repository.select(User.class, 1);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getLastName()).isEqualTo(updatedLastName);
  }

  @Test
  public void givenExistingDatabase_whenUpdateMany_thenItUpdatesEntitiesProperInDb() {
    IEntityRepository<MonitoringEntityBase> repository = givenEntityRepository(SESSION_FACTORY);
    List<User> expectedDb = givenExistingDatabase();
    User firstEntity = user1;
    String updatedLastName = user1.getLastName() + "_UPDATED";
    user1.setLastName(updatedLastName);

    repository.update(user1);
    Optional<User> result = repository.select(User.class, 1);

    assertThat(givenExistingDatabase()).containsExactlyElementsIn(expectedDb);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getLastName()).isEqualTo(updatedLastName);
  }

  private User getFirstObjectToUpdate() {
    return user5;
  }

  private User getSecondObjectToUpdate() {
    return user6;
  }

  private void updateFirstObject(Object object) {
    user5 = (User) object;
  }

  private void updateSecondObject(Object object) {
    user6 = (User) object;
  }

  private User givenObjectToReturnById(int id) {
    return mockDatabase.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
  }

  private User givenObjectToDelete() {
    return user1;
  }

  private List<User> givenExistingDatabase() {
    return mockDatabase;
  }

  protected IEntityRepository<MonitoringEntityBase> givenEntityRepository(
      SessionFactory sessionFactory) {
    return new MonitoringRepository(sessionFactory);
  }
}
