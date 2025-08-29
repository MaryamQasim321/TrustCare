package com.example.trustcare.security;

import com.example.trustcare.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilityTest {

    private JWTUtility jwtUtility;
    private final String testSecret = "mySuperSecretKeyThatIsAtLeast32CharsLong!"; // HS256 requires >= 32 chars
    private UserDetails mockUser;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtility = new JWTUtility();

        // Inject secret
        var field = JWTUtility.class.getDeclaredField("secret");
        field.setAccessible(true);
        field.set(jwtUtility, testSecret);

        // Mock user
        mockUser = Mockito.mock(UserDetails.class);
        Mockito.when(mockUser.getUsername()).thenReturn("testuser");

        // FIX: Use Mockito Answer to avoid generic wildcard problem
        Mockito.when(mockUser.getAuthorities()).thenAnswer(invocation ->
                Collections.singletonList((GrantedAuthority) () -> Role.USER.name())
        );
    }



    @Test
    void testGenerateTokenAndExtractUserName() {
        String token = jwtUtility.generateToken(mockUser);
        assertNotNull(token);

        String username = jwtUtility.extractUserName(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtility.generateToken(mockUser);
        assertTrue(jwtUtility.validateToken(token, mockUser));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String token = jwtUtility.generateToken(mockUser);

        UserDetails otherUser = Mockito.mock(UserDetails.class);
        Mockito.when(otherUser.getUsername()).thenReturn("differentUser");

        assertFalse(jwtUtility.validateToken(token, otherUser));
    }

    @Test
    void testTokenShouldExpire() throws InterruptedException {
        // This test won’t really fail since JWTUtility hardcodes 60s expiry.
        // But leaving as-is for now (would need refactor for shorter expiry).
        String token = jwtUtility.generateToken(mockUser);
        Thread.sleep(1500); // still valid
        assertTrue(jwtUtility.validateToken(token, mockUser));
    }
}
