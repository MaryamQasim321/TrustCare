//package com.example.trustcare.controller;
//import com.example.trustcare.Logging.LogUtils;
//import com.example.trustcare.TrustCareApplication;
//import com.example.trustcare.model.Booking;
//import com.example.trustcare.model.Payment;
//import com.example.trustcare.model.User;
//import com.example.trustcare.repository.UserDAO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeParseException;
//import java.util.List;
//
//@Path("/users")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class UserResource {
//
//    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
//    private final UserDAO userDAO = new UserDAO();
//
//    // View user profile
//    @GET
//    @Path("/{id}")
//    public Response getUserProfile(@PathParam("id") int id) {
//        try {
//            User user = userDAO.viewUserProfile(id);
//            return Response.ok(user).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // Edit user profile
//    @PUT
//    @Path("/{id}")
//    public Response updateUserProfile(@PathParam("id") int id, User user) {
//        try {
//            userDAO.editUserProfile(id, user);
//            logger.info(LogUtils.info("User profile updated successfully."));
//            return Response.ok(user).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // View all bookings
//    @GET
//    @Path("/{id}/bookings")
//    public Response viewAllBookings(@PathParam("id") int id) {
//        try {
//            List<Booking> bookings = userDAO.viewAllBookings(id);
//            logger.info(LogUtils.info("Bookings retrieved successfully."));
//            return Response.ok(bookings).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // Create a new booking
//    @POST
//    @Path("/{caregiverId}/bookings")
//    public Response createNewBooking(
//            @PathParam("caregiverId") int caregiverId,
//            @QueryParam("userId") int userId,
//            @QueryParam("startDate") String startDateStr,
//            @QueryParam("endDate") String endDateStr,
//            @QueryParam("status") String status,
//            @QueryParam("totalAmount") BigDecimal totalAmount) {
//        try {
//            LocalDate startDate = LocalDate.parse(startDateStr);
//            LocalDate endDate = LocalDate.parse(endDateStr);
//            userDAO.createNewBooking(caregiverId, userId, startDate, endDate, status, totalAmount);
//            logger.info(LogUtils.info("New booking created successfully."));
//            return Response.ok().build();
//        } catch (DateTimeParseException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format. Expected yyyy-MM-dd").build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // View booking details
//    @GET
//    @Path("/bookings/{bookingId}")
//    public Response getBookingById(@PathParam("bookingId") int bookingId) {
//        try {
//            Booking booking = userDAO.viewBooking(bookingId);
//            logger.info(LogUtils.info("Booking fetched successfully."));
//            return Response.ok(booking).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // Submit review after session
//    @POST
//    @Path("/{userId}/reviews")
//    public Response submitReview(
//            @PathParam("userId") int userId,
//            @QueryParam("bookingId") int bookingId,
//            @QueryParam("rating") int rating,
//            @QueryParam("comments") String comments,
//            @QueryParam("createdAt") String createdAtStr) {
//        try {
//            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
//            userDAO.submitReview(bookingId, rating, comments, createdAt);
//            logger.info(LogUtils.info("Review submitted successfully."));
//            return Response.ok().build();
//        } catch (DateTimeParseException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid datetime format. Expected yyyy-MM-ddTHH:mm:ss").build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // Submit complaint
//    @POST
//    @Path("/{userId}/complaints")
//    public Response submitComplaint(
//            @PathParam("userId") int userId,
//            @QueryParam("bookingId") int bookingId,
//            @QueryParam("description") String description,
//            @QueryParam("createdAt") String createdAtStr) {
//        try {
//            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
//            userDAO.submitComplaint(userId, bookingId, description, createdAt);
//            logger.info(LogUtils.info("Complaint submitted successfully."));
//            return Response.ok().build();
//        } catch (DateTimeParseException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid datetime format. Expected yyyy-MM-ddTHH:mm:ss").build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//
//    // View payment
//    @GET
//    @Path("/{userId}/payments/{paymentId}")
//    public Response viewPayment(@PathParam("userId") int userId, @PathParam("paymentId") int paymentId) {
//        try {
//            Payment payment = userDAO.viewPayment(paymentId);
//            logger.info(LogUtils.info("Payment fetched successfully."));
//            return Response.ok(payment).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }
//}
