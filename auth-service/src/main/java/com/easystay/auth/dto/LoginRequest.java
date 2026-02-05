package com.easystay.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Richiesta di autenticazione")
public class LoginRequest {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    @Schema(description = "Email dell'utente", example = "user@example.com")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Schema(description = "Password dell'utente", example = "Password123!")
    private String password;
}
