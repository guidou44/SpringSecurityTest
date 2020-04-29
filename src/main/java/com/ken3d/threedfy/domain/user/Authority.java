package com.ken3d.threedfy.domain.user;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

  private Role role;

  public Authority(Role role) {
    this.role = role;
  }

  @Override
  public String getAuthority() {
    return role.getName();
  }

  public int getAuthorityLevel() {
    return role.getAuthorityLevel();
  }
}
