package com.easystay.auth.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo = Ruolo.USER;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Utente() {
    }

    public Utente(Long id, String email, String password, String nome, Ruolo ruolo, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public enum Ruolo {
        USER, ADMIN
    }
}
