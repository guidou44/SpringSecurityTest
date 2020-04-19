package com.ken3d.threedfy.domain.dao;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import com.flextrade.jfixture.JFixture;
import com.ken3d.threedfy.infrastructure.dal.entities.User;
import java.util.Arrays;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;

public class IEntityRepositoryTest {

  private static final SessionFactory SESSION_FACTORY = mock(SessionFactory.class);
  private static final Session SESSION = mock(Session.class);
  private static final Query QUERY = mock(Query.class);

  private User user1 = new User();
  private User user2;
  private User user3;
  private User user4;

  @BeforeEach
  void setUp() {

    JFixture fixture = new JFixture();
    fixture.customise().circularDependencyBehaviour().omitSpecimen();
    fixture.customise().noResolutionBehaviour().omitSpecimen();
    user2 = fixture.create(User.class);
    user3 = fixture.create(User.class);
    user4 = fixture.create(User.class);

    user1.setEmail("test@test.com");
    user1.setFirstName("Tester");
    user1.setLastName("Tested");
    user1.setUsername("TeSt3000");
    user1.setId(1);

    willReturn(SESSION).given(SESSION_FACTORY).getCurrentSession();
    willReturn(Optional.empty()).given(SESSION).get(User.class, anyInt());
    willReturn(user1).given(SESSION).get(User.class, 1);
    willReturn(QUERY).given(SESSION).createQuery(anyString(), User.class);
    willReturn(Arrays.asList(user1, user2, user3, user4)).given(QUERY).list();

  }

}
