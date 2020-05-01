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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainController.class, excludeAutoConfiguration = HibernateConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("nosecurity")
public class MainControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenMainController_whenGetIndex_thenItReturnsProperView() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  public void givenMainController_whenGetDashboard_thenItReturnsProperView() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/dashboard"))
        .andExpect(status().isOk())
        .andExpect(view().name("dashboard"));
  }
}
