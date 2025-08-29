package com.example.trustcare.controller;

import com.example.trustcare.dto.AvailabilityRequest;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.repository.CaregiverDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class CaregiverControllerTest {

    private CaregiverDAO caregiverDAOMock;
    private CaregiverController caregiverController;

    @BeforeEach
    void setUp() {
        caregiverDAOMock = Mockito.mock(CaregiverDAO.class);
        caregiverController = new CaregiverController(caregiverDAOMock);
    }

    @Test
    void testGetCaregiverById() {
        Caregiver caregiver = new Caregiver();
        caregiver.setCareGiverId(1);
        caregiver.setFullName("John Doe");

        Mockito.when(caregiverDAOMock.getCaregiverById(1)).thenReturn(caregiver);

        Caregiver result = caregiverController.getCaregiverById(1);
        assertEquals(caregiver, result);
    }

    @Test
    void testEditCaregiver() {
        Caregiver caregiver = new Caregiver();
        caregiver.setCareGiverId(1);

        String result = caregiverController.editCaregiver(1, caregiver);

        Mockito.verify(caregiverDAOMock).editCaregiverProfile(1, caregiver);
        assertEquals("caregiver edited", result);
    }

    @Test
    void testGetIncomingRequests() {
        Booking booking = new Booking();
        booking.setBookingId(1);
        List<Booking> bookings = Collections.singletonList(booking);

        Mockito.when(caregiverDAOMock.getIncomingBookings(1)).thenReturn(bookings);

        List<Booking> result = caregiverController.getIncomingRequests(1);
        assertEquals(bookings, result);
    }

    @Test
    void testAcceptBooking() {
        String result = caregiverController.acceptBooking(1, 100);

        Mockito.verify(caregiverDAOMock).acceptBooking(100);
        assertEquals("Booking accepted.", result);
    }

    @Test
    void testRejectBooking() {
        String result = caregiverController.rejectBooking(1, 100);

        Mockito.verify(caregiverDAOMock).rejectBooking(100);
        assertEquals("Booking rejected.", result);
    }

    @Test
    void testViewComplaints() {
        Complaint complaint = new Complaint();
        complaint.setComplaintId(1);
        List<Complaint> complaints = Collections.singletonList(complaint);

        Mockito.when(caregiverDAOMock.viewComplaints(1)).thenReturn(complaints);

        List<Complaint> result = caregiverController.viewComplaints(1);
        assertEquals(complaints, result);
    }

    @Test
    void testGetAvailableDays() {
        AvailabilityRequest request = new AvailabilityRequest();
        request.setStartDate("2025-08-28");
        request.setEndDate("2025-08-30");

        List<LocalDate> availableDays = Arrays.asList(
                LocalDate.parse("2025-08-28"),
                LocalDate.parse("2025-08-29"),
                LocalDate.parse("2025-08-30")
        );

        Mockito.when(caregiverDAOMock.getAvailableDays(eq(1),
                        any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(availableDays);

        List<LocalDate> result = caregiverController.getAvailableDays(1, request);
        assertEquals(availableDays, result);
    }

    @Test
    void testSetAvailability() {
        AvailabilityRequest request = new AvailabilityRequest();
        request.setStartDate("2025-08-28");
        request.setEndDate("2025-08-30");
        request.setReason("Vacation");

        String result = caregiverController.setAvailability(1, request);

        Mockito.verify(caregiverDAOMock).setAvailableSlots(
                eq(1),
                eq(LocalDate.parse("2025-08-28")),
                eq(LocalDate.parse("2025-08-30")),
                eq("Vacation")
        );

        assertEquals("Availability updated.", result);
    }
}
