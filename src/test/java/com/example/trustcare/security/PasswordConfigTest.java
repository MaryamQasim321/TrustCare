package com.example.trustcare.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordConfigTest {

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = PasswordConfig.passwordEncoder();

        assertNotNull(encoder, "PasswordEncoder bean should not be null");
        assertTrue(encoder instanceof BCryptPasswordEncoder, "PasswordEncoder should be BCryptPasswordEncoder");

        String rawPassword = "mySecret123";
        String encoded = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded, "Encoded password should not match raw password");
        assertTrue(encoder.matches(rawPassword, encoded), "Encoder should match raw and encoded passwords");
    }
}
