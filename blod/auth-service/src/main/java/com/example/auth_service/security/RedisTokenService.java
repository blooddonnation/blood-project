package com.example.auth_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RedisTokenService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.expiration:86400000}")//expiration time b miliseconds 
    private long jwtExpirationMs;

    private static final String TOKEN_PREFIX = "jwt:";

    public void storeToken(String username, String token) {
        try {
            redisTemplate.opsForValue()
                    .set(TOKEN_PREFIX + username, token, jwtExpirationMs, TimeUnit.MILLISECONDS);
            logger.debug("Stored token for user: {}", username);
        } catch (Exception e) {
            logger.error("Failed to store token for user {}: {}", username, e.getMessage());
        }
    }

    public String getToken(String username) {
        try {
            String token = redisTemplate.opsForValue().get(TOKEN_PREFIX + username);
            if (token == null) {
                logger.debug("No token found for user: {}", username);
            }
            return token;
        } catch (Exception e) {
            logger.error("Failed to retrieve token for user {}: {}", username, e.getMessage());
            return null;
        }
    }

    public void deleteToken(String username) {
        try {
            redisTemplate.delete(TOKEN_PREFIX + username);
            logger.debug("Deleted token for user: {}", username);
        } catch (Exception e) {
            logger.error("Failed to delete token for user {}: {}", username, e.getMessage());
        }
    }

    public void testConnection() {
        try {
            redisTemplate.hasKey("test");
            logger.debug("Redis connection test successful");
        } catch (Exception e) {
            logger.warn("Redis connection test failed: {}", e.getMessage());
            throw e; 
        }
    }

    public boolean isTokenValid(String username, String token) {
        try {
            String storedToken = getToken(username);
            boolean isValid = storedToken != null && storedToken.equals(token);
            if (!isValid) {
                logger.debug("Token validation failed for user: {}. Stored token: {}, Provided token: {}", 
                        username, storedToken, token);
            }
            return isValid;
        } catch (Exception e) {
            logger.error("Failed to validate token for user {}: {}", username, e.getMessage());
            return false;
        }
    }
}