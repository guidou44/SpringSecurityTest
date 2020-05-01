package com.ken3d.threedfy.domain.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;

public abstract class IEntityRepositoryTest<EntityType extends Serializable> {

  protected static final Query QUERY = mock(Query.class);
  protected static final Session SESSION = mock(Session.class);

  @BeforeEach
  void setUp() {
    willReturn(QUERY).given(SESSION).createQuery(anyString(), any(Class.class));
  }

  protected abstract IEntityRepository<EntityType> givenEntityRepository(Session session);
}
