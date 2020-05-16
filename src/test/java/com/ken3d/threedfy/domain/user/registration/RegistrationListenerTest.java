package com.ken3d.threedfy.domain.user.registration;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.OnRegistrationCompleteEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class RegistrationListenerTest {

  private static final User USER = mock(User.class);
  private static final String APP_URL = "http//:Test.com/register";
  private static final String BASE_URL = "http//:Test.com";
  private static final String MAIL_USERNAME = "Test@Test.com";

  private final IUserService service = mock(IUserService.class);
  private final JavaMailSender mailSender = mock(JavaMailSender.class);
  private final Environment env = mock(Environment.class);
  private final OnRegistrationCompleteEvent event = mock(OnRegistrationCompleteEvent.class);

  @BeforeEach
  void setUp() {
    willReturn(USER).given(event).getUser();
    willReturn(APP_URL).given(event).getAppUrl();
    willReturn(BASE_URL).given(env).getProperty("base.url");
    willReturn(MAIL_USERNAME).given(env).getProperty("spring.mail.username");
    willReturn(MAIL_USERNAME).given(USER).getEmail();
  }

  @Test
  public void givenNewRegistration_whenHandleEvent_thenItCreatesNewVerificationToken() {
    RegistrationListener listener = new RegistrationListener(service, mailSender, env);
    List<String> tokens = new ArrayList<>();
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      String token = (String) args[1];
      tokens.add(token);
      return null;
    }).when(service).createVerificationToken(any(User.class), anyString());

    listener.onApplicationEvent(event);

    assertThat(tokens).hasSize(1);
  }

  @Test
  public void givenNewRegistration_whenHandleEvent_thenItDoesNotCreateDuplicateVerificationToken() {
    RegistrationListener listener = new RegistrationListener(service, mailSender, env);
    List<String> tokens = new ArrayList<>();
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      String token = (String) args[1];
      tokens.add(token);
      return null;
    }).when(service).createVerificationToken(any(User.class), anyString());

    listener.onApplicationEvent(event);
    listener.onApplicationEvent(event);

    assertThat(tokens).hasSize(2);
    assertThat(tokens.get(0)).isNotEqualTo(tokens.get(1));
  }

  @Test
  public void givenNewRegistration_whenHandleEvent_thenItSendsConfirmationEmail() {
    RegistrationListener listener = new RegistrationListener(service, mailSender, env);
    List<SimpleMailMessage> emailCallBack = new ArrayList<>();
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      SimpleMailMessage email = (SimpleMailMessage) args[0];
      emailCallBack.add(email);
      return null;
    }).when(mailSender).send(any(SimpleMailMessage.class));

    listener.onApplicationEvent(event);

    assertThat(emailCallBack).hasSize(1);
    assertThat(emailCallBack.get(0).getFrom()).isEqualTo(MAIL_USERNAME);
    assertThat(emailCallBack.get(0).getTo()).hasLength(1);
    assertThat(emailCallBack.get(0).getTo()[0]).isEqualTo(MAIL_USERNAME);
  }

}
