package com.alfa.recommendationsignatureserver.controller;

import com.alfa.recommendationsignatureserver.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@Tag(name = "Configurator", description = "Operations related to configuring recommendation server")
@Slf4j
@CrossOrigin(origins = "*")
public class ConfigController {
    private final RecommendationService recommendationService;

    @Autowired
    public ConfigController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PutMapping("/counter-reset-threshold/{threshold}")
    @Operation(summary = "Change for how long user will not be disturbed (Allowed values - from 2 to 10)", description = "Resets counter trashhold in recommendation service")
    public ResponseEntity<String> updateCounterResetThreshold(@PathVariable int threshold) {
        log.info("Resolving PUT request for updating threshold with new value: {}...", threshold);
        if (threshold < 1 || threshold > 10) {
            log.info("Bad threshold value: {}", threshold);
            return ResponseEntity.badRequest().body("Invalid threshold value: " + threshold + ". Must be from 2 to 10");
        }
        recommendationService.setCounterResetThreshold(threshold);
        log.info("Successfully reset counter threshold with value: {}", threshold);
        return ResponseEntity.ok().build();
    }
}
