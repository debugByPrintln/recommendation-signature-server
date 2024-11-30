package com.alfa.recommendationsignatureserver.controller;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.SignatureMethods;
import com.alfa.recommendationsignatureserver.dto.UseContextValues;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetRecommendation_Success() throws Exception {
        ClientData clientData = new ClientData();
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedMethod(SignatureMethods.PayControl);

        when(recommendationService.getRecommendation(any(ClientData.class))).thenReturn(response);

        mockMvc.perform(post("/api/recommendations/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedMethod").value(SignatureMethods.PayControl.name()));
    }

    @Test
    public void testGetRecommendationWithContext_Success() throws Exception {
        ClientData clientData = new ClientData();
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedMethod(SignatureMethods.PayControl);

        when(recommendationService.getRecommendationWithContext(any(ClientData.class), eq(UseContextValues.base_operation_signature))).thenReturn(response);

        mockMvc.perform(post("/api/recommendations/receive-with-context")
                        .param("useContext", UseContextValues.base_operation_signature.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedMethod").value(SignatureMethods.PayControl.name()));
    }
}
