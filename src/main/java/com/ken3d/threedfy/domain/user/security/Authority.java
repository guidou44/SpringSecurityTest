package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.OrganizationGroup;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Role;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

  private final Role role;
  private final Organization organization;

  private OrganizationGroup organizationGroup = null;

  public Authority(Role role, Organization organization) {
    this.organization = organization;
    this.role = role;
  }

  public Authority(Role role, OrganizationGroup organizationGroup, Organization organization) {
    this(role, organization);
    this.organizationGroup = organizationGroup;
  }

  @Override
  public String getAuthority() {

    if (organizationGroup != null) {
      return organizationGroup.getHighestRole().getName();
    }
    return role.getName();
  }

  public int getAuthorityLevel() {

    if (organizationGroup != null) {

      return organizationGroup.getHighestAuthorityLevel();
    }
    return role.getAuthorityLevel();
  }

  public Optional<OrganizationGroup> getOrganizationGroup() {
    return Optional.ofNullable(this.organizationGroup);
  }

  public Organization getOrganization() {
    return this.organization;
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
    return Objects.equals(role, authority.role)
        && Objects.equals(organizationGroup, authority.organizationGroup)
        && Objects.equals(organization, authority.organization);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role);
  }
}
