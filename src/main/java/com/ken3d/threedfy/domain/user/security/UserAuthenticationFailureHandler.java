package com.ken3d.threedfy.domain.user.security;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final MessageSource messages;
  private final LocaleResolver localeResolver;

  @Autowired
  public UserAuthenticationFailureHandler(@Qualifier("messageSource") MessageSource messages,
      LocaleResolver localeResolver) {
    this.messages = messages;
    this.localeResolver = localeResolver;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    setDefaultFailureUrl("/login?error=true");

    super.onAuthenticationFailure(request, response, exception);

    Locale locale = localeResolver.resolveLocale(request);

    String errorMessage = messages.getMessage("message.badCredentials", null, locale);

    if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
      errorMessage = messages.getMessage("auth.message.disabled", null, locale);
    } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
      errorMessage = messages.getMessage("auth.message.expired", null, locale);
    }

    request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
  }
}
