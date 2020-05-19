package com.ken3d.threedfy.application.printer;

import com.ken3d.threedfy.domain.dao.IEntityRepository;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterCluster;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterEntityBase;
import com.ken3d.threedfy.presentation.printer.IPrinterService;
import com.ken3d.threedfy.presentation.user.IUserService;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PrinterService implements IPrinterService {

  private final IEntityRepository<PrinterEntityBase> printerRepository;
  private final IUserService userService;

  @Autowired
  public PrinterService(IEntityRepository<PrinterEntityBase> printerRepository,
      IUserService userService) {
    this.printerRepository = printerRepository;
    this.userService = userService;
  }

  @Override
  public void createPrinterCluster(PrinterCluster printerCluster) {
    printerCluster.setOrganization(userService.getCurrentUserLoggedOrganization());
    printerRepository.create(printerCluster);
  }

  @Override
  public List<PrinterCluster> getAllPrinterClustersForCurrentOrganization() {
    Organization organization = userService.getCurrentUserLoggedOrganization();
    return new ArrayList<>(organization.getPrinterClusters());
  }
}
