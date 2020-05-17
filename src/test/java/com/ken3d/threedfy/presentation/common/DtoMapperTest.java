package com.ken3d.threedfy.presentation.common;

import static com.google.common.truth.Truth.assertThat;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.UserDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class DtoMapperTest {

  @Test
  public void givenModelWithSkipConditions_whenMapToDto_thenItMapsProperlyAndSkipFieldsInConditio() {
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

}
