package com.easystay.auth.dto;

import com.easystay.auth.model.Utente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Richiesta di registrazione nuovo utente")
public class RegisterRequest {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    @Schema(description = "Email dell'utente", example = "user@example.com")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Schema(description = "Password dell'utente", example = "Password123!")
    private String password;

    @NotBlank(message = "Il nome è obbligatorio")
    @Schema(description = "Nome dell'utente", example = "Mario Rossi")
    private String nome;

    @NotNull(message = "Il ruolo è obbligatorio")
    @Schema(description = "Ruolo dell'utente", example = "USER", allowableValues = {"USER", "ADMIN"})
    private Utente.Ruolo ruolo;
}
