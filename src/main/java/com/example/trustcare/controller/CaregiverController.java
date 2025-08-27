package com.example.trustcare.controller;
import com.example.trustcare.dto.AvailabilityRequest;
import com.example.trustcare.repository.CaregiverDAO;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/caregivers")
public  class CaregiverController {
    private static final Logger logger = LoggerFactory.getLogger(CaregiverDAO.class);

    private final CaregiverDAO caregiverDAO;
    public CaregiverController(@Lazy CaregiverDAO caregiverDAO) {
        this.caregiverDAO = caregiverDAO;
    }

    @PreAuthorize("hasRole('CAREGIVER') or hasRole('ADMIN')")
    @GetMapping("/{caregiverId}")
    public Caregiver getCaregiverById(@PathVariable int caregiverId) {
        logger.info("getCaregiverById caregiverId: " + caregiverId);
        return caregiverDAO.getCaregiverById(caregiverId);
    }

    @PreAuthorize("hasRole('CAREGIVER') and #id == principal.username")
    @PutMapping("/{caregiverId}/edit")
    public String editCaregiver(@PathVariable int caregiverId, @RequestBody Caregiver caregiver) {
        caregiverDAO.editCaregiverProfile(caregiverId, caregiver);
        logger.info("editCaregiver caregiverId: " + caregiverId);
        return "caregiver edited";
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @GetMapping("/{caregiverId}/requests")
    public List<Booking> getIncomingRequests(@PathVariable int caregiverId) {
        return caregiverDAO.getIncomingBookings(caregiverId);
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @PutMapping("/{caregiverId}/requests/{bookingId}/accept")
    public String acceptBooking(@PathVariable int caregiverId, @PathVariable int bookingId) {
        caregiverDAO.acceptBooking(bookingId);
        return "Booking accepted.";
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @PutMapping("/{caregiverId}/requests/{bookingId}/reject")
    public String rejectBooking(@PathVariable int caregiverId, @PathVariable int bookingId) {
        caregiverDAO.rejectBooking(bookingId);
        return "Booking rejected.";
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @GetMapping("/{caregiverId}/complaints")
    public List<Complaint> viewComplaints(@PathVariable int caregiverId) {
        return caregiverDAO.viewComplaints(caregiverId);
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @GetMapping("/{id}/availability")
    public List<LocalDate> getAvailableDays(@PathVariable int id,
                                            @RequestBody AvailabilityRequest request) {
        return caregiverDAO.getAvailableDays(id,
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate()));
    }

    @PreAuthorize("hasRole('CAREGIVER')")
    @PostMapping("/{id}/availability")
    public String setAvailability(@PathVariable int id,
                                  @RequestBody AvailabilityRequest request) {
        caregiverDAO.setAvailableSlots(id,
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate()),
                request.getReason());
        return "Availability updated.";
    }

}