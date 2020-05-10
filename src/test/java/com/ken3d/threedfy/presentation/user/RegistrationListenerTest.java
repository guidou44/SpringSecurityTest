package com.ken3d.threedfy.presentation.user;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ken3d.threedfy.domain.user.registration.RegistrationListener;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import java.util.Locale;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class RegistrationListenerTest {

  private static final Locale LOCALE = Locale.CANADA;
  private static final String REDIRECT_URL = "/confirm/email/test";
  private static final String BASE_APP_URL = "http://test.com";
  private static final String CONFIRM_EMAIL_MESSAGE = "Confirm test email please";

  private final IUserService service = mock(IUserService.class);
  private final MessageSource messages = mock(MessageSource.class);
  private final JavaMailSender mailSender = mock(JavaMailSender.class);
  private final Environment env = mock(Environment.class);
  private final ArgumentCaptor<SimpleMailMessage> emailArgumentCaptor = ArgumentCaptor
      .forClass(SimpleMailMessage.class);

  private OnRegistrationCompleteEvent registrationCompleteEvent;
  private String USER_EMAIL = "test@test.com";

  @BeforeEach
  void setUp() {
    willReturn(CONFIRM_EMAIL_MESSAGE).given(messages).getMessage("message.regSucc", null, LOCALE);
    willReturn(BASE_APP_URL).given(env).getProperty("base.url");
  }

  @Test
  public void givenNewRegistration_whenCompleteRegistrationEventRaised_thenItSendsConfirmationEmail() {
    User user = new User();
    user.setEmail(USER_EMAIL);
    registrationCompleteEvent = new OnRegistrationCompleteEvent(user, LOCALE, REDIRECT_URL);
    RegistrationListener registrationListener =
        new RegistrationListener(service, messages, mailSender, env);

    registrationListener.onApplicationEvent(registrationCompleteEvent);
    verify(mailSender, times(1)).send(emailArgumentCaptor.capture());
    SimpleMailMessage emailSent = emailArgumentCaptor.getValue();

    assertThat(emailSent.getSubject()).isEqualTo("3Dify Registration Confirmation");
  }
}
