package com.alfa.recommendationsignatureserver.controller;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import com.alfa.recommendationsignatureserver.service.impl.RecommendationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recommendations")
@Tag(name = "Recommendation receiver", description = "Operations related to getting signature recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationServiceImpl){
        this.recommendationService = recommendationServiceImpl;
    }

    @PostMapping(value = "/receive", consumes = "application/json")
    @Operation(summary = "Get recommendation based on client data", description = "Sends client data to the model service and retrieves a signature recommendation")
    public RecommendationResponse getRecommendation(@RequestBody ClientData clientData) {
        return recommendationService.getRecommendation(clientData);
    }
}
