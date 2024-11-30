package com.alfa.recommendationsignatureserver.controller;

import com.alfa.recommendationsignatureserver.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ConfigControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private ConfigController configController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(configController).build();
    }

    @Test
    public void testUpdateCounterResetThreshold_Success() throws Exception {
        int threshold = 5;

        mockMvc.perform(put("/api/config/counter-reset-threshold/{threshold}", threshold))
                .andExpect(status().isOk());

        verify(recommendationService, times(1)).setCounterResetThreshold(threshold);
    }

    @Test
    public void testUpdateCounterResetThreshold_InvalidThreshold() throws Exception {
        int threshold = 11;

        mockMvc.perform(put("/api/config/counter-reset-threshold/{threshold}", threshold))
                .andExpect(status().isBadRequest());

        verify(recommendationService, times(0)).setCounterResetThreshold(threshold);
    }
}
