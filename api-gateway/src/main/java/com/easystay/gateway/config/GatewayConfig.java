package com.easystay.gateway.config;

import com.easystay.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service - no JWT required
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("${AUTH_SERVICE_URL:http://localhost:8081}"))
                
                // House Service - JWT required
                .route("house-service", r -> r
                        .path("/api/case-vacanza/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("${HOUSE_SERVICE_URL:http://localhost:8082}"))
                
                // Booking Service - JWT required
                .route("booking-service", r -> r
                        .path("/api/prenotazioni/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("${BOOKING_SERVICE_URL:http://localhost:8083}"))
                
                .build();
    }
}
