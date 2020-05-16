package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

  private Role role;
  private OrganizationGroup organizationGroup = null;

  public Authority(Role role) {
    this.role = role;
  }

  public Authority(Role role, OrganizationGroup organizationGroup) {
    this(role);
    this.organizationGroup = organizationGroup;
  }

  @Override
  public String getAuthority() {
    return role.getName();
  }

  public int getAuthorityLevel() {

    if (organizationGroup != null) {
      return organizationGroup.getAuthorityLevel();
    }
    return role.getAuthorityLevel();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Authority authority = (Authority) o;
    return Objects.equals(role, authority.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role);
  }
}
