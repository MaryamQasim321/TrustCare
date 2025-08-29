package com.example.trustcare.controller;

import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.Payment;
import com.example.trustcare.repository.AdminDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminDAO adminDAO;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testGetUnverifiedCaregivers_Success() throws Exception {
        Caregiver caregiver1 = new Caregiver();
        caregiver1.setCareGiverId(1);
        caregiver1.setFullName("Caregiver One");

        Caregiver caregiver2 = new Caregiver();
        caregiver2.setCareGiverId(2);
        caregiver2.setFullName("Caregiver Two");

        List<Caregiver> caregivers = Arrays.asList(caregiver1, caregiver2);

        when(adminDAO.getUnverifiedCaregivers()).thenReturn(caregivers);

        mockMvc.perform(get("/admin/caregiver-verifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].caregiverId").value(1))
                .andExpect(jsonPath("$[1].caregiverId").value(2));

        verify(adminDAO, times(1)).getUnverifiedCaregivers();
    }

    @Test
    void testApproveCaregiver_Success() throws Exception {
        doNothing().when(adminDAO).approveCaregiver(1);

        mockMvc.perform(put("/admin/caregiver-verifications/1/approve"))
                .andExpect(status().isOk())
                .andExpect(content().string("Caregiver approved"));

        verify(adminDAO, times(1)).approveCaregiver(1);
    }

    @Test
    void testRejectCaregiver_Success() throws Exception {
        doNothing().when(adminDAO).rejectCaregiver(2);

        mockMvc.perform(put("/admin/caregiver-verifications/2/reject"))
                .andExpect(status().isOk())
                .andExpect(content().string("Caregiver rejected"));

        verify(adminDAO, times(1)).rejectCaregiver(2);
    }

    @Test
    void testGetComplaints_Success() throws Exception {
        Complaint complaint1 = new Complaint();
        complaint1.setBookingId(101);
        complaint1.setDescription("Complaint 1");

        Complaint complaint2 = new Complaint();
        complaint2.setBookingId(102);
        complaint2.setDescription("Complaint 2");

        List<Complaint> complaints = Arrays.asList(complaint1, complaint2);

        when(adminDAO.getComplaints()).thenReturn(complaints);

        mockMvc.perform(get("/admin/complaints")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(101))
                .andExpect(jsonPath("$[1].bookingId").value(102));

        verify(adminDAO, times(1)).getComplaints();
    }

    @Test
    void testGetPayments_Success() throws Exception {
        Payment payment1 = new Payment();
        payment1.setPaymentId(1001);
        payment1.setAmount(BigDecimal.valueOf(500));

        Payment payment2 = new Payment();
        payment2.setPaymentId(1002);
        payment2.setAmount(BigDecimal.valueOf(1000));

        List<Payment> payments = Arrays.asList(payment1, payment2);

        when(adminDAO.getPayments()).thenReturn(payments);

        mockMvc.perform(get("/admin/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentID").value(1001))
                .andExpect(jsonPath("$[1].paymentID").value(1002));

        verify(adminDAO, times(1)).getPayments();
    }

    @Test
    void testGetUnverifiedCaregivers_Error() throws Exception {
        when(adminDAO.getUnverifiedCaregivers()).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/admin/caregiver-verifications"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving unverified caregivers"));
    }

    @Test
    void testApproveCaregiver_Error() throws Exception {
        doThrow(new RuntimeException("DB error")).when(adminDAO).approveCaregiver(1);

        mockMvc.perform(put("/admin/caregiver-verifications/1/approve"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error approving caregiver"));
    }

    @Test
    void testRejectCaregiver_Error() throws Exception {
        doThrow(new RuntimeException("DB error")).when(adminDAO).rejectCaregiver(2);

        mockMvc.perform(put("/admin/caregiver-verifications/2/reject"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error rejecting caregiver"));
    }
}
