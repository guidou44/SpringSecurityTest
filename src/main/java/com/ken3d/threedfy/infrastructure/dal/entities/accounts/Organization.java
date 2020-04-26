package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Organization extends AccountEntityBase {

  @Column(name = "Name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "UserOwner_FK", referencedColumnName = "Id", nullable = false)
  private User owner;

  @Column(name = "Is_Collaborative")
  private boolean isCollaborative;

  @OneToMany(mappedBy = "organization")
  private Set<OrganizationGroup> organizationGroups = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public boolean isCollaborative() {
    return isCollaborative;
  }

  public void setCollaborative(boolean collaborative) {
    isCollaborative = collaborative;
  }

  public Set<OrganizationGroup> getOrganizationGroups() {
    return organizationGroups;
  }

  public void setOrganizationGroups(Set<OrganizationGroup> organizationGroups) {
    this.organizationGroups = organizationGroups;
  }
}
