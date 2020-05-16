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

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    setDefaultFailureUrl("/login?error=true");

    super.onAuthenticationFailure(request, response, exception);

    String errorMessage = "Bad username/email or password. Cannot find matching account.";

    if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
      errorMessage = "This User is this disabled. Please create another account.";
    } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
      errorMessage = "It seems this account has expired! It is no longer valid.";
    }

    request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
  }
}
