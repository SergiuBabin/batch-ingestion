package itss.batch.ingestion.service;

import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;

import itss.batch.ingestion.model.response.LegalEntityResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
public class LegalEntityServiceTemplate {

    @Value("${write.api.legal-entity}")
    String api;

    public void restCallToCreateStudent(LegalEntityRequestTemplate requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<LegalEntityResponseTemplate[]> response = restTemplate.postForEntity(api,
                requestBody,
                LegalEntityResponseTemplate[].class);

        if (response.getStatusCode() != HttpStatus.OK) {
             throw new RestClientException(response.getStatusCode().getReasonPhrase());
        }

        log.debug("Response status: {} and body: {}", response.getStatusCode(), response.getBody());

    }
}
