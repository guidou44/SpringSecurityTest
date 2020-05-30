package com.ken3d.threedfy.infrastructure.dal.dao;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterEntityBase;
import org.springframework.stereotype.Repository;

@Repository
public class PrinterRepository extends EntityRepositoryBase<PrinterEntityBase>
    implements IEntityRepository<PrinterEntityBase> {

}
