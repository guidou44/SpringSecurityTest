package com.ken3d.threedfy;

import javax.sql.DataSource;
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

  private final DataSource dataSource;

  @Autowired
  public SecurityConfiguration(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authManager) throws Exception {
    authManager.jdbcAuthentication().dataSource(dataSource)
        .passwordEncoder(new BCryptPasswordEncoder())
        .usersByUsernameQuery(
            "SELECT Username,Password_Hash, Enabled FROM User WHERE username=?")
        .authoritiesByUsernameQuery(
            "SELECT u.Username, ur.Role FROM User_Role as "
                + "ur JOIN User as u on u.Id = ur.User_FK WHERE u.Username=?");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin*").hasRole("ADMIN")
        .antMatchers("/user*").hasRole("USER")
        .antMatchers("/").permitAll()
        .and()
        .httpBasic();
    http.csrf().disable()
        .formLogin().loginPage("/login");
  }
}
