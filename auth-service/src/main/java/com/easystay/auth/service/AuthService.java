package com.easystay.auth.service;

import com.easystay.auth.dto.AuthResponse;
import com.easystay.auth.dto.LoginRequest;
import com.easystay.auth.dto.RegisterRequest;
import com.easystay.auth.exception.BadCredentialsException;
import com.easystay.auth.exception.EmailAlreadyExistsException;
import com.easystay.auth.model.Utente;
import com.easystay.auth.repository.UtenteRepository;
import com.easystay.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setPassword(passwordEncoder.encode(request.getPassword()));
        utente.setNome(request.getNome());
        utente.setRuolo(request.getRuolo());

        Utente savedUtente = utenteRepository.save(utente);

        String token = jwtUtil.generateToken(
                savedUtente.getId(),
                savedUtente.getEmail(),
                savedUtente.getRuolo().name()
        );

        return new AuthResponse(
                token,
                savedUtente.getId(),
                savedUtente.getEmail(),
                savedUtente.getNome(),
                savedUtente.getRuolo().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException());

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            throw new BadCredentialsException();
        }

        String token = jwtUtil.generateToken(
                utente.getId(),
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return new AuthResponse(
                token,
                utente.getId(),
                utente.getEmail(),
                utente.getNome(),
                utente.getRuolo().name()
        );
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
