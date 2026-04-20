package com.tfg.angel.gameswap.backend.auth;

import com.tfg.angel.gameswap.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String USERNAME = "angel_test";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String ROL = "ADMIN";
        String token = jwtService.generateToken(USERNAME, ROL);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertEquals(USERNAME, jwtService.extractUsername(token));
        assertEquals(ROL, jwtService.extractRol(token));
    }

    @Test
    void generateRefreshToken_ShouldBeValidAndHaveCorrectSubject() {
        String refreshToken = jwtService.generateRefreshToken(USERNAME);

        assertNotNull(refreshToken);
        assertTrue(jwtService.isValid(refreshToken));
        assertEquals(USERNAME, jwtService.extractUsername(refreshToken));
        assertNull(jwtService.extractRol(refreshToken));
    }

    @Test
    void isValid_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "este.no.es.un.token.valido";

        assertFalse(jwtService.isValid(invalidToken));
    }

    @Test
    void isValid_ShouldReturnFalseForExpiredToken() {
        assertFalse(jwtService.isValid(""));
        assertFalse(jwtService.isValid(null));
    }

    @Test
    void extractUsername_ShouldThrowExceptionForMalformedToken() {
        assertThrows(Exception.class, () -> jwtService.extractUsername("token.mal.formado"));
    }
}
