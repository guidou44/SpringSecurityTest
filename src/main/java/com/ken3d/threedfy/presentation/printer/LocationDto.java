package com.ken3d.threedfy.presentation.printer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LocationDto {

  @NotNull
  @NotEmpty
  private String address;

  @NotNull
  @NotEmpty
  private String street;

  private String apartment;

  @NotNull
  @NotEmpty
  private String postalCode;

  @NotNull
  @NotEmpty
  private String country;

  private String state;

  private String county;

}
