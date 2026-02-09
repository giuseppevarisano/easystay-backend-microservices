package com.easystay.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

    public AuthResponse() {
    }

    public AuthResponse(String token, Long userId, String email, String nome, String ruolo) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nome = nome;
        this.ruolo = ruolo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
