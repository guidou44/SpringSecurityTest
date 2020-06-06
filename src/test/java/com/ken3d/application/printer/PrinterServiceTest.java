package com.ken3d.application.printer;

import com.ken3d.threedfy.application.printer.PrinterService;
import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterEntityBase;
import com.ken3d.threedfy.presentation.printer.IPrinterService;
import com.ken3d.threedfy.presentation.printer.IPrinterServiceTest;
import com.ken3d.threedfy.presentation.user.IUserService;

public class PrinterServiceTest extends IPrinterServiceTest {

  @Override
  protected IPrinterService givenPrinterService(IEntityRepository<PrinterEntityBase> repository,
      IUserService userService) {
    return new PrinterService(repository, userService);
  }
}
