//package com.example.trustcare.controllers;
//
//import com.example.trustcare.TrustCareApplication;
//import jakarta.ws.rs.core.MediaType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(classes = TrustCareApplication.class)
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setupTestData() {
//        // Clear old data
//        jdbcTemplate.execute("DELETE FROM caregivers");
//        jdbcTemplate.execute("DELETE FROM complaints");
//        jdbcTemplate.execute("DELETE FROM payments");
//
//        // Insert test caregivers
//        jdbcTemplate.update("INSERT INTO caregivers (id, name, verified) VALUES (?, ?, ?)", 1, "Test Caregiver 1", false);
//        jdbcTemplate.update("INSERT INTO caregivers (id, name, verified) VALUES (?, ?, ?)", 2, "Test Caregiver 2", false);
//
//        // Insert test complaints
//        jdbcTemplate.update("INSERT INTO complaints (id, message, date) VALUES (?, ?, ?)", 1, "Complaint 1", LocalDate.now());
//        jdbcTemplate.update("INSERT INTO complaints (id, message, date) VALUES (?, ?, ?)", 2, "Complaint 2", LocalDate.now());
//
//        // Insert test payments
//        jdbcTemplate.update("INSERT INTO payments (id, amount) VALUES (?, ?)", 1, 100);
//        jdbcTemplate.update("INSERT INTO payments (id, amount) VALUES (?, ?)", 2, 200);
//    }
//
//    @Test
//    void testGetUnverifiedCaregivers() throws Exception {
//        mockMvc.perform(get("/admin/caregiver-verifications")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    void testApproveCaregiver() throws Exception {
//        mockMvc.perform(put("/admin/caregiver-verifications/{caregiverId}/approve", 1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Caregiver approved"));
//    }
//
//    @Test
//    void testRejectCaregiver() throws Exception {
//        mockMvc.perform(put("/admin/caregiver-verifications/{caregiverId}/reject", 2)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Caregiver rejected"));
//    }
//
//    @Test
//    void testGetComplaints() throws Exception {
//        mockMvc.perform(get("/admin/complaints")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    void testGetPayments() throws Exception {
//        mockMvc.perform(get("/admin/payments")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//}
