package com.ken3d.threedfy.presentation.common.password;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.presentation.common.password.PasswordValidator;
import com.ken3d.threedfy.presentation.user.UserDto;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

  private static final UserDto USER = mock(UserDto.class);
  private static final ConstraintValidatorContext CONTEXT = mock(ConstraintValidatorContext.class);
  private static final String PASSWORD = "12345";
  private static final String CONFIRM_PASSWORD = "54321";

  @Test
  public void givenMatchingPassword_itValidatesPassword() {
    willReturn(PASSWORD).given(USER).getPassword();
    willReturn(PASSWORD).given(USER).getMatchingPassword();
    PasswordValidator validator = new PasswordValidator();

    boolean isValid = validator.isValid(USER, CONTEXT);

    assertThat(isValid).isTrue();
  }

  @Test
  public void givenNonMatchingPassword_itInvalidatePassword() {
    willReturn(PASSWORD).given(USER).getPassword();
    willReturn(CONFIRM_PASSWORD).given(USER).getMatchingPassword();
    PasswordValidator validator = new PasswordValidator();

    boolean isValid = validator.isValid(USER, CONTEXT);

    assertThat(isValid).isFalse();
  }
}
