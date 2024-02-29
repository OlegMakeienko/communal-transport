package com.makeienko.communaltransport.model;

import com.makeienko.communaltransport.dto.IndividualServiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IndividualServiceClient {

    private String individualServiceUrl = "https://spring-azure-group3.azurewebsites.net/api/v1";

    private RestTemplate restTemplate = new RestTemplate();


    public IndividualServiceDto getRoute(Long id) {
        ResponseEntity<IndividualServiceDto> response = restTemplate.getForEntity(individualServiceUrl + "/routes" + id, IndividualServiceDto.class);
        return response.getBody();
    }
}

