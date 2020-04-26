package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Role extends AccountEntityBase {

  @Column(name = "Name", nullable = false)
  private String name;

  @Column(name = "Description", nullable = true)
  private String description;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
      name = "Role_Module",
      joinColumns = { @JoinColumn(name = "Role_FK", referencedColumnName = "Id") },
      inverseJoinColumns = { @JoinColumn(name = "Module_FK", referencedColumnName = "Id") }
  )
  private Set<Module> modules = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Module> getModules() {
    return modules;
  }

  public void setModules(
      Set<Module> modules) {
    this.modules = modules;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
