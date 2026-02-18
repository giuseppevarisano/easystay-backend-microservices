package com.easystay.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dettagli utente")
public class UtenteDettaglioResponse {

    @Schema(description = "ID utente", example = "1")
    private Long id;

    @Schema(description = "Email utente", example = "user@example.com")
    private String email;

    @Schema(description = "Nome utente", example = "Mario Rossi")
    private String nome;

    @Schema(description = "Ruolo utente", example = "USER")
    private String ruolo;

    @Schema(description = "Data creazione utente")
    private LocalDateTime createdAt;

    public UtenteDettaglioResponse() {
    }

    public UtenteDettaglioResponse(Long id, String email, String nome, String ruolo, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.ruolo = ruolo;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
