package com.easystay.booking.client;

import com.easystay.booking.dto.UtenteDettaglioDTO;
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
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public UtenteDettaglioDTO getUtenteById(Long utenteId, String token) {
        String url = authServiceUrl + "/api/auth/utenti/" + utenteId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UtenteDettaglioDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UtenteDettaglioDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Utente non trovato: " + utenteId);
        }
    }
}
