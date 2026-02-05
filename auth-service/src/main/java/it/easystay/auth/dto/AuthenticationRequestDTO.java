package it.easystay.auth.dto;

public record AuthenticationRequestDTO(
    String email,
    String password
) {
}
