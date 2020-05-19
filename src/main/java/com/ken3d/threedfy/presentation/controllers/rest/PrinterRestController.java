package com.ken3d.threedfy.presentation.controllers.rest;

import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterCluster;
import com.ken3d.threedfy.presentation.common.DtoMapper;
import com.ken3d.threedfy.presentation.printer.IPrinterService;
import com.ken3d.threedfy.presentation.printer.PrinterClusterDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAtLeastAuthorityOf(0)")
public class PrinterRestController {

  private final IPrinterService printerService;
  private final DtoMapper mapper;

  @Autowired
  public PrinterRestController(IPrinterService printerService, DtoMapper mapper) {
    this.printerService = printerService;
    this.mapper = mapper;
  }

  @RequestMapping(value = "/api/printerClusters", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<PrinterClusterDto> getAllPrinterClusters() {

    return printerService.getAllPrinterClustersForCurrentOrganization().stream()
        .map(pc -> mapper.map(pc, PrinterClusterDto.class))
        .collect(Collectors.toList());
  }

  @RequestMapping(value = "/api/printerCluster", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void createPrinterCluster(@RequestBody @Valid PrinterClusterDto printerClusterDto) {
    PrinterCluster cluster = mapper.map(printerClusterDto, PrinterCluster.class);
    printerService.createPrinterCluster(cluster);
  }
}
