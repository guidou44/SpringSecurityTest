package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Organization_Group")
public class OrganizationGroup extends AccountEntityBase {

  @Column(name = "Name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "Organization_FK", referencedColumnName = "Id")
  private Organization organization;

  @ManyToMany(mappedBy = "organizationGroups")
  private Set<User> users = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
      name = "OrganizationGroup_Module",
      joinColumns = { @JoinColumn(name = "Organization_Group_FK", referencedColumnName = "Id") },
      inverseJoinColumns = { @JoinColumn(name = "Module_FK", referencedColumnName = "Id") }
  )
  private Set<Module> modules = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(
      Organization organization) {
    this.organization = organization;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public Set<Module> getModules() {
    return modules;
  }

  public void setModules(
      Set<Module> modules) {
    this.modules = modules;
  }
}
