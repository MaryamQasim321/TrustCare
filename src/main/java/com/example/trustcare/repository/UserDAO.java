package com.example.trustcare.repository;

import com.example.trustcare.logging.LogUtils;
import com.example.trustcare.model.*;
//import com.example.trustcare.service.SqsProducerService;
import com.example.trustcare.service.SnsPublisherService;
import com.example.trustcare.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final JdbcTemplate jdbcTemplate;
    private final CaregiverDAO caregiverDAO;
    private final SubscriptionService  subscriptionService;
    private final SnsPublisherService snsPublisherService;


    public UserDAO(JdbcTemplate jdbcTemplate,  CaregiverDAO caregiverDAO,@Lazy SubscriptionService subscriptionService, SnsPublisherService snsPublisherService) {
        this.subscriptionService = subscriptionService;
        this.jdbcTemplate = jdbcTemplate;
        this.caregiverDAO=caregiverDAO;
        this.snsPublisherService=snsPublisherService;
    }



    private static final String GET_USER_BY_ID = "SELECT * FROM User WHERE userID = ?";
    private static final String UPDATE_USER_PREFIX = "UPDATE User SET ";
    private static final String VIEW_ALL_BOOKINGS_FOR_USER = "SELECT * FROM Booking WHERE userID = ?";
    private static final String CREATE_BOOKING = "INSERT INTO booking (userId, caregiverId, startTime, endTime, status, totalAmount) VALUES (?,?,?,?,?,?)";
    private static final String SUBMIT_COMPLAINT = "INSERT INTO complaint (bookingId, submittedBy, description, createdAt) VALUES (?,?,?,?)";
    private static final String VIEW_BOOKING_BY_ID = "SELECT * FROM Booking WHERE bookingID = ?";
    private static final String VIEW_PAYMENT = "SELECT * FROM Payments WHERE paymentID = ?";
    private static final String SUBMIT_REVIEW = "INSERT INTO review (bookingId, rating, comment, createdAt) VALUES (?,?,?,?)";
    private static final String GET_USER_BY_EMAIL=  "SELECT * FROM User WHERE email = ?";
    private static final String SAVE_USER="insert into caregiver(email, password) values (?, ?)";
    private final String GET_ALL_USERS= "SELECT * FROM User";




    public List<User> getAllUsers() {
        try{
          return   jdbcTemplate.query(GET_ALL_USERS, new BeanPropertyRowMapper<>(User.class));

        }
        catch (Exception e){
            throw e;
        }


    }
    public void saveUser(User user) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection->{
                PreparedStatement statement=connection.prepareStatement(
                        SAVE_USER,Statement.RETURN_GENERATED_KEYS
                );
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                return statement;
                    },keyHolder

            );
                int userID=keyHolder.getKey().intValue();
            logger.info(LogUtils.info("user saved with email: " + user.getEmail()));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error adding user: " + e.getMessage()));
        }
    }





    // View user profile
    public User viewUserProfile(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_USER_BY_ID, new BeanPropertyRowMapper<>(User.class), id);
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching user profile: " + e.getMessage()), e);
            return null;
        }
    }



    //get user by email
    public User getUserByEmail(String email){
        try {
            return jdbcTemplate.queryForObject(GET_USER_BY_EMAIL, new BeanPropertyRowMapper<>(User.class), email);
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching user profile: " + e.getMessage()), e);
            return null;
        }
    }




    public void editUserProfile(int id, User user) {
        StringBuilder sql = new StringBuilder(UPDATE_USER_PREFIX);
        new StringBuilder();

        if (user.getFullName() != null) sql.append(" FullName = '").append(user.getFullName()).append("',");
        if (user.getEmail() != null) sql.append(" Email = '").append(user.getEmail()).append("',");
        if (user.getPassword() != null) sql.append(" Password = '").append(user.getPassword()).append("',");
        if (user.getContact() != null) sql.append(" PhoneNumber = '").append(user.getContact()).append("',");
        if (user.getAddress() != null) sql.append(" Address = '").append(user.getAddress()).append("',");
        if (user.getCreatedAt() != null) sql.append(" CreatedAt = '").append(user.getCreatedAt()).append("',");


        sql.setLength(sql.length() - 1);

        sql.append(" WHERE userID = ").append(id);

        try {
            jdbcTemplate.update(sql.toString());
            logger.info(LogUtils.info("Updated user: " + id));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error updating user: " + e.getMessage()), e);
        }
    }



    // View all bookings
    public List<Booking> viewAllBookings(int userId) {
        try {
            return jdbcTemplate.query(VIEW_ALL_BOOKINGS_FOR_USER, new BeanPropertyRowMapper<>(Booking.class), userId);
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching bookings: " + e.getMessage()), e);
            throw e;
        }
    }



    public void createNewBooking(int userId, int caregiverId, LocalDate startDate, LocalDate endDate, String status, BigDecimal totalAmount) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(CREATE_BOOKING, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, caregiverId);
                ps.setDate(3, Date.valueOf(startDate));
                ps.setDate(4, Date.valueOf(endDate));
                ps.setString(5, status);
                ps.setBigDecimal(6, totalAmount);
                return ps;
            }, keyHolder);
            int bookingId = keyHolder.getKey().intValue();
//
//            snsPublisherService.publishEmailNotification(
//                    "Booking Accepted",
//                    "Your booking with caregiver ID " + caregiverId + " has been accepted.",
//                    List.of(user.getEmail())
//            );



        } catch (Exception e) {
            logger.error(LogUtils.error("Error creating booking: " + e.getMessage()), e);
        }
    }


    // Report caregiver misconduct (complaint)
    public void submitComplaint(int bookingId, int submittedBy, String description, LocalDateTime createdAt) {
        try {
            jdbcTemplate.update(SUBMIT_COMPLAINT, bookingId, submittedBy, description, createdAt);
            logger.info(LogUtils.info("Complaint submitted for booking: " + bookingId));
           Booking booking= caregiverDAO.getBookingById(bookingId);
           int caregiverId=booking.getCaregiverId();






        } catch (Exception e) {
            logger.error(LogUtils.error("Error submitting complaint: " + e.getMessage()), e);
        }
    }



    // View booking details
    public Booking viewBooking(int bookingId) {
        try {
            return jdbcTemplate.queryForObject(VIEW_BOOKING_BY_ID, new BeanPropertyRowMapper<>(Booking.class), bookingId);
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching booking: " + e.getMessage()), e);
            return null;
        }
    }

    // View payment
    public Payment viewPayment(int paymentId) {
        try {
            return jdbcTemplate.queryForObject(VIEW_PAYMENT, new BeanPropertyRowMapper<>(Payment.class), paymentId);
        } catch (Exception e) {
            logger.error(LogUtils.error("Error fetching payment: " + e.getMessage()), e);
            return null;
        }
    }

    // Submit review
    public void submitReview(int bookingId, int rating, String comments, LocalDateTime createdAt) {
        try {
            jdbcTemplate.update(SUBMIT_REVIEW, bookingId, rating, comments, createdAt);
            logger.info(LogUtils.info("Review submitted for booking: " + bookingId));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error submitting review: " + e.getMessage()), e);
        }



    }
}
