package com.easystay.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Risposta di autenticazione con JWT token")
public class AuthResponse {

    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "ID dell'utente", example = "1")
    private Long userId;

    @Schema(description = "Email dell'utente", example = "user@example.com")
    private String email;

    @Schema(description = "Nome dell'utente", example = "Mario Rossi")
    private String nome;

    @Schema(description = "Ruolo dell'utente", example = "USER")
    private String ruolo;
}
