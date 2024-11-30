package com.alfa.recommendationsignatureserver.service.impl;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.SignatureMethods;
import com.alfa.recommendationsignatureserver.dto.UseContextValues;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    @Value(value = "${model.url-to-predict}")
    private String urlToPredict;

    @Value(value = "${model.url-to-predict-with-context}")
    private String urlToPredictWithContext;

    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public RecommendationResponse getRecommendation(ClientData clientData){
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        try {
            recommendationResponse = restTemplate.postForObject(urlToPredict, clientData, RecommendationResponse.class);

            if (recommendationResponse == null) {
                log.warn("Received null response from ML service. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
                recommendationResponse = new RecommendationResponse();
                recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
            }
            else {
                log.info("Got recommendation for client {} with signature recommendation: {}", clientData.getClientId(), recommendationResponse.getRecommendedMethod());
            }
        }

        catch (RestClientException e){
            log.warn("Unable to receive signature recommendation. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
            recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
        }

        return recommendationResponse;
    }

    @Override
    public RecommendationResponse getRecommendationWithContext(ClientData clientData, List<UseContextValues> useContextValues) {
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlToPredictWithContext)
                    .queryParam("useContext", useContextValues.stream().map(Enum::name).toArray(String[]::new));
            URI uri = builder.build().toUri();

            log.info(uri.toString());
            recommendationResponse = restTemplate.postForObject(uri, clientData, RecommendationResponse.class);

            if (recommendationResponse == null) {
                log.warn("Received null response from ML service. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
                recommendationResponse = new RecommendationResponse();
                recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
            }
            else {
                log.info("Got recommendation for client {} with signature recommendation: {}", clientData, recommendationResponse.getRecommendedMethod());
            }
        }
        catch (RestClientException e) {
            log.warn("Unable to receive signature recommendation. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
            recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
        }
        return recommendationResponse;
    }
}
