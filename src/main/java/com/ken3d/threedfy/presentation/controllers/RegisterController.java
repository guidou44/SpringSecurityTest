package com.ken3d.threedfy.presentation.controllers;

import com.ken3d.threedfy.infrastructure.dal.entities.accounts.User;
import com.ken3d.threedfy.infrastructure.dal.entities.accounts.VerificationToken;
import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.OnRegistrationCompleteEvent;
import com.ken3d.threedfy.presentation.user.UserDto;
import com.ken3d.threedfy.presentation.user.exceptions.UserAlreadyExistException;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

  private final IUserService userService;
  private final MessageSource messages;
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  public RegisterController(IUserService userService,
      @Qualifier("messageSource") MessageSource messages,
      ApplicationEventPublisher eventPublisher) {
    this.userService = userService;
    this.messages = messages;
    this.eventPublisher = eventPublisher;
  }

  @GetMapping("/register")
  public String showRegistrationForm(WebRequest request, Model model) {
    UserDto userDto = new UserDto();
    model.addAttribute("user", userDto);
    return "register";
  }

  @PostMapping("/registration")
  public ModelAndView registerUserAccount(
      @ModelAttribute("user") @Valid UserDto userDto,
      HttpServletRequest request, Errors errors) {

    try {
      User user = userService.registerNewUserAccount(userDto);

      String appUrl = request.getContextPath();
      eventPublisher
          .publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
    } catch (UserAlreadyExistException uaeEx) {
      ModelAndView mav = new ModelAndView("register", "user", userDto);
      mav.addObject("message", "An account for that username/email already exists.");
      return mav;
    } catch (RuntimeException ex) {
      return new ModelAndView("emailError", "user", userDto);
    }

    return new ModelAndView("successRegister", "user", userDto);
  }

  @GetMapping("/registrationConfirm")
  public String confirmRegistration(WebRequest request, Model model,
      @RequestParam("token") String token) {

    Locale locale = request.getLocale();
    VerificationToken verificationToken = userService.getVerificationToken(token);

    if (verificationToken == null) {
      String message = messages.getMessage("auth.message.invalidToken", null, locale);
      model.addAttribute("message", message);
      return "redirect:/badUser";
    }

    User user = verificationToken.getUser();

    if (isTokenExpired(verificationToken)) {
      String messageValue = messages.getMessage("auth.message.expired", null, locale);
      model.addAttribute("message", messageValue);
      return "redirect:/badUser";
    }

    user.setEnabled(true);
    userService.saveRegisteredUser(user);

    return "redirect:/login";
  }

  private boolean isTokenExpired(VerificationToken verificationToken) {
    Calendar cal = Calendar.getInstance();
    return (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
  }

}
