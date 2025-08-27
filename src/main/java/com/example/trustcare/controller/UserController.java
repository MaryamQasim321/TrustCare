package com.example.trustcare.controller;

import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.dto.BookingRequest;
import com.example.trustcare.dto.ComplaintRequest;
import com.example.trustcare.dto.ReviewRequest;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.Payment;
import com.example.trustcare.model.User;
import com.example.trustcare.repository.UserDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserDAO userDAO;

    public UserController(@Lazy UserDAO userDAO) {
        this.userDAO = userDAO;
    }



    @Operation(summary = "Get user profile", description = "Fetches the user profile details for a given user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable int id) {
        try {
            User user = userDAO.viewUserProfile(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error fetching user profile", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @Operation(summary = "Update user profile", description = "Allows a user to update their own profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden – user is not authorized to update this profile",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable int id, @RequestBody User user) {
        try {
            userDAO.editUserProfile(id, user);
            logger.info(LogUtils.info("User profile updated successfully."));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error updating user profile", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @Operation(summary = "View all bookings for a user",
            description = "Returns a list of all bookings associated with the given user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden – user is not authorized to view these bookings",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/bookings")
    public ResponseEntity<?> viewAllBookings(@PathVariable int id) {
        try {
            List<Booking> bookings = userDAO.viewAllBookings(id);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            logger.error("Error fetching bookings", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new booking",
            description = "Allows a user to create a new booking with a caregiver by providing booking details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., wrong date format)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden – user is not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })

    @PostMapping("/{userId}/bookings")
    public ResponseEntity<?> createNewBooking(
            @PathVariable int userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking details including caregiver ID, start/end dates, status, and total amount",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingRequest.class))
            )
            @Valid @RequestBody BookingRequest bookingData) {
        try {
            int caregiverId = bookingData.getCaregiverId();
            String startDate = bookingData.getStartDate();
            String endDate = bookingData.getEndDate();
            String status = bookingData.getStatus();
            BigDecimal totalAmount = bookingData.getTotalAmount();
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            userDAO.createNewBooking(userId, caregiverId, start, end, status, totalAmount);

            return ResponseEntity.ok("Booking created successfully");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
        } catch (Exception e) {
            logger.error("Error creating booking", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get booking by ID",
            description = "Fetches the booking details for a specific booking ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden – user is not authorized to view this booking",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable int bookingId) {
        try {
            Booking booking = userDAO.viewBooking(bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            logger.error("Error fetching booking", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Submit a review",
            description = "Allows a user to submit a review for a booking, including rating, comments, and timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review submitted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., wrong datetime format)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden – user not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userId}/reviews")
    public ResponseEntity<?> submitReview(
            @PathVariable int userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Review details including bookingId, rating, comments, and createdAt timestamp",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReviewRequest.class))
            )
            @Valid @RequestBody ReviewRequest reviewData) {
        try {
            int bookingId = reviewData.getBookingId();
            int rating = reviewData.getRating();
            String comments = reviewData.getComments();
            LocalDateTime created = LocalDateTime.parse(reviewData.getCreatedAt());

            userDAO.submitReview(bookingId, rating, comments, created);

            return ResponseEntity.ok("Review submitted successfully");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid datetime format. Use yyyy-MM-ddTHH:mm:ss");
        } catch (Exception e) {
            logger.error("Error submitting review", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Submit a complaint",
            description = "Allows a user to submit a complaint related to a booking.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complaint submitted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., wrong datetime format)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden – user not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userId}/complaints")
    public ResponseEntity<?> submitComplaint(
            @PathVariable int userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Complaint details including bookingId, description, and createdAt timestamp",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ComplaintRequest.class))
            )
            @Valid @RequestBody ComplaintRequest complaintData) {
        try {
            int bookingId = complaintData.getBookingId();
            String description = complaintData.getDescription();
            LocalDateTime createdAt = LocalDateTime.parse(complaintData.getCreatedAt());

            userDAO.submitComplaint(bookingId, userId, description, createdAt);

            return ResponseEntity.ok("Complaint submitted successfully");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid datetime format. Use yyyy-MM-ddTHH:mm:ss");
        } catch (Exception e) {
            logger.error("Error submitting complaint", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @Operation(summary = "View payment by ID",
            description = "Fetches the payment details for a specific payment ID associated with the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden – user is not authorized to view this payment",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}/payments/{paymentId}")
    public ResponseEntity<?> viewPayment(@PathVariable int userId, @PathVariable int paymentId) {
        try {
            Payment payment = userDAO.viewPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error fetching payment", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
