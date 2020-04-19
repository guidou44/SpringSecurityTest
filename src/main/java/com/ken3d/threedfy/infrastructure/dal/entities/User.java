package com.ken3d.threedfy.infrastructure.dal.entities;

import com.ken3d.threedfy.infrastructure.dal.entities.baseentity.MonitoringEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class User extends MonitoringEntityBase {

  @Column(name = "User_Name")
  private String username;

  @Column(name = "First_Name")
  private String firstName;

  @Column(name = "Last_Name")
  private String lastName;

  @Column(name = "Email")
  private String email;

  @Column(name = "Password_Hash")
  private String passwordHash;


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
}
