//package com.example.trustcare.controller;
//import com.example.trustcare.model.Caregiver;
//import com.example.trustcare.repository.CaregiverDAO;
//import com.example.trustcare.repository.DAOFactory;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.List;
//
//
//@Path("/caregivers")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class CaregiverResource {
//    CaregiverDAO caregiverDAO= DAOFactory.getCaregiverDAO();
//    //get caretaker, view and search
//    //verify caretakers
//    //remove caretakers
//@GET
//    public Response getCaregivers() {
//
//
//
//    return Response;
//}
//@GET
//    @Path("/{id}")
//    public Response getCaregiver(@PathParam("id") int id) {
//    return ;
//
//}
////Edit profile, update certifications/skills
//    @PUT
//    @Path("/{id}")
//    public Response editCaregiver(@PathParam("id") int id) {
//    return;
//    }
//
//    //Get booked days
//    @GET
//    @Path("/{id}/{availability}")
//    public Response getBookedDays(@PathParam("id") int id, @PathParam("availability") String availability) {
//    return;
//
//    }
//    //set booked days
//    @PUT
//    @Path("/{id}/{availability}")
//    public Response editBookedDays(@PathParam("id") int id, @PathParam("availability") String availability) {
//
//
//    return;
//    }
//
//    //View incoming booking requests
//    @GET
//    @Path("/{id}/requests")
//    public Response getAllRequests(@PathParam("id") int id) {
//
//
//    }
//
//    //Accept booking
//    @PUT
//
//    @Path("/{id}/{requestId}/{bookingId}")
//    public  Response acceptRequest(@PathParam("id") int id, @PathParam("requestId") int requestId, @PathParam("bookingId") int bookingId) {
//
//
//    }
//    //reject booking
//    @PUT
//    @Path("/{id}/{requestId}/{bookingId}")
//    public  Response rejectRequest(@PathParam("id") int id, @PathParam("requestId") int requestId, @PathParam("bookingId") int bookingId) {
//
//    }
//    //View session history
//    @GET
//    @Path("/{id}/sessions")
//    public Response getAllSessions(@PathParam("id") int id) {
//
//    }
//    //Mark session start
//    @POST
//    @Path("/{id}/sessions/{sessionId}/check-in")
//    public Response checkIn(@PathParam("id") int id, @PathParam("sessionId") int sessionId) {
//
//    }
//
//    @POST
//    @Path("/{id}/sessions/{sessionId}/check-out")
//    public Response checkOut(@PathParam("id") int id, @PathParam("sessionId") int sessionId) {
//
//    }
//
//    //Upload CNIC/police report
//    @POST
//    @Path("/{id}/verification")
//    public Response verify(@PathParam("id") int id) {
//    }
//}