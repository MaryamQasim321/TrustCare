//package com.example.trustcare.controller;
//import com.example.trustcare.Logging.LogUtils;
//import com.example.trustcare.TrustCareApplication;
//import com.example.trustcare.model.Booking;
//import com.example.trustcare.model.Caregiver;
//import com.example.trustcare.model.Complaint;
//import com.example.trustcare.repository.CaregiverDAO;
//import com.example.trustcare.repository.DAOFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//@Path("/caregivers")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class CaregiverResource {
//    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
//    CaregiverDAO caregiverDAO = DAOFactory.getCaregiverDAO();
//    @GET
//    public Response getCaregivers() {
//        System.out.println("caregiver called");
//        try {
//            List<Caregiver> caregivers = caregiverDAO.getAllCareGivers();
//            logger.info(LogUtils.success("All caregivers retrieved"));
//            return Response.ok(caregivers).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error(caregivers can't be retreived").build();
//        }
//    }
//    @GET
//    @Path("/{id}")
//    public Response getCaregiverById(@PathParam("id") int id) {
//        try {
//            Caregiver caregiver = caregiverDAO.getCaregiverById(id);
//            logger.info(LogUtils.success("All caregivers retrieved"));
//            return Response.ok(caregiver).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error(caregiver can't be retreived").build();
//        }
//    }
//    @PUT
//    @Path("/{caregiverId}/edit")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response editCaregiverProfile(@PathParam("caregiverId") int caregiverId, Caregiver caregiver) {
//        try {
//            caregiverDAO.editCaregiverProfile(caregiverId, caregiver);
//            return Response.ok("Profile updated successfully.").build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Failed to update profile: " + e.getMessage()).build();
//        }
//    }
//    //View incoming booking requests
//    @GET
//    @Path("/{id}/requests")
//    public Response getAllRequests(@PathParam("id") int id) {
//        try {
//            List<Booking> bookings = caregiverDAO.getIncomingBookings(id);
//            logger.info(LogUtils.success("All caregivers retrieved"));
//            return Response.ok(bookings).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error(bookings can't be retreived").build();
//        }
//    }
//    //Accept booking
//    @PUT
//    @Path("/{id}/{bookingId}/accept")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response acceptRequest(@PathParam("id") int id, @PathParam("bookingId") int bookingId) {
//        try {
//            caregiverDAO.acceptBooking(bookingId);
//            logger.info(LogUtils.success("Caregiver " + id + " accepted booking " + bookingId));
//            return Response.ok("Booking accepted successfully.").build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error. Could not accept booking").build();
//        }
//    }
//    //reject booking
//    @PUT
//    @Path("/{id}/{requestId}/{bookingId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response rejectRequest(@PathParam("id") int id, @PathParam("requestId") int requestId, @PathParam("bookingId") int bookingId) {
//
//        try {
//            caregiverDAO.rejectBooking(bookingId);
//            logger.info(LogUtils.success("Caregiver " + id + " accepted booking " + bookingId));
//            return Response.ok("Booking rejected successfully.").build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error. Could not reject booking").build();
//        }
//    }
//    @GET
//    @Path("{id}/complaints")
//    public Response getAllComplaints(@PathParam("id") int id) {
//        try {
//            List<Complaint> complaints = new ArrayList<>();
//            logger.info(LogUtils.success("All complaints retrieved"));
//            return Response.ok(complaints).build();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Response.serverError().entity("Error in fetching complaints from database.").build();
//        }
//    }
//    //get booked days
//    @GET
//    @Path("/{id}/availability")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getBookedDays(
//            @PathParam("id") int caregiverId,
//            @QueryParam("startDate") String startDateStr,
//            @QueryParam("endDate") String endDateStr) {
//        try {
//            LocalDate startDate = LocalDate.parse(startDateStr);
//            LocalDate endDate = LocalDate.parse(endDateStr);
//            List<LocalDate> availableDays = caregiverDAO.getAvailableDays(caregiverId, startDate, endDate);
//            logger.info(LogUtils.success("Available days retrieved"));
//            return Response.ok(availableDays).build();
//        } catch (Exception e) {
//            logger.error(LogUtils.error("Error in getBookedDays: " + e.getMessage()));
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Invalid dates or server error").build();
//        }
//    }
//    //set booked days
//    @PUT
//    @Path("/{id}/availability")
//    public Response editBookedDays(@PathParam("id") int id, @QueryParam("startDate") String startDateStr, @QueryParam("endDate") String endDateStr) {
//        try {
//            LocalDate startDate = LocalDate.parse(startDateStr);
//            LocalDate endDate = LocalDate.parse(endDateStr);
//            List<LocalDate> availableDays = caregiverDAO.getAvailableDays(id, startDate, endDate);
//            logger.info(LogUtils.success("Available days retrieved"));
//            return Response.ok(availableDays).build();
//        } catch (Exception e) {
//            logger.error(LogUtils.error("Error in editBookedDays: " + e.getMessage()));
//            return Response.serverError().entity("Database error. Availability cannot be changed.").build();
//        }
//    }
//}