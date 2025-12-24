package org.example.security;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // Version simplifiée sans JWT pour commencer
    public String generateToken(Object authentication) {
        return "fake-jwt-token-for-testing";  // Token fake pour tests
    }
}
