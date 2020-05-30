package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Organization_Group")
public class OrganizationGroup extends AccountEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "Organization_FK", referencedColumnName = "Id")
  private Organization organization;

  @ManyToMany(mappedBy = "organizationGroups")
  private Set<User> users = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "OrganizationGroup_Role",
      joinColumns = {@JoinColumn(name = "OrganizationGroup_FK", referencedColumnName = "Id")},
      inverseJoinColumns = {@JoinColumn(name = "Role_FK", referencedColumnName = "Id")}
  )
  private Set<Role> roles = new HashSet<>();

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getHighestAuthorityLevel() {
    Set<Role> roles = this.getRoles();
    return getHighestRole().getAuthorityLevel();
  }

  public Role getHighestRole() {
    Set<Role> roles = this.getRoles();
    return Collections
        .max(roles, Comparator.comparing(Role::getAuthorityLevel));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationGroup that = (OrganizationGroup) o;
    return id == that.id
        && Objects.equals(name, that.name)
        && Objects.equals(organization, that.organization)
        && Objects.equals(users, that.users);
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
