package itss.batch.ingestion.service;

import itss.batch.ingestion.exceptions.ContactsBulkException;
import itss.batch.ingestion.model.request.ContactsRequest;
import itss.batch.ingestion.model.response.ContactsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ContactsService {

    @Value("${write.api.contact-manager}")
    String api;
/*
    @Autowired
    ValidationUtils validationUtils;*/

    public void createContactsBulk(ContactsRequest requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            //validationUtils.validateError(requestBody);

            ResponseEntity<ContactsResponse> response = restTemplate.postForEntity(api,
                    requestBody,
                    ContactsResponse.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ContactsBulkException(response.getStatusCode().getReasonPhrase(), requestBody);
            }

            log.debug("Response status: {} and body: {}", response.getStatusCode(), response.getBody());
        } catch (RestClientException e){
            throw new ContactsBulkException(e.getMessage(), requestBody);
        }
    }
}
