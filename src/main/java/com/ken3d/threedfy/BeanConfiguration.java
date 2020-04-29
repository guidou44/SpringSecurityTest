package com.ken3d.threedfy;

import com.ken3d.threedfy.domain.user.security.UserAuthenticationSuccessHandler;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public UserAuthenticationSuccessHandler authSuccessHandler() {
    return new UserAuthenticationSuccessHandler();
  }
}
