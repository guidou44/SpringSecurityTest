package com.ken3d.threedfy.presentation.common;

import static com.google.common.truth.Truth.assertThat;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.Organization;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.Location;
import com.ken3d.threedfy.infrastructure.dal.entities.printers.PrinterCluster;
import com.ken3d.threedfy.presentation.printer.LocationDto;
import com.ken3d.threedfy.presentation.printer.PrinterClusterDto;
import com.ken3d.threedfy.presentation.user.OrganizationDto;
import com.ken3d.threedfy.presentation.user.UserDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class DtoMapperTest {

  @Test
  public void givenModelWithSkipConditions_whenMapToDto_thenItMapsProperlyAndSkipFieldsInCondition() {
    ModelMapper mapperInternal = new ModelMapper();
    DtoMapper mapper = new DtoMapper(mapperInternal);
    User user = givenUserModel();

    UserDto userDto = mapper.map(user, UserDto.class);

    assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
    assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
    assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
    assertThat(userDto.getPassword()).isNull();
    assertThat(userDto.getMatchingPassword()).isNull();
  }

  @Test
  public void givenModelWith2LevelWithPropertyMap_whenMapToDto_thenItMapsProperlySubLevelModel() {
    ModelMapper mapperInternal = new ModelMapper();
    DtoMapper mapper = new DtoMapper(mapperInternal);
    PrinterCluster printerCluster = givenPrinterClusterModel();

    PrinterClusterDto clusterDto = mapper.map(printerCluster, PrinterClusterDto.class);

    assertThat(clusterDto.getLocation()).isInstanceOf(LocationDto.class);
    assertThat(clusterDto.getOrganization()).isInstanceOf(OrganizationDto.class);
  }

  @Test
  public void givenModelWith2LevelWithoutPropertyMap_whenMapToDto_thenItMapsProperlySubLevelModel() {
    ModelMapper mapperInternal = new ModelMapper();
    DtoMapper mapper = new DtoMapper(mapperInternal);
    Organization organization = givenOrganizationModel();

    OrganizationDto organizationDto = mapper.map(organization, OrganizationDto.class);

    assertThat(organizationDto.getOwner()).isInstanceOf(UserDto.class);
  }

  private User givenUserModel() {
    User user = new User();
    user.setEmail("test@test.com");
    user.setEnabled(true);
    user.setId(1);
    user.setPasswordHash("PASSWORD_HASH_123");
    user.setFirstName("TEST");
    user.setLastName("TEST");
    return user;
  }

  private Organization givenOrganizationModel() {
    Organization organization = new Organization();
    organization.setId(1);
    organization.setOwner(givenUserModel());
    organization.setCollaborative(false);
    organization.setName("TEST");
    return organization;
  }

  private Location givenLocationModel() {
    Location local = new Location();
    local.setId(1);
    local.setStreet("test street");
    local.setCountry("Canada");
    local.setAddress("1111");
    local.setPostalCode("G1V4P4");
    return local;
  }

  private PrinterCluster givenPrinterClusterModel() {
    PrinterCluster cluster = new PrinterCluster();
    cluster.setId(2);
    cluster.setLocation(givenLocationModel());
    cluster.setName("TEST_CLUSTER");
    cluster.setOrganization(givenOrganizationModel());
    return cluster;
  }

}
