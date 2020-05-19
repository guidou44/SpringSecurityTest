package com.ken3d.threedfy.infrastructure.dal.entities.printers;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Printer_Cluster")
public class PrinterCluster extends PrinterEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "Location_FK", referencedColumnName = "Id", nullable = false)
  private Location location;

  @ManyToOne
  @JoinColumn(name = "Organization_FK", referencedColumnName = "Id", nullable = false)
  private Organization organization;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(
      Location location) {
    this.location = location;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(
      Organization organization) {
    this.organization = organization;
  }
}
