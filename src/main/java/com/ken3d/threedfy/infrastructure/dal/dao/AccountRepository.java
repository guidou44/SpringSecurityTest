package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.AccountEntityBase;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends EntityRepositoryBase<AccountEntityBase>
    implements IEntityRepository<AccountEntityBase> {

}
