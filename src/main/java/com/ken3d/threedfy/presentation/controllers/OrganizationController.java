package com.ken3d.threedfy.presentation.controllers;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.common.DtoMapper;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.OrganizationDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    return concatOwnedAndNotOwnedOrganizations(currentUser);
  }

  private List<OrganizationDto> concatOwnedAndNotOwnedOrganizations(User user) {
    List<Organization> notOwnedOrg = user.getOrganizationGroups().stream()
        .map(OrganizationGroup::getOrganization).collect(Collectors.toList());
    List<OrganizationDto> allOrganizationsDto = new ArrayList<>();

    user.getOrganizations().stream()
        .map(o -> mapper.map(o, OrganizationDto.class))
        .map(allOrganizationsDto::add);
    notOwnedOrg.stream()
        .map(o -> mapper.map(o, OrganizationDto.class))
        .map(allOrganizationsDto::add);

    return allOrganizationsDto;
  }

  @RequestMapping(value = "/organization", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void updateCurrentOrganization(OrganizationDto orgDto) {
    Organization organization = mapper.map(orgDto, Organization.class);
    userService.updateCurrentOrganization(organization);
  }

  @RequestMapping(value = "/organization", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void createNewOrganizationAndMakeCurrent(OrganizationDto orgDto) {

  }
}
