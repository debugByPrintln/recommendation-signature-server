package com.alfa.recommendationsignatureserver.service.impl;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.SignatureMethods;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    @Value(value = "${model.url-to-predict}")
    private String urlToPredict;

    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public RecommendationResponse getRecommendation(ClientData clientData){
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        try{
            recommendationResponse = restTemplate.postForObject(urlToPredict, clientData, RecommendationResponse.class);
            log.info("Got recommendation for client {} with signature recommendation: {}", clientData.getClientId(), recommendationResponse.getRecommendedMethod());
        }
        catch (RestClientException e){
            log.warn("Unable to receive signature recommendation. Setting recommendation signature method to {}", SignatureMethods.SMS);
            recommendationResponse.setRecommendedMethod(SignatureMethods.SMS);
        }
        return recommendationResponse;
    }
}
