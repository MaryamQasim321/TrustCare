package com.example.trustcare.repository;

import com.example.trustcare.model.*;
import com.example.trustcare.service.SnsPublisherService;
import com.example.trustcare.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CaregiverDAO caregiverDAO;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private SnsPublisherService snsPublisherService;

    @InjectMocks
    private UserDAO userDAO;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setFullName("Test User");
    }

    @Test
    void testGetAllUsers() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(mockUser));

        var users = userDAO.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
    }

    @Test
    void testSaveUser() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(java.util.Map.of("GENERATED_KEY", 1));
            return 1;
        });

        assertDoesNotThrow(() -> userDAO.saveUser(mockUser));
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void testViewUserProfile() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), anyInt()))
                .thenReturn(mockUser);

        User user = userDAO.viewUserProfile(1);
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq("test@example.com")))
                .thenReturn(mockUser);

        User user = userDAO.getUserByEmail("test@example.com");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testEditUserProfile() {
        // Use thenReturn() for update instead of doNothing()
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        userDAO.editUserProfile(1, mockUser);

        verify(jdbcTemplate, times(1)).update(contains("UPDATE User SET"), any(Object[].class));
    }

    @Test
    void testViewAllBookings() {
        Booking booking = new Booking();
        booking.setBookingId(10);

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyInt()))
                .thenReturn(List.of(booking));

        List<Booking> result = userDAO.viewAllBookings(1);
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getBookingId());
    }

    @Test
    void testCreateNewBooking() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(java.util.Map.of("GENERATED_KEY", 100));
            return 1;
        });

        assertDoesNotThrow(() -> userDAO.createNewBooking(
                1, 2, LocalDate.now(), LocalDate.now().plusDays(1), "PENDING", BigDecimal.TEN
        ));

        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void testSubmitComplaint() {
        Booking booking = new Booking();
        booking.setBookingId(20);
        booking.setCaregiverId(5);

        when(caregiverDAO.getBookingById(20)).thenReturn(booking);
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyString(), any()))
                .thenReturn(1);

        assertDoesNotThrow(() ->
                userDAO.submitComplaint(20, 1, "bad service", LocalDateTime.now()));

        verify(jdbcTemplate, times(1))
                .update(anyString(), anyInt(), anyInt(), anyString(), any());
        verify(caregiverDAO, times(1)).getBookingById(20);
    }

    @Test
    void testViewBooking() {
        Booking booking = new Booking();
        booking.setBookingId(99);
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(99)))
                .thenReturn(booking);
        Booking result = userDAO.viewBooking(99);
        assertNotNull(result);
        assertEquals(99, result.getBookingId());
    }

    @Test
    void testViewPayment() {
        Payment payment = new Payment();
        payment.setPaymentId(50);

        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(50)))
                .thenReturn(payment);

        Payment result = userDAO.viewPayment(50);
        assertNotNull(result);
        assertEquals(50, result.getPaymentId());
    }

    @Test
    void testSubmitReview() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyString(), any()))
                .thenReturn(1);

        assertDoesNotThrow(() ->
                userDAO.submitReview(10, 5, "Good service", LocalDateTime.now()));

        verify(jdbcTemplate, times(1))
                .update(anyString(), anyInt(), anyInt(), anyString(), any());
    }


    @Test
    void testViewUserProfile_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), anyInt()))
                .thenThrow(new RuntimeException("DB error"));

        User user = userDAO.viewUserProfile(1);
        assertNull(user);  // should return null on exception
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        User user = userDAO.getUserByEmail("nonexistent@example.com");
        assertNull(user);
    }

    @Test
    void testEditUserProfile_NullFields() {
        User userWithNulls = new User(); // all fields null
        when(jdbcTemplate.update(anyString())).thenReturn(1);

        assertDoesNotThrow(() -> userDAO.editUserProfile(1, userWithNulls));
        verify(jdbcTemplate, times(1)).update(anyString());
    }

    @Test
    void testSubmitComplaint_Exception() {
        when(caregiverDAO.getBookingById(anyInt())).thenThrow(new RuntimeException("DB fail"));
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyString(), any())).thenReturn(1);

        assertDoesNotThrow(() -> userDAO.submitComplaint(1, 1, "test", LocalDateTime.now()));
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyInt(), anyString(), any());
    }

    @Test
    void testCreateNewBooking_Exception() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenThrow(new RuntimeException("DB fail"));

        assertDoesNotThrow(() -> userDAO.createNewBooking(1, 2, LocalDate.now(), LocalDate.now().plusDays(1), "PENDING", BigDecimal.TEN));
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void testSubmitReview_Exception() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyString(), any())).thenThrow(new RuntimeException("DB fail"));

        assertDoesNotThrow(() -> userDAO.submitReview(10, 5, "Good service", LocalDateTime.now()));
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyInt(), anyString(), any());
    }

}
