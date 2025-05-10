package com.example.auth_service;

import com.example.auth_service.security.RedisTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=l9ssirhaywanXkcdIsTheBestComicEverCreatedByRandallMunroeAndIHighlyRecommendItToEveryoneWhoLovesHumorAndGeekCulture",
    "jwt.expiration=86400000",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=update"
})
class AuthServiceApplicationTests {

    @MockBean
    private RedisTokenService redisTokenService;

    @BeforeEach
    void setUp() {
        // Mock RedisTokenService behavior
        doNothing().when(redisTokenService).storeToken(anyString(), anyString());
        doNothing().when(redisTokenService).deleteToken(anyString());
        doNothing().when(redisTokenService).testConnection();
        when(redisTokenService.getToken(anyString())).thenReturn("mockedToken");
        when(redisTokenService.isTokenValid(anyString(), anyString())).thenReturn(true);
    }

    @Test
    void contextLoads() {
    }
}