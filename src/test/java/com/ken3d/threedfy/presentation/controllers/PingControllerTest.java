package com.ken3d.threedfy.presentation.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ken3d.threedfy.HibernateConfiguration;
import com.ken3d.threedfy.TestWebSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
    controllers = PingController.class,
    excludeAutoConfiguration = HibernateConfiguration.class)
@Import(TestWebSecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("nosecurity")
public class PingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithUserDetails(value="user", userDetailsServiceBeanName="userDetailsService")
  public void givenPingController_whenGetHelloWorld_thenItReturnsProperView() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/ping"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  @WithUserDetails(value="user", userDetailsServiceBeanName="userDetailsService")
  public void givenUserLevelAuth_whenGetManagerEndPoint_thenItReturnsForbidden() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/ping/managerAuth"))
        .andExpect(status().isForbidden());
  }
}
