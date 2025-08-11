package com.example.trustcare.repository;
import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.TrustCareApplication;
import com.example.trustcare.config.DatabaseConnectorService;
import com.example.trustcare.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
    //queries
     static String GET_USER_BY_ID="select * from User where userID=?";
     StringBuilder  updateUserQuery=new StringBuilder("UPDATE User SET ");
     static String VIEW_ALL_BOOKINGS_FOR_USER="select * from Booking where userID=?";
     static String CREATE_BOOKING="insert into booking (userId, caregiverId, startTime, endTime, status, totalAmount) values (?,?,?,?,?,?)";
     static String SUBMIT_COMPLAINT="insert into complaint (bookingId, submittedBy, description,createdAt ) values (?,?,?,?)";
     static String VIEW_BOOKING_BY_ID="select * from Booking where bookingID=?";
     static String VIEW_PAYMENT="select * from Payments where userID=?";
     static String SUBMIT_REVIEW="insert into review(bookingId, Rating, Comment, createdAt) values (?,?,?,?)";
    //View user profile
    public User viewUserProfile(int id) {
        User user = null;


        try (
                Connection connection = DatabaseConnectorService.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("UserID"));
                    user.setFullName(resultSet.getString("FullName"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setContact(resultSet.getString("PhoneNumber"));
                    user.setAddress(resultSet.getString("Address"));
                    user.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.error(e.getMessage()));
        }

        return user;
    }



    //Edit user profile
    public void editUserProfile(int id,User user) {
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if(user.getContact()!=null){
            updates.add("Contact = ?");
            params.add(user.getContact());
        }
        if(user.getEmail()!=null){
            updates.add("Email = ?");
            params.add(user.getEmail());
        }
        if(user.getPassword()!=null){
            updates.add("Password = ?");
            params.add(user.getPassword());
        }
        if(user.getFullName()!=null){
            updates.add("FullName = ?");
            params.add(user.getFullName());
        }
        if(user.getCreatedAt()!=null){
            updates.add("CreatedAt = ?");
            params.add(user.getCreatedAt());
        }
        if(user.getAddress()!=null){
            updates.add("Address = ?");
            params.add(user.getAddress());
        }
       if(updates.size()==0){
           logger.info(LogUtils.info("Nothing to update: " + user.getFullName()));
           return;
       }
        if(updates.size()==0){
            logger.info(LogUtils.info("Nothing to update: " + user.getFullName()));
            return;
        }
        updateUserQuery.append(String.join(", ", updates));
        updateUserQuery.append(" WHERE CaregiverID = ?");
        params.add(id);
        try(Connection connection = DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(updateUserQuery));
        ) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            statement.executeUpdate();
            logger.info(LogUtils.info("Updated: " + user.getFullName()));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }
    //View all bookings (upcoming, past, canceled)
    public List<Booking> viewAllBookings(int userId){

        List<Booking> bookings = new ArrayList<>();

        try(Connection connection=DatabaseConnectorService.getConnection();
                    PreparedStatement statement = connection.prepareStatement(String.valueOf(VIEW_ALL_BOOKINGS_FOR_USER))){
                        ResultSet resultSet = statement.executeQuery();
                        while(resultSet.next()){
                            Booking booking = new Booking();
                            booking.setBookingId(resultSet.getInt("bookingID"));
                            booking.setUserId(userId);
                            booking.setEndTime(resultSet.getTimestamp("endTime"));
                            booking.setStartTime(resultSet.getTimestamp("startTime"));
                            booking.setStatus(resultSet.getString("status"));
                            booking.setCaregiverId(resultSet.getInt("caregiverID"));
                            booking.setTotalAmount(resultSet.getBigDecimal("totalAmount"));
                            bookings.add(booking);
                        }
                        logger.info(LogUtils.info("Nothing to view: " + bookings.size()));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
        return bookings;
    }
    //Create a new booking
 public void createNewBooking(int caregiverId, int userId, LocalDate startDate, LocalDate endDate, String status, BigDecimal totalAmount) {
        Booking booking = new Booking();
        try(Connection connection=DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(CREATE_BOOKING))){
                statement.setInt(1, caregiverId);
                statement.setInt(2, userId);
                statement.setString(3, startDate.toString());
                statement.setString(4, endDate.toString());
                statement.setString(5, status);
                statement.setBigDecimal(6, totalAmount);
                statement.executeUpdate();
                logger.info(LogUtils.info("New booking created: " + booking));
        }
        catch (Exception e){
        logger.error(LogUtils.error(e.getMessage()));
        }
 }
    //Report caregiver misconduct(complaint)
    public void submitComplaint( int submittedBy, int bookingId,String description, LocalDateTime createdAt){

        try(Connection connection=DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(SUBMIT_COMPLAINT))){
            statement.setInt(1, bookingId);
            statement.setInt(2, submittedBy);
            statement.setString(3, description);
            statement.setString(4, createdAt.toString());
            statement.executeUpdate();
            logger.info(LogUtils.info("New booking submitted: " + bookingId));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }
    //View booking details
    public Booking viewBooking(int bookingId){
        Booking booking = new Booking();
        try(Connection connection=DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(VIEW_BOOKING_BY_ID))) {
            statement.setInt(1, bookingId);
            ResultSet resultSet = statement.executeQuery();
            booking.setBookingId(resultSet.getInt("bookingID"));
            booking.setUserId(resultSet.getInt("userId"));
            booking.setEndTime(resultSet.getTimestamp("endTime"));
            booking.setStartTime(resultSet.getTimestamp("startTime"));
            booking.setStatus(resultSet.getString("status"));
            booking.setTotalAmount(resultSet.getBigDecimal("totalAmount"));
            booking.setCaregiverId(resultSet.getInt("caregiverID"));
            logger.info(LogUtils.info("New booking submitted: " + booking));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
            return booking;
    }
    //View invoices/payment history
    public Payment viewPayment(int paymentId){
        Payment payment = new Payment();
        try(Connection connection=DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(VIEW_PAYMENT))) {
            ResultSet resultSet = statement.executeQuery();
            payment.setBookingID(resultSet.getInt("bookingID"));
            payment.setMethod(resultSet.getString("method"));
            payment.setStatus(resultSet.getString("status"));
            payment.setAmount(resultSet.getBigDecimal("amount"));
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentID(resultSet.getInt("paymentID"));
            logger.info(LogUtils.info("New payment submitted: " + payment));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
        return payment;
        }
    //Submit review after booking completion
    public void submitReview(int bookingId, int rating, String comments, LocalDateTime createdAt){
        try(Connection connection=DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(SUBMIT_REVIEW))){
            statement.setInt(1, bookingId);
            statement.setInt(2, rating);
            statement.setString(3, comments);
            statement.setString(4, createdAt.toString());
            statement.executeUpdate();
            logger.info(LogUtils.info("New review submitted: " + bookingId));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }
    //Search caregivers (with filters)
}
    //Messaging with caregiver
    //Get real-time notifications ( WebSocket/SNS)