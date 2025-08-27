package com.example.trustcare.controller;

import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.TrustCareApplication;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.Payment;
import com.example.trustcare.repository.AdminDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);

    private final AdminDAO adminDAO = new AdminDAO();

    // ✅ View all unverified caregivers
    @GET
    @Path("/caregiver-verifications")
    public Response getUnverifiedCaregivers() {
        try {
            List<Caregiver> caregivers = adminDAO.getUnverifiedCaregivers();
            logger.info(LogUtils.info("Retrieved all unverified caregivers"));
            return Response.ok(caregivers).build();
        } catch (Exception e) {
            logger.error(LogUtils.error("Failed to get unverified caregivers: " + e.getMessage()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving unverified caregivers").build();
        }
    }

    // ✅ Approve caregiver verification
    @PUT
    @Path("/caregiver-verifications/{caregiverId}/approve")
    public Response approveCaregiver(@PathParam("caregiverId") int caregiverId) {
        try {
            boolean updated = adminDAO.approveCaregiver(caregiverId);
            if (updated) {
                logger.info(LogUtils.info("Approved caregiver ID: " + caregiverId));
                return Response.ok().entity("Caregiver approved").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Caregiver not found").build();
            }
        } catch (Exception e) {
            logger.error(LogUtils.error("approveCaregiver failed: " + e.getMessage()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error approving caregiver").build();
        }
    }

    // ✅ Reject caregiver verification
    @PUT
    @Path("/caregiver-verifications/{caregiverId}/reject")
    public Response rejectCaregiver(@PathParam("caregiverId") int caregiverId) {
        try {
            boolean updated = adminDAO.rejectCaregiver(caregiverId);
            if (updated) {
                logger.info(LogUtils.info("Rejected caregiver ID: " + caregiverId));
                return Response.ok().entity("Caregiver rejected").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Caregiver not found").build();
            }
        } catch (Exception e) {
            logger.error(LogUtils.error("rejectCaregiver failed: " + e.getMessage()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error rejecting caregiver").build();
        }
    }

    // ✅ Get all complaints (misconduct reports)
    @GET
    @Path("/complaints")
    public Response getComplaints() {
        try {
            List<Complaint> complaints = adminDAO.getComplaints();
            logger.info(LogUtils.info("Retrieved all complaints"));
            return Response.ok(complaints).build();
        } catch (Exception e) {
            logger.error(LogUtils.error("getComplaints failed: " + e.getMessage()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving complaints").build();
        }
    }

    // ✅ Get all payment records
    @GET
    @Path("/payments")
    public Response getPayments() {
        try {
            List<Payment> payments = adminDAO.getPayments();
            logger.info(LogUtils.info("Retrieved all payments"));
            return Response.ok(payments).build();
        } catch (Exception e) {
            logger.error(LogUtils.error("getPayments failed: " + e.getMessage()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving payments").build();
        }
    }
}
