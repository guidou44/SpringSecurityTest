package com.ken3d.threedfy.presentation.controllers;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.common.DtoMapper;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.OrganizationDto;
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
public class OrganizationController {

  private final IUserService userService;
  private final DtoMapper mapper;

  @Autowired
  public OrganizationController(IUserService userService,
      DtoMapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
  }

  @RequestMapping(value = "/organization", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public OrganizationDto getCurrentOrganization() {
    Organization organization = userService.getCurrentUserLoggedOrganization();
    return mapper.map(organization, OrganizationDto.class);
  }

  @RequestMapping(value = "/organizations", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<OrganizationDto> getAllOrganizations() {
    User currentUser = userService.getCurrentUser();

    return currentUser.getOrganizations().stream()
        .map(o -> mapper.map(o, OrganizationDto.class)).collect(Collectors.toList());
  }

  @RequestMapping(value = "/organization", method = RequestMethod.PUT)
  public void updateCurrentOrganization(OrganizationDto orgDto) {

  }

  @RequestMapping(value = "/organization", method = RequestMethod.POST)
  public void createNewOrganizationAndMakeCurrent(OrganizationDto orgDto) {

  }
}
