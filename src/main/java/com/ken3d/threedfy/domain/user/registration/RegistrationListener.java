package com.ken3d.threedfy.domain.user.registration;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.OnRegistrationCompleteEvent;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private final IUserService service;
  private final MessageSource messages;
  private final JavaMailSender mailSender;
  private final Environment env;

  @Autowired
  public RegistrationListener(IUserService service,
      @Qualifier("messageSource") MessageSource messages,
      JavaMailSender mailSender, Environment env) {
    this.service = service;
    this.messages = messages;
    this.mailSender = mailSender;
    this.env = env;
  }


  @Override
  public void onApplicationEvent(@NonNull OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
    confirmRegistration(onRegistrationCompleteEvent);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    User user = event.getUser();
    String token = UUID.randomUUID().toString();
    service.createVerificationToken(user, token);

    String recipientAddress = user.getEmail();
    String subject = "3Dify Registration Confirmation";
    String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
    String message = messages.getMessage("message.regSucc", null, event.getLocale());

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message + "\r\n" + env.getProperty("base.url") + confirmationUrl);
    mailSender.send(email);
  }
}
