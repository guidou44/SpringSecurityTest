package com.ken3d.threedfy;

import com.ken3d.threedfy.domain.User.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Profile("!nosecurity")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private UserAuthenticationService userAuthService;

  @Autowired
  public SecurityConfiguration(UserAuthenticationService userAuthService) {
    this.userAuthService = userAuthService;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
    authManagerBuilder.userDetailsService(userAuthService)
        .passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.formLogin().loginPage("/login")
        .and()
        .authorizeRequests()
        .antMatchers("/admin*").hasRole("ADMIN")
        .antMatchers("/user*").hasRole("USER")
        .antMatchers("/*").permitAll()
        .and()
        .logout().permitAll().logoutSuccessUrl("/login")
        .and()
        .csrf().disable();
  }
}
