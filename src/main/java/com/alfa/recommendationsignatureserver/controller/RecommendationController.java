package com.alfa.recommendationsignatureserver.controller;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.UseContextValues;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/recommendations")
@Tag(name = "Recommendation receiver", description = "Operations related to getting signature recommendations")
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationServiceImpl){
        this.recommendationService = recommendationServiceImpl;
    }

    @PostMapping(value = "/receive-with-context", consumes = "application/json")
    @Operation(summary = "Get recommendation based on client data and use context", description = "Sends client data and use context to the model service and retrieves a signature recommendation")
    public RecommendationResponse getRecommendationWithContext(@RequestBody ClientData clientData, @RequestParam("useContext") UseContextValues useContextValues) {
        log.info("Resolving POST request with clientData: {} and useContexts: {}", clientData, useContextValues);
        return recommendationService.getRecommendationWithContext(clientData, useContextValues);
    }

    @PostMapping(value = "/receive", consumes = "application/json")
    @Operation(summary = "Get recommendation based on client data", description = "Sends client data to the model service and retrieves a signature recommendation")
    public RecommendationResponse getRecommendation(@RequestBody ClientData clientData) {
        log.info("Resolving POST request with clientData: {}", clientData);
        return recommendationService.getRecommendation(clientData);
    }
}
