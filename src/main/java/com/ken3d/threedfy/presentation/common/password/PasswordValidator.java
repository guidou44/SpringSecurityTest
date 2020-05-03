package com.ken3d.threedfy.presentation.common.password;

import com.ken3d.threedfy.presentation.user.UserDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){
    UserDto user = (UserDto) obj;
    return user.getPassword().equals(user.getMatchingPassword());
  }
}
