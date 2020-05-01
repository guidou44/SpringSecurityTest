package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Module extends AccountEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Module module = (Module) o;
    return id == module.id
        && Objects.equals(name, module.name)
        && Objects.equals(roles, module.roles)
        && Objects.equals(organizationGroups, module.organizationGroups);
  }
}
