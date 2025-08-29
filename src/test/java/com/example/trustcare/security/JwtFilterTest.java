package com.example.trustcare.security;

import com.example.trustcare.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @InjectMocks
    private JWTFilter jwtFilter;

    @Mock
    private JWTUtility jwtUtility;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Clear SecurityContext before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotFilter_ForAuthPaths() throws Exception {
        when(request.getServletPath()).thenReturn("/auth/login");
        assertTrue(jwtFilter.shouldNotFilter(request));

        when(request.getServletPath()).thenReturn("/swagger-ui/index.html");
        assertTrue(jwtFilter.shouldNotFilter(request));

        when(request.getServletPath()).thenReturn("/v3/api-docs");
        assertTrue(jwtFilter.shouldNotFilter(request));

        when(request.getServletPath()).thenReturn("/webjars/something");
        assertTrue(jwtFilter.shouldNotFilter(request));
    }

    @Test
    void shouldFilter_ForNonExcludedPaths() throws Exception {
        when(request.getServletPath()).thenReturn("/users/1");
        assertFalse(jwtFilter.shouldNotFilter(request));
    }

    @Test
    void doFilterInternal_WithValidToken_SetsAuthentication() throws Exception {
        String token = "validToken";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtility.extractUserName(token)).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtility.validateToken(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(java.util.Collections.emptyList());

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Check SecurityContext set
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_SendsError() throws Exception {
        String token = "invalidToken";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtility.extractUserName(token)).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtility.validateToken(token, userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNoTokenHeader_CallsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithException_SendsError() throws Exception {
        when(request.getHeader("Authorization")).thenThrow(new RuntimeException("boom"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error validating JWT");
        verify(filterChain, never()).doFilter(request, response);
    }
}
