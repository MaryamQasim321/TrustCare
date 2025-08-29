package com.example.trustcare.controller;

import com.example.trustcare.dto.BookingRequest;
import com.example.trustcare.dto.ComplaintRequest;
import com.example.trustcare.dto.ReviewRequest;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Payment;
import com.example.trustcare.model.User;
import com.example.trustcare.repository.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private UserDAO userDAOMock;
    private UserController userController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userDAOMock = Mockito.mock(UserDAO.class);
        userController = new UserController(userDAOMock);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setFullName("testuser");

        when(userDAOMock.viewUserProfile(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.fullName").value("testuser"));

        verify(userDAOMock, times(1)).viewUserProfile(1);
    }

    @Test
    void testGetUserProfile_Exception() throws Exception {
        when(userDAOMock.viewUserProfile(1)).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("DB error"));

        verify(userDAOMock, times(1)).viewUserProfile(1);
    }

    @Test
    void testUpdateUserProfile_Success() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setFullName("updated");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("updated"));

        verify(userDAOMock, times(1)).editUserProfile(eq(1), any(User.class));
    }

    @Test
    void testUpdateUserProfile_Exception() throws Exception {
        User user = new User();
        doThrow(new RuntimeException("Update failed"))
                .when(userDAOMock).editUserProfile(eq(1), any(User.class));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Update failed"));

        verify(userDAOMock, times(1)).editUserProfile(eq(1), any(User.class));
    }

    @Test
    void testViewAllBookings_Success() throws Exception {
        Booking booking = new Booking();
        booking.setUserId(1);
        List<Booking> bookings = Collections.singletonList(booking);

        when(userDAOMock.viewAllBookings(1)).thenReturn(bookings);

        mockMvc.perform(get("/users/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(userDAOMock, times(1)).viewAllBookings(1);
    }

    @Test
    void testCreateNewBooking_Success() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setCaregiverId(2);
        request.setStartDate("2025-08-28");
        request.setEndDate("2025-08-29");
        request.setStatus("CONFIRMED");
        request.setTotalAmount(new BigDecimal("100.00"));

        mockMvc.perform(post("/users/1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking created successfully"));

        verify(userDAOMock, times(1)).createNewBooking(
                eq(1),
                eq(2),
                eq(LocalDate.parse("2025-08-28")),
                eq(LocalDate.parse("2025-08-29")),
                eq("CONFIRMED"),
                eq(new BigDecimal("100.00"))
        );
    }

    @Test
    void testCreateNewBooking_InvalidDate() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setStartDate("invalid");
        request.setEndDate("2025-08-29");

        mockMvc.perform(post("/users/1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid date format. Use yyyy-MM-dd"));

        verify(userDAOMock, never()).createNewBooking(anyInt(), anyInt(), any(), any(), anyString(), any());
    }

    @Test
    void testCreateNewBooking_DAOException() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setCaregiverId(2);
        request.setStartDate("2025-08-28");
        request.setEndDate("2025-08-29");
        request.setStatus("CONFIRMED");
        request.setTotalAmount(new BigDecimal("100.00"));

        doThrow(new RuntimeException("DB failed"))
                .when(userDAOMock).createNewBooking(anyInt(), anyInt(), any(), any(), anyString(), any());

        mockMvc.perform(post("/users/1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("DB failed"));

        verify(userDAOMock, times(1)).createNewBooking(anyInt(), anyInt(), any(), any(), anyString(), any());
    }

    @Test
    void testGetBookingById_Success() throws Exception {
        Booking booking = new Booking();
        booking.setUserId(1);
        when(userDAOMock.viewBooking(1)).thenReturn(booking);

        mockMvc.perform(get("/users/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));

        verify(userDAOMock, times(1)).viewBooking(1);
    }

    @Test
    void testSubmitReview_Success() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setBookingId(1);
        request.setRating(5);
        request.setComments("Great!");
        request.setCreatedAt(LocalDateTime.now().toString());

        mockMvc.perform(post("/users/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review submitted successfully"));

        verify(userDAOMock, times(1)).submitReview(
                eq(1),
                eq(5),
                eq("Great!"),
                any(LocalDateTime.class)
        );
    }

    @Test
    void testSubmitReview_InvalidDateTime() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setCreatedAt("invalid");

        mockMvc.perform(post("/users/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid datetime format. Use yyyy-MM-ddTHH:mm:ss"));

        verify(userDAOMock, never()).submitReview(anyInt(), anyInt(), anyString(), any());
    }

    @Test
    void testSubmitComplaint_Success() throws Exception {
        ComplaintRequest request = new ComplaintRequest();
        request.setBookingId(1);
        request.setDescription("Issue");
        request.setCreatedAt(LocalDateTime.now().toString());

        mockMvc.perform(post("/users/1/complaints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Complaint submitted successfully"));

        verify(userDAOMock, times(1)).submitComplaint(
                eq(1),
                eq(1),
                eq("Issue"),
                any(LocalDateTime.class)
        );
    }

    @Test
    void testSubmitComplaint_InvalidDateTime() throws Exception {
        ComplaintRequest request = new ComplaintRequest();
        request.setCreatedAt("invalid");

        mockMvc.perform(post("/users/1/complaints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid datetime format. Use yyyy-MM-ddTHH:mm:ss"));

        verify(userDAOMock, never()).submitComplaint(anyInt(), anyInt(), anyString(), any());
    }

    @Test
    void testViewPayment_Success() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(1);

        when(userDAOMock.viewPayment(1)).thenReturn(payment);

        mockMvc.perform(get("/users/1/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1));

        verify(userDAOMock, times(1)).viewPayment(1);
    }

    @Test
    void testViewPayment_DAOException() throws Exception {
        doThrow(new RuntimeException("DB failed")).when(userDAOMock).viewPayment(1);

        mockMvc.perform(get("/users/1/payments/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("DB failed"));

        verify(userDAOMock, times(1)).viewPayment(1);
    }
}
