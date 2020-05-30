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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getApartment() {
    return apartment;
  }

  public void setApartment(String apartment) {
    this.apartment = apartment;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
