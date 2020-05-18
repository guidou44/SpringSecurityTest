package com.ken3d.threedfy.presentation.user;

public class OrganizationDto {

  private int id;
  private String name;
  private UserDto owner;
  private boolean isCollaborative;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserDto getOwner() {
    return owner;
  }

  public void setOwner(UserDto owner) {
    this.owner = owner;
  }

  public boolean isCollaborative() {
    return isCollaborative;
  }

  public void setCollaborative(boolean collaborative) {
    isCollaborative = collaborative;
  }
}
