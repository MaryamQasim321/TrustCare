//package com.example.trustcare.controllers;
//
//import com.example.trustcare.controller.CaregiverController;
//import com.example.trustcare.dto.AvailabilityRequest;
//import com.example.trustcare.model.Booking;
//import com.example.trustcare.model.Caregiver;
//import com.example.trustcare.model.Complaint;
//import com.example.trustcare.repository.CaregiverDAO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CaregiverController.class)
//public class CaregiverControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CaregiverDAO caregiverDAO;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetCaregiverById() throws Exception {
//        Caregiver caregiver = new Caregiver();
//        when(caregiverDAO.getCaregiverById(1)).thenReturn(caregiver);
//
//        mockMvc.perform(get("/caregivers/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(caregiverDAO, times(1)).getCaregiverById(1);
//    }
//
//    @Test
//    void testEditCaregiver() throws Exception {
//        Caregiver caregiver = new Caregiver();
//        mockMvc.perform(put("/caregivers/1/edit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(caregiver)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("caregiver edited"));
//
//        verify(caregiverDAO, times(1)).editCaregiverProfile(eq(1), any(Caregiver.class));
//    }
//
//    @Test
//    void testGetIncomingRequests() throws Exception {
//        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
//        when(caregiverDAO.getIncomingBookings(1)).thenReturn(bookings);
//
//        mockMvc.perform(get("/caregivers/1/requests")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//
//        verify(caregiverDAO, times(1)).getIncomingBookings(1);
//    }
//
//    @Test
//    void testAcceptBooking() throws Exception {
//        mockMvc.perform(put("/caregivers/1/requests/10/accept"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Booking accepted."));
//
//        verify(caregiverDAO, times(1)).acceptBooking(10);
//    }
//
//    @Test
//    void testRejectBooking() throws Exception {
//        mockMvc.perform(put("/caregivers/1/requests/10/reject"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Booking rejected."));
//
//        verify(caregiverDAO, times(1)).rejectBooking(10);
//    }
//
//    @Test
//    void testViewComplaints() throws Exception {
//        List<Complaint> complaints = Arrays.asList(new Complaint(), new Complaint());
//        when(caregiverDAO.viewComplaints(1)).thenReturn(complaints);
//
//        mockMvc.perform(get("/caregivers/1/complaints")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//
//        verify(caregiverDAO, times(1)).viewComplaints(1);
//    }
//
//    @Test
//    void testGetAvailableDays() throws Exception {
//        AvailabilityRequest request = new AvailabilityRequest();
//        request.setStartDate("2025-08-01");
//        request.setEndDate("2025-08-05");
//
//        List<LocalDate> days = Arrays.asList(LocalDate.of(2025,8,1),
//                LocalDate.of(2025,8,2));
//        when(caregiverDAO.getAvailableDays(eq(1), any(LocalDate.class), any(LocalDate.class))).thenReturn(days);
//
//        mockMvc.perform(get("/caregivers/1/availability")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//
//        verify(caregiverDAO, times(1)).getAvailableDays(eq(1), any(LocalDate.class), any(LocalDate.class));
//    }
//
//    @Test
//    void testSetAvailability() throws Exception {
//        AvailabilityRequest request = new AvailabilityRequest();
//        request.setStartDate("2025-08-01");
//        request.setEndDate("2025-08-05");
//        request.setReason("Vacation");
//
//        mockMvc.perform(post("/caregivers/1/availability")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Availability updated."));
//
//        verify(caregiverDAO, times(1)).setAvailableSlots(eq(1), any(LocalDate.class), any(LocalDate.class), eq("Vacation"));
//    }
//}
