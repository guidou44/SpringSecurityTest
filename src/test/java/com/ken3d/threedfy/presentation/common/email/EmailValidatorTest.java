package com.ken3d.threedfy.presentation.common.email;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {

  private static final ConstraintValidatorContext CONTEXT = mock(ConstraintValidatorContext.class);
  private static final String VALID_EMAIL = "test@hotmail.com";

  @Test
  public void givenValidEmail_itValidatesEmail() {
    EmailValidator validator = new EmailValidator();

    boolean isValid = validator.isValid(VALID_EMAIL, CONTEXT);

    assertThat(isValid).isTrue();
  }

  @Test
  public void givenInvalidEmail_itInvalidatesEmail() {
    final String invalidEmail1 = "1234@";
    final String invalidEmail2 = "1234@3456.";
    final String invalidEmail3 = "1234";
    final String invalidEmail4 = "1234@----";
    EmailValidator validator = new EmailValidator();

    boolean isValid1 = validator.isValid(invalidEmail1, CONTEXT);
    boolean isValid2 = validator.isValid(invalidEmail2, CONTEXT);
    boolean isValid3 = validator.isValid(invalidEmail3, CONTEXT);
    boolean isValid4 = validator.isValid(invalidEmail4, CONTEXT);

    assertThat(isValid1).isFalse();
    assertThat(isValid2).isFalse();
    assertThat(isValid3).isFalse();
    assertThat(isValid4).isFalse();
  }
}
