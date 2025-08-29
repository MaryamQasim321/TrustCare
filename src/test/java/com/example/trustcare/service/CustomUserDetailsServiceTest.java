package com.example.trustcare.service;

import com.example.trustcare.enums.Role;
import com.example.trustcare.model.Admin;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.User;
import com.example.trustcare.repository.AdminDAO;
import com.example.trustcare.repository.CaregiverDAO;
import com.example.trustcare.repository.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UserDAO userDAO;
    @Mock private CaregiverDAO caregiverDAO;
    @Mock private AdminDAO adminDAO;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void loadUserByUsername_UserFound() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPass");

        when(adminDAO.getAdminByEmail("user@example.com")).thenReturn(null);
        when(userDAO.getUserByEmail("user@example.com")).thenReturn(user);
        when(caregiverDAO.getCaregiverByEmail("user@example.com")).thenReturn(null);

        UserDetails result = service.loadUserByUsername("user@example.com");

        assertEquals("user@example.com", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .anyMatch(auth -> auth.equals("ROLE_USER")));

        verify(userDAO).getUserByEmail("user@example.com");
        verify(adminDAO).getAdminByEmail("user@example.com");
        verify(caregiverDAO).getCaregiverByEmail("user@example.com");
    }

    @Test
    void loadUserByUsername_CaregiverFound() {
        Caregiver caregiver = new Caregiver();
        caregiver.setEmail("care@example.com");
        caregiver.setPassword("carePass");

        when(adminDAO.getAdminByEmail("care@example.com")).thenReturn(null);
        when(userDAO.getUserByEmail("care@example.com")).thenReturn(null);
        when(caregiverDAO.getCaregiverByEmail("care@example.com")).thenReturn(caregiver);

        UserDetails result = service.loadUserByUsername("care@example.com");

        assertEquals("care@example.com", result.getUsername());
        assertEquals("carePass", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .anyMatch(auth -> auth.equals("ROLE_CAREGIVER")));

        verify(caregiverDAO).getCaregiverByEmail("care@example.com");
    }

    @Test
    void loadUserByUsername_AdminFound() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setPassword("adminPass");

        when(adminDAO.getAdminByEmail("admin@example.com")).thenReturn(admin);
        when(userDAO.getUserByEmail("admin@example.com")).thenReturn(null);
        when(caregiverDAO.getCaregiverByEmail("admin@example.com")).thenReturn(null);

        UserDetails result = service.loadUserByUsername("admin@example.com");

        assertEquals("admin@example.com", result.getUsername());
        assertEquals("adminPass", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .anyMatch(auth -> auth.equals("ROLE_ADMIN")));

        verify(adminDAO).getAdminByEmail("admin@example.com");
    }

    @Test
    void loadUserByUsername_NotFound_ThrowsException() {
        when(adminDAO.getAdminByEmail("missing@example.com")).thenReturn(null);
        when(userDAO.getUserByEmail("missing@example.com")).thenReturn(null);
        when(caregiverDAO.getCaregiverByEmail("missing@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com"));
    }

    @Test
    void loadUserByUsername_DAOReturnsNullOrderCoverage() {
        // test that order of checking Admin -> User -> Caregiver is fully covered
        User user = new User();
        user.setEmail("user2@example.com");
        user.setPassword("pass2");

        // AdminDAO returns null
        when(adminDAO.getAdminByEmail("user2@example.com")).thenReturn(null);
        // UserDAO returns null
        when(userDAO.getUserByEmail("user2@example.com")).thenReturn(null);
        // CaregiverDAO returns null
        when(caregiverDAO.getCaregiverByEmail("user2@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("user2@example.com"));

        verify(adminDAO).getAdminByEmail("user2@example.com");
        verify(userDAO).getUserByEmail("user2@example.com");
        verify(caregiverDAO).getCaregiverByEmail("user2@example.com");
    }
}
