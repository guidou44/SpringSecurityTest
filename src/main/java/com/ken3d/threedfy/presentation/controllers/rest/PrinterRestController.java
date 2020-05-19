package com.ken3d.threedfy.presentation.controllers.rest;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.presentation.common.DtoMapper;
import com.ken3d.threedfy.presentation.printer.PrinterClusterDto;
import com.ken3d.threedfy.presentation.user.IUserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAtLeastAuthorityOf(0)")
public class PrinterRestController {

  private final IUserService userService;
  private final DtoMapper mapper;

  @Autowired
  public PrinterRestController(IUserService userService, DtoMapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
  }

  @RequestMapping(value = "/api/printerClusters", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PrinterClusterDto> getAllPrinterClusters() {
    Organization organization = userService.getCurrentUserLoggedOrganization();

    return organization.getPrinterClusters().stream()
        .map(pc -> mapper.map(pc, PrinterClusterDto.class))
        .collect(Collectors.toList());
  }
}
