package com.example.trustcare.repository;

import com.example.trustcare.model.Admin;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AdminDAO adminDAO;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setAdminId(1);
        admin.setEmail("test@example.com");
        admin.setPassword("password");
    }

    @Test
    void testSaveAdmin() {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(java.util.Collections.singletonMap("GENERATED_KEY", 1));
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        adminDAO.saveAdmin(admin);

        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void testGetAdminById() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(1)))
                .thenReturn(admin);
        Admin result = adminDAO.getAdminById(1);
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(1));
    }

    @Test
    void testGetAdminByEmail() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq("test@example.com")))
                .thenReturn(admin);
        Admin result = adminDAO.getAdminByEmail("test@example.com");
        assertNotNull(result);
        assertEquals(1, result.getAdminId());
    }

    @Test
    void testGetAllAdmins() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(admin));
        List<Admin> admins = adminDAO.getAllAdmins();
        assertEquals(1, admins.size());
        verify(jdbcTemplate, times(1))
                .query(anyString(), any(BeanPropertyRowMapper.class));
    }

    @Test
    void testApproveCaregiver() {
        adminDAO.approveCaregiver(101);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(101));
    }

    @Test
    void testRejectCaregiver() {
        adminDAO.rejectCaregiver(102);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(102));
    }

    @Test
    void testGetUnverifiedCaregivers() {
        Caregiver caregiver = new Caregiver();
        caregiver.setCareGiverId(200);
        caregiver.setFullName("Jane Doe");

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(caregiver));

        List<Caregiver> caregivers = adminDAO.getUnverifiedCaregivers();

        assertEquals(1, caregivers.size());
        assertEquals("Jane Doe", caregivers.get(0).getFullName());
    }

    @Test
    void testGetComplaints() {
        Complaint complaint = new Complaint();
        complaint.setComplaintId(1);

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(complaint));

        List<Complaint> complaints = adminDAO.getComplaints();

        assertEquals(1, complaints.size());
    }

    @Test
    void testGetPayments() {
        Payment payment = new Payment();
        payment.setPaymentId(1);

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(payment));

        List<Payment> payments = adminDAO.getPayments();

        assertEquals(1, payments.size());
    }

    @Test
    void testAddCaregiver() {
        Caregiver caregiver = new Caregiver();
        caregiver.setCareGiverId(300);
        caregiver.setFullName("John Smith");
        caregiver.setEmail("john@example.com");
        caregiver.setCNIC("12345-6789012-3");
        caregiver.setExperienceYears(5);
        caregiver.setMonthlyRate(BigDecimal.valueOf(50000));
        caregiver.setLocation("City");
        caregiver.setAddress("Street 123");
        caregiver.setPhoneNumber("1234567890");
        caregiver.setBio("Experienced caregiver");
        caregiver.setVerified(false);
        caregiver.setCreatedAt(LocalDateTime.now());
        caregiver.setPassword("secret");

        adminDAO.addCaregiver(caregiver);

        verify(jdbcTemplate, times(1)).update(anyString(),
                eq(300), eq("John Smith"), eq("john@example.com"), eq("12345-6789012-3"),
                eq(5), eq(50000), eq("City"), eq("Street 123"), eq("1234567890"),
                eq("Experienced caregiver"), eq(false), any(Timestamp.class), eq("secret"));
    }

    @Test
    void testGetAdminByEmail_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq("missing@example.com")))
                .thenThrow(new org.springframework.dao.EmptyResultDataAccessException(1));

        Admin result = adminDAO.getAdminByEmail("missing@example.com");
        assertNull(result);
    }

    @Test
    void testGetAllAdmins_Exception() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenThrow(new RuntimeException("DB error"));

        Exception exception = assertThrows(RuntimeException.class, () -> adminDAO.getAllAdmins());
        assertEquals("DB error", exception.getMessage());
    }

    @Test
    void testGetUnverifiedCaregivers_Empty() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of());

        List<Caregiver> caregivers = adminDAO.getUnverifiedCaregivers();
        assertTrue(caregivers.isEmpty());
    }

    @Test
    void testApproveCaregiver_Exception() {
        doThrow(new RuntimeException("DB fail")).when(jdbcTemplate).update(anyString(), anyInt());
        assertDoesNotThrow(() -> adminDAO.approveCaregiver(101));
    }

    @Test
    void testRejectCaregiver_Exception() {
        doThrow(new RuntimeException("DB fail")).when(jdbcTemplate).update(anyString(), anyInt());
        assertDoesNotThrow(() -> adminDAO.rejectCaregiver(102));
    }

    @Test
    void testGetComplaints_Exception() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenThrow(new RuntimeException("DB fail"));
        assertThrows(RuntimeException.class, () -> adminDAO.getComplaints());
    }

    @Test
    void testGetPayments_Exception() {
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenThrow(new RuntimeException("DB fail"));
        assertThrows(RuntimeException.class, () -> adminDAO.getPayments());
    }
    @Test
    void testAddCaregiver_Exception() {
        Caregiver caregiver = new Caregiver();
        caregiver.setCareGiverId(1);
        doThrow(new RuntimeException("DB fail")).when(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        assertDoesNotThrow(() -> adminDAO.addCaregiver(caregiver));
    }

}
