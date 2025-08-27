package com.example.trustcare.controller;

import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.model.*;
import com.example.trustcare.repository.AdminDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminDAO adminDAO;
    public AdminController(@Lazy AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }


    @Operation(
            summary = "get all unverified caregivers",
            description = "admin can view all unverified caregivers and then can verify them",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Caregiver.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('AMDIN')")
    @GetMapping("/caregiver-verifications")
    public ResponseEntity<?> getUnverifiedCaregivers() {
        try {
            List<Caregiver> caregivers = adminDAO.getUnverifiedCaregivers();
            logger.info(LogUtils.info("Retrieved all unverified caregivers"));
            return ResponseEntity.ok(caregivers);
        } catch (Exception e) {
            logger.error(LogUtils.error("Failed to get unverified caregivers: " + e.getMessage()), e);
            return ResponseEntity.internalServerError().body("Error retrieving unverified caregivers");
        }
    }



    @Operation(
            summary = "admin can accept caregiver's verification",
            description = "admin can accept caregiver's verification",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Caregiver.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('AMDIN')")
    @PutMapping("/caregiver-verifications/{caregiverId}/approve")
    public ResponseEntity<?> approveCaregiver(@PathVariable int caregiverId) {
        try {
            adminDAO.approveCaregiver(caregiverId);

                logger.info(LogUtils.info("Approved caregiver ID: " + caregiverId));
                return ResponseEntity.ok("Caregiver approved");

        } catch (Exception e) {
            logger.error(LogUtils.error("approveCaregiver failed: " + e.getMessage()), e);
            return ResponseEntity.internalServerError().body("Error approving caregiver");
        }
    }



    @Operation(
            summary = "admin can reject caregiver",
            description = "admin can reject caregiver if his background information is not valid",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Caregiver.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('AMDIN')")
    @PutMapping("/caregiver-verifications/{caregiverId}/reject")
    public ResponseEntity<?> rejectCaregiver(@PathVariable int caregiverId) {
        try {
           adminDAO.rejectCaregiver(caregiverId);

                logger.info(LogUtils.info("Rejected caregiver ID: " + caregiverId));
                return ResponseEntity.ok("Caregiver rejected");
        } catch (Exception e) {
            logger.error(LogUtils.error("rejectCaregiver failed: " + e.getMessage()), e);
            return ResponseEntity.internalServerError().body("Error rejecting caregiver");
        }
    }

    @Operation(
            summary = "admin can view all complaints ",
            description = "admin can view all complaints submitted by users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Complaint.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('AMDIN')")
    @GetMapping("/complaints")
    public ResponseEntity<?> getComplaints() {
        try {
            List<Complaint> complaints = adminDAO.getComplaints();
            logger.info(LogUtils.info("Retrieved all complaints"));
            return ResponseEntity.ok(complaints);
        } catch (Exception e) {
            logger.error(LogUtils.error("getComplaints failed: " + e.getMessage()), e);
            return ResponseEntity.internalServerError().body("Error retrieving complaints");
        }
    }

    @Operation(
            summary = "get all payments",
            description = "admin can view all payments by this",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Payment.class)
                            )
                    )
            }
    )

    @PreAuthorize("hasRole('AMDIN')")
    @GetMapping("/payments")
    public ResponseEntity<?> getPayments() {
        try {
            List<Payment> payments = adminDAO.getPayments();
            logger.info(LogUtils.info("Retrieved all payments"));
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            logger.error(LogUtils.error("getPayments failed: " + e.getMessage()), e);
            return ResponseEntity.internalServerError().body("Error retrieving payments");
        }
    }
}