package it.easystay.auth.dto;

import it.easystay.common.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {

    private String email;
    private String nome;
    private Ruolo ruolo;
    private String token;
}
