package com.example.trustcare.repository;

import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.model.Admin;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.Payment;
import com.example.trustcare.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class AdminDAO {

    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);

    private final JdbcTemplate jdbcTemplate;

    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_UNVERIFIED_CAREGIVERS ="select * from caregiver where isverified=false";
    private static final String APPROVE_CAREGIVER = "update caregiver set isverified=true where caregiverid=?";
    private static final String REJECT_CAREGIVER = "update caregiver set isverified=false where caregiverid=?";
    private static final String VIEW_COMPLAINTS ="select c.*, b.caregiverid from complaint c join booking b ON c.bookingid = b.bookingid";
    private static final String VIEW_PAYMENTS ="select * from payments";
    private static final String ADD_CAREGIVER ="insert into caregiver (CaregiverID, fullName, Email, CNIC, ExperienceYears, MonthlyRate, Location, Address, PhoneNumber, Bio, isVerified, createdAt, password) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ADMIN_BY_EMAIL ="select * from admin where email=?";
    private static final String SAVE_ADMIN="insert into admin (email, password) values (?, ?)";
    private static final String GET_ADMIN_BY_ID="select * from admin where adminid=?";
    private static final String GET_AL_ADMINS="select * from admin";
    public void saveAdmin(Admin admin) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection->{
                        PreparedStatement statement=connection.prepareStatement(
                                SAVE_ADMIN, Statement.RETURN_GENERATED_KEYS
                        );
                        statement.setString(1, admin.getEmail());
                        statement.setString(2, admin.getPassword());
                        return statement;
                    },keyHolder

            );
            int userID=keyHolder.getKey().intValue();


            logger.info(LogUtils.info("user saved with email: " + admin.getEmail()));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error adding user: " + e.getMessage()));
        }

    }


    public Admin getAdminById(int id) {


        Admin admin=jdbcTemplate.queryForObject(GET_ADMIN_BY_ID, new BeanPropertyRowMapper<>(Admin.class), id);

        return admin;
    }


    public Admin getAdminByEmail(String email) {


        Admin admin=jdbcTemplate.queryForObject(GET_ADMIN_BY_EMAIL, new BeanPropertyRowMapper<>(Admin.class), email);

       return admin;
    }




    public List<Admin> getAllAdmins() {
        try{
            List<Admin> admins=jdbcTemplate.query(GET_AL_ADMINS, new BeanPropertyRowMapper<>(Admin.class));
       return admins;
        }
        catch (Exception e) {
            logger.error(LogUtils.error("Error fetching admins " + e.getMessage()));
            throw e;
        }


    }
    public List<Caregiver> getUnverifiedCaregivers() {
        try {
            List<Caregiver> caregivers = jdbcTemplate.query(
                    GET_UNVERIFIED_CAREGIVERS,
                    new BeanPropertyRowMapper<>(Caregiver.class)
            );
            logger.info(LogUtils.info("Unverified caregivers found: " + caregivers.size()));
            return caregivers;
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching unverified caregivers: " + e.getMessage()));
            throw e;
        }
    }

    public void approveCaregiver(int caregiverId) {
        try {
            jdbcTemplate.update(APPROVE_CAREGIVER, caregiverId);
            logger.info(LogUtils.info("Caregiver approved with ID: " + caregiverId));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error approving caregiver: " + e.getMessage()));
        }
    }

    public void rejectCaregiver(int caregiverId) {
        try {
            jdbcTemplate.update(REJECT_CAREGIVER, caregiverId);
            logger.info(LogUtils.info("Caregiver rejected with ID: " + caregiverId));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error rejecting caregiver: " + e.getMessage()));
        }
    }

    public List<Complaint> getComplaints() {
        try {
            List<Complaint> complaints = jdbcTemplate.query(
                    VIEW_COMPLAINTS,
                    new BeanPropertyRowMapper<>(Complaint.class)
            );
            logger.info(LogUtils.info("Complaints found: " + complaints.size()));
            return complaints;
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching complaints: " + e.getMessage()));
            throw e;
        }
    }

    public List<Payment> getPayments() {
        try {
            List<Payment> payments = jdbcTemplate.query(
                    VIEW_PAYMENTS,
                    new BeanPropertyRowMapper<>(Payment.class)
            );
            logger.info(LogUtils.info("Payments found: " + payments.size()));
            return payments;
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching payments: " + e.getMessage()));
            throw e;
        }
    }

    public void addCaregiver(Caregiver caregiver) {
        try {
            jdbcTemplate.update(
                    ADD_CAREGIVER,
                    caregiver.getCareGiverId(),
                    caregiver.getFullName(),
                    caregiver.getEmail(),
                    caregiver.getCNIC(),
                    caregiver.getExperienceYears(),
                    caregiver.getMonthlyRate(),
                    caregiver.getLocation(),
                    caregiver.getAddress(),
                    caregiver.getPhoneNumber(),
                    caregiver.getBio(),
                    caregiver.isVerified(),
                    Timestamp.valueOf(caregiver.getCreatedAt()),
                    caregiver.getPassword()
            );
            logger.info(LogUtils.info("Caregiver added with ID: " + caregiver.getCareGiverId()));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error adding caregiver: " + e.getMessage()));
        }
    }
}
