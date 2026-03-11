package org.example.basic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value(value = "${jwt.secret}")
    private String jwtSecret;
    @Value(value = "${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;
    @Value(value = "${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    private SecretKey secretKey;

    private Key key() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        }
        return secretKey;
    }

    public String generateAccessToken(SecurityUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(key())
                .compact();
    }

    public boolean isValidToken(String token) {
        Jwts.parser()
                .verifyWith((SecretKey) key())
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
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateRefreshToken(SecurityUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(key())
                .compact();
    }
}
