//package com.example.trustcare.repository;
//import com.example.trustcare.Logging.LogUtils;
//import com.example.trustcare.TrustCareApplication;
//import com.example.trustcare.config.DatabaseConnectorService;
//import com.example.trustcare.model.Caregiver;
//import com.example.trustcare.model.Complaint;
//import com.example.trustcare.model.Payment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AdminDAO {
//    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
//    static String GET_UNVERIFIED_CAREGIVERS="select * from Caregiver where isverified=false";
//    static String APPROVE_CAREGIVER="set isverified=true where caregiverid=?";
//    static String REJECT_CAREGIVER="set isverified=false where caregiverid=?";
//    static String VIEW_COMPLAINTS="select complaint.*, booking.caregiverid from complaint join on complaint.bookingid=booking.bookingid ";
//    static String VIEW_PAYMENTS="select * from payments";
//    static String ADD_CAREGIVER="insert into caregiver (   CaregiverID,fullName,  Email,CNIC, ExperienceYears, MonthlyRate, Location, Address,PhoneNumber, Bio, isVerified, createdAt, password )  values (?,?,?,?,?,?,?,?,?,?,?,?, ?)";
//    public List<Caregiver>  getUnverifiedCaregivers() {
//        List<Caregiver> caregivers = new ArrayList<>();
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(GET_UNVERIFIED_CAREGIVERS);
//             ResultSet rs = statement.executeQuery();
//        ) {
//            while (rs.next()) {
//                Caregiver caregiver = new Caregiver();
//                caregiver.setCareGiverId(rs.getInt("caregiverid"));
//                caregiver.setAddress(rs.getString("address"));
//                caregiver.setBio(rs.getString("bio"));
//                caregiver.setCNIC(rs.getString("cnic"));
//                caregiver.setEmail(rs.getString("email"));
//                caregiver.setContact(rs.getString("PhoneNumber"));
//                caregiver.setExperienceYears(rs.getInt("experienceYears"));
//                caregiver.setFullName(rs.getString("fullname"));
//                caregiver.setMonthlyRate(rs.getInt("monthlyrate"));
//                caregiver.setLocation(rs.getString("location"));
//                caregiver.setPassword(rs.getString("password"));
//                caregiver.setIsVerified(rs.getString("isverified"));
//
//                caregivers.add(caregiver);
//            }
//            logger.info(LogUtils.info("caregivers found"));
//
//        } catch (SQLException e) {
//            logger.error(LogUtils.error(e.getMessage()));
//            throw new RuntimeException(e);
//
//        }
//        return caregivers;
//    }
//    public void approveCaregiver(int caregiverId) {
//
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(APPROVE_CAREGIVER);
//        ) {
//            statement.setInt(1, caregiverId);
//            statement.executeUpdate();
//            logger.info(LogUtils.info("caregiver approved"));
//
//        }
//        catch (SQLException e) {
//            logger.error(LogUtils.error(e.getMessage()));
//        }
//
//    }
//    public void rejectCaregiver(int caregiverId) {
//
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(REJECT_CAREGIVER);
//        ) {
//            statement.setInt(1, caregiverId);
//            statement.executeUpdate();
//            logger.info(LogUtils.info("caregiver approved"));
//
//        }
//        catch (SQLException e) {
//            logger.error(LogUtils.error(e.getMessage()));
//        }
//
//    }
//
//
////    View reported misconduct/complaint
//    public List<Complaint> getComplaints() {
//        List<Complaint> complaints = new ArrayList<>();
//
//
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(VIEW_COMPLAINTS);
//             ResultSet rs = statement.executeQuery();
//        ) {
//            while (rs.next()) {
//                Complaint complaint = new Complaint();
//                complaint.setComplaintId(rs.getInt("complaintid"));
//                complaint.setBookingId(rs.getInt("bookingid"));
//                complaint.setCreatedAt(rs.getTimestamp("createdat"));
//                complaint.setDescription(rs.getString("description"));
//                complaint.setSubmittedBy(rs.getInt("submittedby"));
//
//complaints.add(complaint);
//logger.info(LogUtils.info("complaints found"));
//            }
//        }
//        catch (SQLException e) {
//            logger.error(LogUtils.error(e.getMessage()));
//
//        }
//        return complaints;
//
//
//    }
//
//
//
//
////    View all payment records
//    public List<Payment> getPayments() {
//        List<Payment> payments = new ArrayList<>();
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(VIEW_PAYMENTS);
//             ResultSet rs = statement.executeQuery();
//        ) {
//            while (rs.next()) {
//                Payment payment = new Payment();
//                payment.setPaymentID(rs.getInt("paymentId"));
//                payment.setBookingID(rs.getInt("bookingid"));
//                payment.setPaymentDate(rs.getTimestamp("paymentDate").toLocalDateTime());
//                payment.setMethod(rs.getString("method"));
//                payment.setStatus(rs.getString("status"));
//                payment.setAmount(rs.getBigDecimal("amount"));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//return payments;
//    }
//    //add caregiver
//    public void addCaregiver(Caregiver caregiver) {
//        try (Connection connection = DatabaseConnectorService.getConnection();
//             PreparedStatement statement = connection.prepareStatement(ADD_CAREGIVER);
//
//        ) {
//            // CaregiverID,fullName,  Email,CNIC, ExperienceYears, MonthlyRate, Location, Address,PhoneNumber, Bio, isVerified, createdAt )
//
//            statement.setInt(1, caregiver.getCareGiverId());
//            statement.setString(2, caregiver.getFullName());
//            statement.setString(3, caregiver.getEmail());
//            statement.setString(4, caregiver.getCNIC());
//            statement.setInt(5, caregiver.getExperienceYears());
//            statement.setBigDecimal(6, BigDecimal.valueOf( caregiver.getMonthlyRate()));
//            statement.setString(7, caregiver.getAddress());
//            statement.setString(8, caregiver.getAddress());
//            statement.setString(9, caregiver.getContact());
//            statement.setString(10, caregiver.getBio());
//            statement.setString(11, caregiver.getIsVerified());
//            statement.setTimestamp(12, Timestamp.valueOf(caregiver.getCreatedAt()));
//            statement.setString(13, caregiver.getPassword());
//
//    }
//        catch (SQLException e) {
//        logger.error(LogUtils.error(e.getMessage()));
//        }
//    }
//    //remove caregiver
//
//
//
//}