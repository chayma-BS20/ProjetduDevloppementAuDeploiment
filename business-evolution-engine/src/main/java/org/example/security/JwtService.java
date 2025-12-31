package org.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final String SECRET_KEY = "mySecretKeyMustBe32CharsMinimum1234567890ABCDEF";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1h

    public String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // ✅ setSubject (0.11.5)
                .claim("roles", roles)
                .setIssuedAt(new Date())  // ✅ setIssuedAt
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))  // ✅ 0.11.5
                .compact();
    }
}
