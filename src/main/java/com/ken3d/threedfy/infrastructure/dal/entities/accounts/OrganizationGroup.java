package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

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

  @Column(name = "Authority_Level", nullable = false)
  @ColumnDefault("-1")
  private int authorityLevel;

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

  public int getAuthorityLevel() {
    return authorityLevel;
  }

  public void setAuthorityLevel(int authorityLevel) {
    this.authorityLevel = authorityLevel;
  }
}
