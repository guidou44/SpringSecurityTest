package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends EntityRepositoryBase<AccountEntityBase>
    implements IEntityRepository<AccountEntityBase> {

  @Autowired
  public AccountRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
