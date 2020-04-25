package com.ken3d.threedfy.infrastructure.dal.entities.accounts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "User_Role")
public class UserRole extends AccountEntityBase {

  @ManyToOne
  @JoinColumn(name = "User_FK", referencedColumnName = "Id")
  private User user;

  @Column(name = "Role", nullable = false)
  private String authority;

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
