//package com.example.trustcare.controllers;
//
//import com.example.trustcare.controller.UserController;
//import com.example.trustcare.dto.BookingRequest;
//import com.example.trustcare.dto.ComplaintRequest;
//import com.example.trustcare.dto.ReviewRequest;
//import com.example.trustcare.model.Booking;
//import com.example.trustcare.model.Complaint;
//import com.example.trustcare.model.Payment;
//import com.example.trustcare.model.User;
//import com.example.trustcare.repository.UserDAO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserDAO userDAO;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetUserProfile() throws Exception {
//        User user = new User();
//        when(userDAO.viewUserProfile(1)).thenReturn(user);
//
//        mockMvc.perform(get("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(userDAO, times(1)).viewUserProfile(1);
//    }
//
//    @Test
//    void testUpdateUserProfile() throws Exception {
//        User user = new User();
//        mockMvc.perform(put("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(user)));
//
//        verify(userDAO, times(1)).editUserProfile(eq(1), any(User.class));
//    }
//
//    @Test
//    void testViewAllBookings() throws Exception {
//        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
//        when(userDAO.viewAllBookings(1)).thenReturn(bookings);
//
//        mockMvc.perform(get("/users/1/bookings")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//
//        verify(userDAO, times(1)).viewAllBookings(1);
//    }
//
//    @Test
//    void testCreateNewBooking() throws Exception {
//        BookingRequest request = new BookingRequest();
//        request.setCaregiverId(2);
//        request.setStartDate("2025-08-01");
//        request.setEndDate("2025-08-05");
//        request.setStatus("PENDING");
//        request.setTotalAmount(BigDecimal.valueOf(500));
//
//        mockMvc.perform(post("/users/1/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Booking created successfully"));
//
//        verify(userDAO, times(1))
//                .createNewBooking(eq(1), eq(2), any(LocalDate.class), any(LocalDate.class), eq("PENDING"), eq(BigDecimal.valueOf(500)));
//    }
//
//    @Test
//    void testGetBookingById() throws Exception {
//        Booking booking = new Booking();
//        when(userDAO.viewBooking(10)).thenReturn(booking);
//
//        mockMvc.perform(get("/users/bookings/10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(userDAO, times(1)).viewBooking(10);
//    }
//
//    @Test
//    void testSubmitReview() throws Exception {
//        ReviewRequest review = new ReviewRequest();
//        review.setBookingId(10);
//        review.setRating(5);
//        review.setComments("Excellent");
//        review.setCreatedAt("2025-08-25T12:00:00");
//
//        mockMvc.perform(post("/users/1/reviews")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(review)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Review submitted successfully"));
//
//        verify(userDAO, times(1))
//                .submitReview(eq(10), eq(5), eq("Excellent"), any(LocalDateTime.class));
//    }
//
//    @Test
//    void testSubmitComplaint() throws Exception {
//        ComplaintRequest complaint = new ComplaintRequest();
//        complaint.setBookingId(10);
//        complaint.setDescription("Problem with service");
//        complaint.setCreatedAt("2025-08-25T12:00:00");
//
//        mockMvc.perform(post("/users/1/complaints")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(complaint)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Complaint submitted successfully"));
//
//        verify(userDAO, times(1))
//                .submitComplaint(eq(10), eq(1), eq("Problem with service"), any(LocalDateTime.class));
//    }
//
//    @Test
//    void testViewPayment() throws Exception {
//        Payment payment = new Payment();
//        when(userDAO.viewPayment(100)).thenReturn(payment);
//
//        mockMvc.perform(get("/users/1/payments/100")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(userDAO, times(1)).viewPayment(100);
//    }
//}
