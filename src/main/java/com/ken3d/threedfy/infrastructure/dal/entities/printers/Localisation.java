package com.ken3d.threedfy.infrastructure.dal.entities.printers;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Localisation")
public class Localisation extends PrinterEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Address", nullable = false)
  private String address;

  @Column(name = "Street", nullable = false)
  private String street;

  @Column(name = "Apartment", nullable = true)
  private String apartment;

  @Column(name = "Postal_Code", nullable = false)
  private String postalCode;

  @Column(name = "Country", nullable = false)
  private String country;

  @Column(name = "State", nullable = true)
  private String state;

  @Column(name = "County", nullable = true)
  private String county;

  public String getAddress() {
    return address;
  }

  @OneToMany(mappedBy = "localisation")
  private Set<PrinterCluster> printerClusters = new HashSet<>();

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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Set<PrinterCluster> getPrinterClusters() {
    return printerClusters;
  }

  public void setPrinterClusters(
      Set<PrinterCluster> printerClusters) {
    this.printerClusters = printerClusters;
  }
}
