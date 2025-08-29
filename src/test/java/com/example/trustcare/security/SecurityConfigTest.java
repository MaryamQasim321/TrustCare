package com.example.trustcare.security;

import com.example.trustcare.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private JWTFilter jwtFilterMock;
    private CustomUserDetailsService userDetailsServiceMock;

    @BeforeEach
    void setUp() {
        jwtFilterMock = Mockito.mock(JWTFilter.class);
        userDetailsServiceMock = Mockito.mock(CustomUserDetailsService.class);

        // Use reflection to inject private fields
        securityConfig = new SecurityConfig();
        try {
            var jwtField = SecurityConfig.class.getDeclaredField("jwtFilter");
            jwtField.setAccessible(true);
            jwtField.set(securityConfig, jwtFilterMock);

            var userField = SecurityConfig.class.getDeclaredField("customUserDetailsService");
            userField.setAccessible(true);
            userField.set(securityConfig, userDetailsServiceMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFilterChainBuilds() throws Exception {
        // Mock HttpSecurity with deep stubs
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = securityConfig.filterChain(http);
        assertNotNull(chain);

        // Verify addFilterBefore is called with our JWTFilter
        verify(http, atLeastOnce()).addFilterBefore(eq(jwtFilterMock), any());
    }

    @Test
    void testAuthenticationManagerBuilds() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        AuthenticationManager authManager = securityConfig.authenticationManager(http);
        assertNotNull(authManager);

    }
}
