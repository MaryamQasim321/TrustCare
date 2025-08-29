package com.example.trustcare.repository;

import com.example.trustcare.logging.LogUtils;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
//import com.example.trustcare.service.SqsProducerService;
import com.example.trustcare.model.User;
import com.example.trustcare.service.SnsPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

@Repository
public class CaregiverDAO {

    private static final Logger logger = LoggerFactory.getLogger(CaregiverDAO.class);
    private final JdbcTemplate jdbcTemplate;
    private final SnsPublisherService snsPublisherService;
    private final UserDAO userDAO;

    public CaregiverDAO(JdbcTemplate jdbcTemplate, SnsPublisherService snsPublisherService,@Lazy UserDAO userDAO) {
        this.snsPublisherService=snsPublisherService;
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }


    private static final String GET_ALL_CAREGIVERS = "SELECT * FROM Caregiver";
    private static final String GET_CAREGIVER_BY_ID = "SELECT * FROM Caregiver WHERE CaregiverID = ?";
    private static final String SEARCH_CAREGIVER = "SELECT * FROM Caregiver WHERE FullName = ?";
    private static final String GET_AVAILABLE_DAYS =
            "SELECT StartDate, EndDate FROM UnavailableSlot WHERE CaregiverID = ? " +
                    "AND (StartDate <= ? AND EndDate >= ?)";
    private static final String SET_AVAILABLE_SLOTS =
            "INSERT INTO UnavailableSlot (CaregiverID, StartDate, EndDate, Reason) VALUES (?, ?, ?, ?)";
    private static final String GET_INCOMING_BOOKINGS =
            "SELECT * FROM Booking WHERE CaregiverID = ? AND Status = 'PENDING'";
    private static final String ACCEPT_BOOKING =
            "UPDATE Booking SET Status = 'ACCEPTED' WHERE BookingID = ?";
    private static final String REJECT_BOOKING =
            "UPDATE Booking SET Status = 'REJECTED' WHERE BookingID = ?";
    private static final String VIEW_COMPLAINTS_FOR_CAREGIVER =
            "SELECT c.* FROM Complaint c JOIN Booking b ON b.BookingID = c.BookingID WHERE b.CaregiverID = ?";
    private static final String GET_CAREGIVER_BY_EMAIL = "SELECT * FROM caregiver WHERE email = ?";
    private static final String SAVE_CNIC_URL =
            "INSERT INTO CNICUploads (CaregiverID, CNICUrl) VALUES (?, ?)";
    private static final String SAVE_CAREGIVER="insert into caregiver(email, password) values (?, ?)";
    private static final String GET_BOOKING_BY_ID="select * from booking where bookingID = ?";


    public void saveCaregiver(Caregiver caregiver) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection->{
                        PreparedStatement statement=connection.prepareStatement(
                                SAVE_CAREGIVER, Statement.RETURN_GENERATED_KEYS
                        );
                        statement.setString(1, caregiver.getEmail());
                        statement.setString(2, caregiver.getPassword());
                        return statement;
                    },keyHolder

            );
            int userID=keyHolder.getKey().intValue();

            logger.info(LogUtils.info("user saved with email: " + caregiver.getEmail()));
        } catch (Exception e) {
            logger.error(LogUtils.error("Error adding user: " + e.getMessage()));
        }
    }


    public Booking getBookingById(int bookingId) {
        try{
            return jdbcTemplate.queryForObject(GET_BOOKING_BY_ID, new BeanPropertyRowMapper<>(Booking.class), bookingId);

        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public Caregiver getCaregiverByEmail(String email) {
        try{
            return jdbcTemplate.queryForObject(GET_CAREGIVER_BY_EMAIL, new BeanPropertyRowMapper<>(Caregiver.class), email);

        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }


    public List<Caregiver> getAllCareGivers() {
        List<Caregiver> caregivers = jdbcTemplate.query(
                GET_ALL_CAREGIVERS,
                new BeanPropertyRowMapper<>(Caregiver.class)
        );
        logger.info(LogUtils.info("Caregivers found: " + caregivers.size()));
        return caregivers;
    }



    public Caregiver getCaregiverById(int id) {
        return jdbcTemplate.queryForObject(
                GET_CAREGIVER_BY_ID,
                new BeanPropertyRowMapper<>(Caregiver.class),
                id
        );
    }


    public Caregiver searchCaregiverByName(String name) {
        return jdbcTemplate.queryForObject(
                SEARCH_CAREGIVER,
                new BeanPropertyRowMapper<>(Caregiver.class),
                name
        );
    }




    public List<LocalDate> getAvailableDays(int caregiverId, LocalDate fromDate, LocalDate toDate) {
        Set<LocalDate> allDays = new LinkedHashSet<>();
        Set<LocalDate> unavailableDays = new HashSet<>();
        LocalDate current = fromDate;
        while (!current.isAfter(toDate)) {
            allDays.add(current);
            current = current.plusDays(1);
        }
        jdbcTemplate.query(GET_AVAILABLE_DAYS, rs -> {
            LocalDate start = rs.getDate("StartDate").toLocalDate();
            LocalDate end = rs.getDate("EndDate").toLocalDate();
            while (!start.isAfter(end)) {
                unavailableDays.add(start);
                start = start.plusDays(1);
            }
        }, caregiverId, Date.valueOf(toDate), Date.valueOf(fromDate));
        allDays.removeAll(unavailableDays);
        return new ArrayList<>(allDays);
    }






    public void setAvailableSlots(int caregiverId, LocalDate startDay, LocalDate endDay, String reason) {
        jdbcTemplate.update(SET_AVAILABLE_SLOTS, caregiverId, Date.valueOf(startDay), Date.valueOf(endDay), reason);
        logger.info(LogUtils.info("unavailable slot saved for caregiver " + caregiverId));
    }




    public List<Booking> getIncomingBookings(int caregiverId) {
        return jdbcTemplate.query(GET_INCOMING_BOOKINGS,
                new BeanPropertyRowMapper<>(Booking.class),
                caregiverId);
    }




    public void acceptBooking(int bookingId) {
        jdbcTemplate.update(ACCEPT_BOOKING, bookingId);
        logger.info(LogUtils.info("Booiking accepted"));
        Booking booking=getBookingById(bookingId);
        int caregiverId=booking.getCaregiverId();
        int userId=booking.getUserId();
        User user=userDAO.viewUserProfile(userId);
        snsPublisherService.publishEmailNotification(
                "Booking Accepted",
                "Your booking with caregiver ID " + caregiverId + " has been accepted.",
                List.of(user.getEmail())
        );
        logger.info(LogUtils.info("Booking accepted  " + userId + ", BookingID: " + bookingId));
    }
    public void rejectBooking(int bookingId) {
            jdbcTemplate.update(REJECT_BOOKING, bookingId);
        logger.info(LogUtils.info("Booiking rejected"));
        Booking booking=getBookingById(bookingId);
        int caregiverId=booking.getCaregiverId();
        int userId=booking.getUserId();
        User user=userDAO.viewUserProfile(userId);
        snsPublisherService.publishEmailNotification(
                "Booking rejected",
                "Your booking with caregiver ID " + caregiverId + " has been rejected.",
                List.of(user.getEmail())
        );
    }

    public List<Complaint> viewComplaints(int caregiverId) {
        return jdbcTemplate.query(VIEW_COMPLAINTS_FOR_CAREGIVER,
                new BeanPropertyRowMapper<>(Complaint.class),
                caregiverId);
    }

    public void editCaregiverProfile(int caregiverId, Caregiver caregiver) {
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (caregiver.getFullName() != null) { updates.add("FullName = ?"); params.add(caregiver.getFullName()); }
        if (caregiver.getEmail() != null) { updates.add("Email = ?"); params.add(caregiver.getEmail()); }
        if (caregiver.getAddress() != null) { updates.add("Address = ?"); params.add(caregiver.getAddress()); }
        if (caregiver.getBio() != null) { updates.add("Bio = ?"); params.add(caregiver.getBio()); }
        if (caregiver.getPhoneNumber() != null) { updates.add("Contact = ?"); params.add(caregiver.getPhoneNumber()); }
        if (caregiver.getLocation() != null) { updates.add("Location = ?"); params.add(caregiver.getLocation()); }
        if (caregiver.getExperienceYears() > 0) { updates.add("ExperienceYears = ?"); params.add(caregiver.getExperienceYears()); }
        if (caregiver.getMonthlyRate() !=null) { updates.add("MonthlyRate = ?"); params.add(caregiver.getMonthlyRate()); }
        if (caregiver.getPassword() != null) { updates.add("Password = ?"); params.add(caregiver.getPassword()); }

        if (updates.isEmpty()) {
            logger.info(LogUtils.info("Nothing to update for caregiver: " + caregiverId));
            return;
        }

        String sql = "UPDATE Caregiver SET " + String.join(", ", updates) + " WHERE CaregiverID = ?";
        params.add(caregiverId);

        jdbcTemplate.update(sql, params.toArray());
        logger.info(LogUtils.info("Caregiver profile updated: " + caregiverId));
    }

}
