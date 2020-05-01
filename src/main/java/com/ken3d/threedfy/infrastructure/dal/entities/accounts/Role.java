package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import com.ken3d.threedfy.domain.dao.AccountEntityBase;
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
import javax.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table
public class Role extends AccountEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

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

  @Column(name = "Authority_Level", nullable = false)
  @ColumnDefault("-1")
  private int authorityLevel;

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAuthorityLevel() {
    return authorityLevel;
  }

  public void setAuthorityLevel(int authorityLevel) {
    this.authorityLevel = authorityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Role role = (Role) o;
    return id == role.id &&
        authorityLevel == role.authorityLevel &&
        Objects.equals(name, role.name) &&
        Objects.equals(description, role.description) &&
        Objects.equals(users, role.users) &&
        Objects.equals(modules, role.modules);
  }
}
