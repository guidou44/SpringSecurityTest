package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.baseentity.MonitoringEntityBase;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MonitoringRepository extends EntityRepositoryBase<MonitoringEntityBase>
    implements IEntityRepository<MonitoringEntityBase> {

  public MonitoringRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
