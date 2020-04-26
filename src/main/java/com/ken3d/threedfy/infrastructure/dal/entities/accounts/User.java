package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class User extends AccountEntityBase {

  @Column(name = "Username", nullable = false)
  private String username;

  @Column(name = "First_Name")
  private String firstName;

  @Column(name = "Last_Name")
  private String lastName;

  @Column(name = "Email")
  private String email;

  @Column(name = "Password_Hash", nullable = false)
  private String passwordHash;

  @Column(name = "Enabled")
  private boolean enabled;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "User_Role",
      joinColumns = {@JoinColumn(name = "User_FK", referencedColumnName = "Id")},
      inverseJoinColumns = {@JoinColumn(name = "Role_FK", referencedColumnName = "Id")}
  )
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "OrganizationGroup_User",
      joinColumns = {@JoinColumn(name = "User_FK", referencedColumnName = "Id")},
      inverseJoinColumns = {
          @JoinColumn(name = "Organization_Group_FK", referencedColumnName = "Id")}
  )
  private Set<OrganizationGroup> organizationGroups = new HashSet<>();

  @OneToMany(mappedBy = "owner")
  private Set<Organization> organizations = new HashSet<>();


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
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

  public Set<Organization> getOrganizations() {
    return organizations;
  }

  public void setOrganizations(
      Set<Organization> organizations) {
    this.organizations = organizations;
  }
}
