package com.ken3d.threedfy;

import com.ken3d.threedfy.domain.user.security.UserAuthenticationService;
import com.ken3d.threedfy.domain.user.security.UserAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("!nosecurity")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private UserAuthenticationService userAuthService;
  private UserAuthenticationSuccessHandler userAuthSuccessHandler;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public WebSecurityConfiguration(UserAuthenticationService userAuthService,
      UserAuthenticationSuccessHandler userAuthSuccessHandler,
      PasswordEncoder passwordEncoder) {
    this.userAuthService = userAuthService;
    this.userAuthSuccessHandler = userAuthSuccessHandler;
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
    authManagerBuilder.userDetailsService(userAuthService)
        .passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/*").permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .successHandler(userAuthSuccessHandler)
        .and()
        .logout().permitAll().logoutSuccessUrl("/login")
        .and()
        .csrf().disable();
  }
}
