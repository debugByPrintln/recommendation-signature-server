package com.alfa.recommendationsignatureserver.service;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;

public interface RecommendationService {
    RecommendationResponse getRecommendation(ClientData clientData);
}
