package com.alfa.recommendationsignatureserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Client data used for recommendation")
public class ClientData {
    private String clientId;

    private String organizationId;

    private String segment;

    private String role;

    private int organizations;

    private String currentMethod;

    private boolean mobileApp;

    private Signatures signatures;

    private List<String> availableMethods;

    private int claims;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Signatures details")
    public static class Signatures {
        private Usage common;

        private Usage special;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Schema(description = "Usage details")
        public static class Usage {
            private int mobile;

            private int web;
        }
    }
}
