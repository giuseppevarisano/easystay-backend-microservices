package it.easystay.booking.client;

import it.easystay.booking.dto.CasavacanzaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service")
public interface PropertyClient {
    
    @GetMapping("/api/casevacanza/{id}")
    CasavacanzaDTO getPropertyById(@PathVariable("id") Long id);
}
