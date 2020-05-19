package com.ken3d.threedfy.presentation.printer;

import com.ken3d.threedfy.presentation.user.OrganizationDto;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PrinterClusterDto {

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  private LocationDto location;

  @NotNull
  private OrganizationDto organization;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrganizationDto getOrganization() {
    return organization;
  }

  public void setOrganization(OrganizationDto organization) {
    this.organization = organization;
  }

  public LocationDto getLocation() {
    return location;
  }

  public void setLocation(LocationDto location) {
    this.location = location;
  }
}
