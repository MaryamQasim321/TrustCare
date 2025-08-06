package com.example.trustcare.repository;
import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.TrustCareApplication;
import com.example.trustcare.config.DatabaseConnectorService;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CaregiverDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
    //get caretaker, view and search
    //queries
    static String GET_ALL_CAREGIVERS = "select * from Caregiver";
    static String GET_CAREGIVER_BY_ID = "select * from Caregiver where id=?";
    static String SEARCH_CAREGIVER = "select * from Caregiver where FullName=?";
    static String GET_AVAILABLE_TIMESLOTS="SELECT StartTime, EndTime FROM Booking WHERE CaregiverID = ? AND Status IN ('Pending', 'Accepted')";
    static String SET_AVAILABLE_SLOTS="";
    static String GET_INCOMING_BOOKINGS="select * from booking where caregiverID=? && status=PENDING";
    static String ACCEPT_BOOKING="";
    public List<Caregiver> getAllCareGivers() {
        List<Caregiver> caregivers = new ArrayList<>();
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_CAREGIVERS);
             ResultSet rs = statement.executeQuery();
        ) {
            while (rs.next()) {
                Caregiver caregiver = new Caregiver();
                caregiver.setCareGiverId(rs.getInt("id"));
                caregiver.setAddress(rs.getString("address"));
                caregiver.setBio(rs.getString("bio"));
                caregiver.setCNIC(rs.getString("cnic"));
                caregiver.setEmail(rs.getString("email"));
                caregiver.setContact(rs.getString("contact"));
                caregiver.setExperienceYears(rs.getInt("experience_years"));
                caregiver.setFullName(rs.getString("full_name"));
                caregiver.setMonthlyRate(rs.getInt("monthly_rate"));
                caregiver.setLocation(rs.getString("location"));
                caregiver.setPassword(rs.getString("password"));
                caregivers.add(caregiver);
            }
            logger.info(LogUtils.info("caregivers found"));

        } catch (SQLException e) {
            logger.error(LogUtils.error(e.getMessage()));
            throw new RuntimeException(e);

        }
        return caregivers;
    }

    public Caregiver getCaregiverById(int id) {
        Caregiver caregiver = new Caregiver();
        try (Connection conn = DatabaseConnectorService.getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_CAREGIVER_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    caregiver.setCareGiverId(id);
                    caregiver.setAddress(rs.getString("address"));
                    caregiver.setBio(rs.getString("bio"));
                    caregiver.setCNIC(rs.getString("cnic"));
                    caregiver.setContact(rs.getString("contact"));
                    caregiver.setEmail(rs.getString("email"));
                    caregiver.setExperienceYears(rs.getInt("experience_years"));
                    caregiver.setFullName(rs.getString("full_name"));
                    caregiver.setMonthlyRate(rs.getInt("monthly_rate"));
                    caregiver.setLocation(rs.getString("location"));
                    caregiver.setPassword(rs.getString("password"));
                }
            } catch (SQLException e) {
                logger.error(LogUtils.error(e.getMessage()));
                throw new RuntimeException(e);
            }


            return caregiver;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Caregiver searchCaregiverByName(String name) {
        Caregiver caregiver = new Caregiver();
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_CAREGIVER);) {
            statement.setString(1, name);


            try (
                    ResultSet rs = statement.executeQuery();
            ) {
                caregiver.setCareGiverId(rs.getInt("id"));
                caregiver.setAddress(rs.getString("address"));
                caregiver.setBio(rs.getString("bio"));
                caregiver.setCNIC(rs.getString("cnic"));
                caregiver.setContact(rs.getString("contact"));
                caregiver.setEmail(rs.getString("email"));
                caregiver.setExperienceYears(rs.getInt("experience_years"));
                caregiver.setFullName(rs.getString("full_name"));
                caregiver.setMonthlyRate(rs.getInt("monthly_rate"));
                caregiver.setLocation(rs.getString("location"));
                caregiver.setPassword(rs.getString("password"));
            } catch (SQLException e) {
                logger.error(LogUtils.error(e.getMessage()));
                throw new RuntimeException(e);
            }
            return caregiver;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Edit profile, update certifications/skills

    //Get booked dates
    public List<LocalDate> getBookedDays(int caregiverId) {
        List<LocalDate> bookedDays = new ArrayList<>();
        try(Connection connection = DatabaseConnectorService.getConnection();
        PreparedStatement statement=connection.prepareStatement(GET_AVAILABLE_TIMESLOTS)
        ){
            statement.setInt(1, caregiverId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LocalDate start = rs.getTimestamp("StartTime").toLocalDateTime().toLocalDate();
                LocalDate end = rs.getTimestamp("EndTime").toLocalDateTime().toLocalDate();

                while (!start.isAfter(end)) {
                    bookedDays.add(start);
                    start = start.plusDays(1);
                }
            }
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
        return bookedDays;
    }

    //set available time slots
    public void getBookedDays(int caregiverId, int startDay, int endDay) {


    }

    //View incoming booking requests
    //Accept booking request
    //View incoming booking requests
    public List<Booking>  getIncomingBookings(int caregiverId) {
        List<Booking> bookings = new ArrayList<>();

        try(Connection connection = DatabaseConnectorService.getConnection();
            PreparedStatement statement=connection.prepareStatement(GET_INCOMING_BOOKINGS);
        ){
            statement.setInt(1, caregiverId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCaregiverId(rs.getInt("caregiver_id"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setStatus(rs.getString("status"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                bookings.add(booking);
            }
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
        return bookings;
    }

    //Accept booking


}