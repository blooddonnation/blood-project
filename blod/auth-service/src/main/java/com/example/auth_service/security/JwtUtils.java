package com.example.auth_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpirationMs;

    private SecretKey key;

    private final RedisTokenService redisTokenService;

    public JwtUtils(RedisTokenService redisTokenService) {
        this.redisTokenService = redisTokenService;
    }

    @PostConstruct
    public void init() {
        // valider jwt secret key wch hya hdik
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        try {
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JWT signing key: " + e.getMessage(), e);
        }

        // la t expirat lkey aka fok 24 h
        if (jwtExpirationMs <= 0) {
            throw new IllegalStateException("JWT expiration must be a positive number");
        }

        // Test redis
        try {
            if (redisTokenService != null) {
                redisTokenService.testConnection();
            }
        } catch (Exception e) {
            logger.warn("RedisTokenService connection test failed: {}. Proceeding without Redis integration.", e.getMessage());
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();

        // sauvegardi token b redis
        try {
            if (redisTokenService != null) {
                redisTokenService.storeToken(userPrincipal.getUsername(), token);
            }
        } catch (Exception e) {
            logger.error("Failed to store token in Redis for user {}: {}", userPrincipal.getUsername(), e.getMessage());
        }

        return token;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}