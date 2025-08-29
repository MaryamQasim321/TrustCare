package com.example.trustcare.controller;

import com.example.trustcare.dto.AuthRequest;
import com.example.trustcare.model.Admin;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.User;
import com.example.trustcare.repository.AdminDAO;
import com.example.trustcare.repository.CaregiverDAO;
import com.example.trustcare.repository.UserDAO;
import com.example.trustcare.security.JWTUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JWTUtility jwtUtility;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserDAO userDAO;
    @Mock private CaregiverDAO caregiverDAO;
    @Mock private AdminDAO adminDAO;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "encodedPass",
                Collections.singletonList((GrantedAuthority) () -> "ROLE_USER")
        );

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                mockUserDetails, null, mockUserDetails.getAuthorities()
        );

        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtility.generateToken(mockUserDetails)).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void testSignupUser() throws Exception {
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        doNothing().when(userDAO).saveUser(any(User.class));

        mockMvc.perform(post("/auth/signUp/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testSignupCaregiver() throws Exception {
        when(passwordEncoder.encode("secret")).thenReturn("encodedSecret");
        doNothing().when(caregiverDAO).saveCaregiver(any(Caregiver.class));

        mockMvc.perform(post("/auth/signUp/caregiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Caregiver1\",\"email\":\"care@example.com\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("caregiver registered successfully"));
    }

    @Test
    void testSignupAdmin() throws Exception {
        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");
        doNothing().when(adminDAO).saveAdmin(any(Admin.class));

        mockMvc.perform(post("/auth/signUp/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"SuperAdmin\",\"email\":\"admin@example.com\",\"password\":\"adminpass\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("admin registered successfully"));
    }
}