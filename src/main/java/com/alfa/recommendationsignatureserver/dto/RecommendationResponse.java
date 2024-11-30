package com.alfa.recommendationsignatureserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Recommendation response from the service")
public class RecommendationResponse {
    @Schema(description = "Recommended method for the client")
    private SignatureMethods recommendedMethod;
}
