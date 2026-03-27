package org.example.basic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.basic.entities.User;
import org.example.basic.properties.SecurityProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecurityProperties securityProperties;
    private SecretKey secretKey;

    private SecretKey key() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(securityProperties.getSecretKey()));
        }
        return secretKey;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(securityProperties.userAccessMaxAge())))
                .signWith(key())
                .compact();
    }

    public boolean isValidToken(String token) {
        Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token);
        return true;
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
