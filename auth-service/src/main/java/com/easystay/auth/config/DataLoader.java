package com.easystay.auth.config;

import com.easystay.auth.model.Utente;
import com.easystay.auth.repository.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        String defaultEmail = "user@easystay.com";
        
        if (!utenteRepository.existsByEmail(defaultEmail)) {
            Utente defaultUser = new Utente();
            defaultUser.setEmail(defaultEmail);
            defaultUser.setPassword(passwordEncoder.encode("User123!"));
            defaultUser.setNome("User");
            defaultUser.setRuolo(Utente.Ruolo.USER);
            
            utenteRepository.save(defaultUser);
            logger.info("✓ Utente di default creato: {}", defaultEmail);
        } else {
            logger.info("→ Utente {} già esistente, skip creazione", defaultEmail);
        }
    }
}
