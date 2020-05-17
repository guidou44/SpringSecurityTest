package com.ken3d.threedfy.presentation.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ken3d.threedfy.HibernateConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
    controllers = PingController.class,
    excludeAutoConfiguration = HibernateConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("nosecurity")
public class PingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenPingController_whenGetPing_thenItReturnsProperView() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/ping"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  public void givenPingController_whenInternalServerError_thenItReturnsProperView()
      throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/internalservererror"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"));
  }

  @Test
  public void givenPingController_whenBadRequest_thenItReturnsProperView() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/badrequesterror"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(view().name("error"));
  }
}
