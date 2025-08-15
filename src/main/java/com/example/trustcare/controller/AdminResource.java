//package com.example.trustcare.controller;
//
//import com.example.trustcare.Logging.LogUtils;
//import com.example.trustcare.TrustCareApplication;
//import com.example.trustcare.model.Caregiver;
//import com.example.trustcare.model.Complaint;
//import com.example.trustcare.model.Payment;
//import com.example.trustcare.repository.AdminDAO;
//import com.example.trustcare.repository.CaregiverDAO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.List;
//import java.util.Map;
//
//
//@Path("/admin")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//
//public class AdminResource {
//    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
//
//    AdminDAO adminDAO = new AdminDAO();
////
////    /admin/dashboard	GET	Summary: users, bookings, flagged content
////    @GET
////    @Path("/{id}/dashboard")
////    public Response getDashboard(@PathParam("id") int id) {
////
////
////
////
////
////
////    }
//
/////admin/caregiver-verifications	GET	View pending verifications
//
//    @GET
//    @Path("/caregiver-verifications")
//    public Response getUnverifiedCaregivers() {
//        try{
//            List<Caregiver> caregivers = adminDAO.getUnverifiedCaregivers();
//            logger.info(LogUtils.info("all unverfified caregivers retrieved " ));
//            return Response.ok(caregivers).build();
//        }
//        catch (Exception e){
//            logger.error(LogUtils.error("getUnverifiedCaregivers failed" ));
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }
/////admin/caregiver-verifications/{id}	POST	Approve/reject verification
//    @PUT
//    @Path("approve-caregiver/{caregiverId}")
//    public Response approveCaregiver(@PathParam("caregiverId") int caregiverId) {
//        try{
//            adminDAO.approveCaregiver(caregiverId);
//            logger.info(LogUtils.info("approved caregiver " + caregiverId));
//            return Response.ok().build();
//        }
//        catch (Exception e){
//            logger.error(LogUtils.error("approveCaregiver failed" ));
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//    ///admin/caregiver-verifications/{id}	POST	Approve/reject verification
//    @PUT
//    @Path("reject-caregiver/{caregiverId}")
//    public Response rejectCaregiver(@PathParam("caregiverId") int caregiverId) {
//        try{
//            adminDAO.rejectCaregiver(caregiverId);
//            logger.info(LogUtils.info("rejected caregiver " + caregiverId));
//            return Response.ok().build();
//        }
//        catch (Exception e){
//            logger.error(LogUtils.error("reject caregiver failed" ));
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
//
/////admin/abuse-reports	GET	View reported misconduct
//@GET
//    @Path("/complaints")
//    public Response getComplaints() {
//    try{
//        List<Complaint> complaints = adminDAO.getComplaints();
//        logger.info(LogUtils.info("get complaints retrieved " ));
//        return Response.ok(complaints).build();
//
//    }
//    catch (Exception e){
//        logger.error(LogUtils.error("getComplaints failed" ));
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//    }
//}
//
/////admin/transactions	GET	View all payment records
//    @GET
//    @Path("/payments")
//    public Response getPayments() {
//        try{
//            List<Payment> payments = adminDAO.getPayments();
//            logger.info(LogUtils.info("get payments retrieved " ));
//            return Response.ok(payments).build();
//        }
//        catch (Exception e){
//
//            logger.error(LogUtils.error("getPayments failed" ));
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
/////admin/analytics	GET	Generate platform reports (bookings, earnings)
/////admin/settings	PUT	Update platform settings
//
//
//
//
//
//}