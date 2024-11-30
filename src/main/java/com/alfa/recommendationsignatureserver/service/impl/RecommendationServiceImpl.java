package com.alfa.recommendationsignatureserver.service.impl;

import com.alfa.recommendationsignatureserver.dto.ClientData;
import com.alfa.recommendationsignatureserver.dto.RecommendationResponse;
import com.alfa.recommendationsignatureserver.dto.SignatureMethods;
import com.alfa.recommendationsignatureserver.dto.UseContextValues;
import com.alfa.recommendationsignatureserver.entity.Mapper;
import com.alfa.recommendationsignatureserver.repository.MapperRepository;
import com.alfa.recommendationsignatureserver.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final MapperRepository mapperRepository;

    @Autowired
    public RecommendationServiceImpl(RestTemplate restTemplate, MapperRepository mapperRepository){
        this.restTemplate = restTemplate;
        this.mapperRepository = mapperRepository;
    }

    @Override
    @Transactional
    public RecommendationResponse getRecommendation(ClientData clientData) {
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        try {
            Mapper mapper = mapperRepository.findByClientId(clientData.getClientId());
            if (mapper == null) {
                mapper = new Mapper();
                mapper.setClientId(clientData.getClientId());
                mapper.setCounter(0);
                mapperRepository.save(mapper);
                // Call ML service for recommendation
                recommendationResponse = restTemplate.postForObject(urlToPredict, clientData, RecommendationResponse.class);
            }
            else {
                mapper.setCounter(mapper.getCounter() + 1);
                mapperRepository.save(mapper);
                if (mapper.getCounter() > 3) {
                    // Reset counter
                    mapper.setCounter(0);
                    mapperRepository.save(mapper);
                    // Call ML service for recommendation
                    recommendationResponse = restTemplate.postForObject(urlToPredict, clientData, RecommendationResponse.class);
                }
                else {
                    // Return default recommendation
                    recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
                }
            }

            if (recommendationResponse == null) {
                log.warn("Received null response from ML service. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
                recommendationResponse = new RecommendationResponse();
                recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
            }
            else {
                log.info("Got recommendation for client {} with signature recommendation: {}", clientData.getClientId(), recommendationResponse.getRecommendedMethod());
            }
        }
        catch (RestClientException e) {
            log.warn("Unable to receive signature recommendation. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
            recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
        }
        return recommendationResponse;
    }

    @Override
    @Transactional
    public RecommendationResponse getRecommendationWithContext(ClientData clientData, List<UseContextValues> useContextValues) {
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        try {
            Mapper mapper = mapperRepository.findByClientId(clientData.getClientId());
            if (mapper == null) {
                mapper = new Mapper();
                mapper.setClientId(clientData.getClientId());
                mapper.setCounter(0);
                mapperRepository.save(mapper);
                // Call ML service with context for recommendation
                recommendationResponse = callMLServiceWithContext(clientData, useContextValues);
            }
            else {
                mapper.setCounter(mapper.getCounter() + 1);
                mapperRepository.save(mapper);
                if (mapper.getCounter() > 3) {
                    // Reset counter
                    mapper.setCounter(0);
                    mapperRepository.save(mapper);
                    // Call ML service with context for recommendation
                    recommendationResponse = callMLServiceWithContext(clientData, useContextValues);
                }
                else {
                    // Return default recommendation
                    recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
                }
            }

            if (recommendationResponse == null) {
                log.warn("Received null response from ML service. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
                recommendationResponse = new RecommendationResponse();
                recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
            } else {
                log.info("Got recommendation for client {} with signature recommendation: {}", clientData.getClientId(), recommendationResponse.getRecommendedMethod());
            }
        } catch (RestClientException e) {
            log.warn("Unable to receive signature recommendation. Setting recommendation signature method to {}", SignatureMethods.NoRecommendedMethod);
            recommendationResponse.setRecommendedMethod(SignatureMethods.NoRecommendedMethod);
        }
        return recommendationResponse;
    }

    private RecommendationResponse callMLServiceWithContext(ClientData clientData, List<UseContextValues> useContextValues) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlToPredictWithContext)
                .queryParam("useContext", useContextValues.stream().map(Enum::name).toArray(String[]::new));
        URI uri = builder.build().toUri();
        log.info("Calling ML service with context: {}", uri.toString());
        return restTemplate.postForObject(uri, clientData, RecommendationResponse.class);
    }
}
