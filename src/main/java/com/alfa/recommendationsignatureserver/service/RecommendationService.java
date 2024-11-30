package com.alfa.recommendationsignatureserver.service;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.UseContextValues;

import java.util.List;

public interface RecommendationService {
    RecommendationResponse getRecommendation(ClientData clientData);

    RecommendationResponse getRecommendationWithContext(ClientData clientData, List<UseContextValues> useContextValues);
}
