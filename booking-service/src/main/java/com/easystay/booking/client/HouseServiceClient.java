package com.easystay.booking.client;

import com.easystay.booking.dto.CasaVacanzaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HouseServiceClient {

    private final RestTemplate restTemplate;

    @Value("${house.service.url}")
    private String houseServiceUrl;

    public CasaVacanzaDTO getCasaVacanzaById(Long casaId, String token) {
        String url = houseServiceUrl + "/api/case-vacanza/" + casaId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<CasaVacanzaDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    CasaVacanzaDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Casa vacanza non trovata");
        }
    }
}
