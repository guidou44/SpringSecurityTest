package com.ken3d.threedfy.presentation.printer;

import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterCluster;
import java.util.List;

public interface IPrinterService {

  void createPrinterCluster(PrinterCluster printerCluster);

  List<PrinterCluster> getAllPrinterClustersForCurrentOrganization();
}
