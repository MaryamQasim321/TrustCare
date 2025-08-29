package com.example.trustcare.repository;

import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.User;
import com.example.trustcare.service.SnsPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CaregiverDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SnsPublisherService snsPublisherService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    @Spy
    private CaregiverDAO caregiverDAO; // spy to mock internal method calls

    private Caregiver caregiver;
    private Booking booking;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        caregiver = new Caregiver();
        caregiver.setCareGiverId(1);
        caregiver.setEmail("caregiver@example.com");
        caregiver.setFullName("John Doe");
        caregiver.setExperienceYears(5);
        caregiver.setMonthlyRate(BigDecimal.valueOf(2000.0));

        booking = new Booking();
        booking.setBookingId(100);
        booking.setUserId(10);
        booking.setCaregiverId(1);

        user = new User();
        user.setUserId(10);
        user.setEmail("user@example.com");
    }

    @Test
    void testSaveCaregiver() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Collections.singletonMap("GENERATED_KEY", 1));
            return 1;
        });

        assertDoesNotThrow(() -> caregiverDAO.saveCaregiver(caregiver));
        verify(jdbcTemplate).update(any(), any(KeyHolder.class));
    }

    @Test
    void testGetBookingById_Found() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(100)))
                .thenReturn(booking);

        Booking result = caregiverDAO.getBookingById(100);
        assertNotNull(result);
        assertEquals(100, result.getBookingId());
    }

    @Test
    void testGetBookingById_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(200)))
                .thenThrow(new RuntimeException());

        assertThrows(UsernameNotFoundException.class, () -> caregiverDAO.getBookingById(200));
    }

    @Test
    void testGetCaregiverByEmail_Found() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq("caregiver@example.com")))
                .thenReturn(caregiver);

        Caregiver result = caregiverDAO.getCaregiverByEmail("caregiver@example.com");
        assertEquals("caregiver@example.com", result.getEmail());
    }

    @Test
    void testGetCaregiverByEmail_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), anyString()))
                .thenThrow(new RuntimeException());

        assertThrows(UsernameNotFoundException.class, () -> caregiverDAO.getCaregiverByEmail("x@example.com"));
    }

    @Test
    void testGetAllCareGivers() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(caregiver));

        List<Caregiver> result = caregiverDAO.getAllCareGivers();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
    }

    @Test
    void testGetCaregiverById() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(1)))
                .thenReturn(caregiver);

        Caregiver result = caregiverDAO.getCaregiverById(1);
        assertEquals(1, result.getCareGiverId());
    }

    @Test
    void testSearchCaregiverByName() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq("John Doe")))
                .thenReturn(caregiver);

        Caregiver result = caregiverDAO.searchCaregiverByName("John Doe");
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    void testGetAvailableDays() {
        LocalDate from = LocalDate.of(2025, 8, 28);
        LocalDate to = LocalDate.of(2025, 8, 30);

        doAnswer(invocation -> {
            var rs = invocation.getArgument(1, org.springframework.jdbc.core.RowCallbackHandler.class);
            return null; // simulate no unavailable days
        }).when(jdbcTemplate).query(anyString(), any(org.springframework.jdbc.core.RowCallbackHandler.class), any(), any(), any());

        List<LocalDate> days = caregiverDAO.getAvailableDays(1, from, to);
        assertEquals(3, days.size());
    }

    @Test
    void testSetAvailableSlots() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        caregiverDAO.setAvailableSlots(1, LocalDate.now(), LocalDate.now().plusDays(1), "Vacation");

        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testAcceptBooking() {
        doReturn(booking).when(caregiverDAO).getBookingById(100);
        when(userDAO.viewUserProfile(10)).thenReturn(user);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        caregiverDAO.acceptBooking(100);

        verify(snsPublisherService).publishEmailNotification(anyString(), anyString(), anyList());
    }

    @Test
    void testRejectBooking() {
        doReturn(booking).when(caregiverDAO).getBookingById(100);
        when(userDAO.viewUserProfile(10)).thenReturn(user);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        caregiverDAO.rejectBooking(100);

        verify(snsPublisherService).publishEmailNotification(anyString(), anyString(), anyList());
    }

    @Test
    void testViewComplaints() {
        Complaint complaint = new Complaint();
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), eq(1)))
                .thenReturn(List.of(complaint));

        List<Complaint> complaints = caregiverDAO.viewComplaints(1);
        assertEquals(1, complaints.size());
    }

    @Test
    void testEditCaregiverProfile_AllFieldsNullOrZero() {
        Caregiver empty = new Caregiver();
        assertDoesNotThrow(() -> caregiverDAO.editCaregiverProfile(1, empty));
    }

    @Test
    void testEditCaregiverProfile_WithFields() {
        Caregiver update = new Caregiver();
        update.setFullName("Jane Doe");
        update.setEmail("jane@example.com");
        update.setExperienceYears(5);
        update.setMonthlyRate(BigDecimal.valueOf(2000));

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        caregiverDAO.editCaregiverProfile(1, update);
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }
}
