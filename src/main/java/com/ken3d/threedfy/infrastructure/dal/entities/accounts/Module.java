package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Module extends AccountEntityBase {

  @Column(name = "Name", nullable = false)
  private String name;

  @ManyToMany(mappedBy = "modules")
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(mappedBy = "modules")
  private Set<OrganizationGroup> organizationGroups = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<OrganizationGroup> getOrganizationGroups() {
    return organizationGroups;
  }

  public void setOrganizationGroups(
      Set<OrganizationGroup> organizationGroups) {
    this.organizationGroups = organizationGroups;
  }
}
