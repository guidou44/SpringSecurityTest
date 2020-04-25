package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends EntityRepositoryBase<AccountEntityBase>
    implements IEntityRepository<AccountEntityBase> {

  public AccountRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
