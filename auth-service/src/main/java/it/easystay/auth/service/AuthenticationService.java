package it.easystay.auth.service;

import it.easystay.auth.dto.AuthenticationRequestDTO;
import it.easystay.auth.dto.AuthenticationResponseDTO;
import it.easystay.auth.dto.RegisterRequestDTO;
import it.easystay.auth.dto.RegisterResponseDTO;
import it.easystay.auth.model.Utente;
import it.easystay.auth.repository.UtenteRepository;
import it.easystay.auth.security.JwtService;
import it.easystay.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        var utente = Utente.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ruolo(request.getRuolo())
                .build();

        utenteRepository.save(utente);
        var jwtToken = jwtService.generateToken(utente);

        return RegisterResponseDTO.builder()
                .email(utente.getEmail())
                .nome(utente.getNome())
                .ruolo(utente.getRuolo())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        // AuthenticationManager will throw AuthenticationException if credentials are invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // User should exist at this point since authentication succeeded
        // This orElseThrow is a safeguard for data inconsistency scenarios
        var utente = utenteRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        var jwtToken = jwtService.generateToken(utente);

        return new AuthenticationResponseDTO(jwtToken);
    }
}
