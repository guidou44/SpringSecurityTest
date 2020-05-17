package com.ken3d.threedfy.presentation.common;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

  private ModelMapper modelMapper;

  @Autowired
  public DtoMapper(ModelMapper mapper) {
    modelMapper = mapper;
    addMappingConditions();
  }

  public <T> T map(Object fromObject, Class<T> toClass) {
    return modelMapper.map(fromObject, toClass);
  }

  private void addMappingConditions() {
    addUserMappingConditions();
  }

  private void addUserMappingConditions() {
    modelMapper.addMappings(new PropertyMap<User, UserDto>() {
      @Override
      protected void configure() {
        skip(destination.getPassword());
        skip(destination.getMatchingPassword());
      }
    });
  }
}
