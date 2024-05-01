package com.example.bcpp.service;

import com.example.bcpp.config.AppConfig;
import com.example.bcpp.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private static final String SECRET_KEY = "15a090c039afc96c84380bd1a04f7fa62615d965aa1b6928260243ee33bb68d2";

    private static final Long EXPIRATION = 86400000L;

    private final UserDetails userDetails = createUserDetails();

    private UserDetails createUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                "username",
                "password123",
                new ArrayList<>());
    }

    @Mock
    private AppConfig appConfig;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    void shouldExtractUsername_whenExtractUsername_ifSubjectExists() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);

        verify(appConfig, times(2)).getJwtSecretKey();
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldExtractClaim_whenExtractClaim_ifTokenIsValid() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        // Extracting claim
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals(userDetails.getUsername(), subject);

        verify(appConfig, times(2)).getJwtSecretKey();
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldGenerateToken_whenGenerateToken_ifUserDetailsProvided() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // When: Generating token
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        verify(appConfig).getJwtSecretKey();
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldReturnTrue_whenIsTokenValid_ifTokenIsValid() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(EXPIRATION);

        // Generating token
        String token = jwtService.generateToken(userDetails);

        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isTokenValid);

        verify(appConfig, times(3)).getJwtSecretKey();
        verify(appConfig).getJwtExpiration();
    }

    @Test
    void shouldReturnFalse_whenIsTokenValid_ifTokenIsNotValid() {
        // Given: Mocking secret key
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);

        // Generating invalid token
        String token = Jwts.builder()
                .setSubject("nonExistingUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .compact();

        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);
        assertFalse(isTokenValid);

        verify(appConfig).getJwtSecretKey();
    }

    @Test
    void shouldReturnFalse_whenIsTokenValid_ifTokenIsExpired() {
        // Given: Mocking secret key and expiration
        when(appConfig.getJwtSecretKey()).thenReturn(SECRET_KEY);
        when(appConfig.getJwtExpiration()).thenReturn(0L);

        // Generating expired token
        String token = jwtService.generateToken(userDetails);

        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class,
                () -> jwtService.isTokenValid(token, userDetails));

        verify(appConfig, times(2)).getJwtSecretKey();

        verify(appConfig).getJwtExpiration();
    }

}