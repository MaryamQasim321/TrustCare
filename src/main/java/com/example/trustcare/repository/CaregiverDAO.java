package com.example.trustcare.repository;
import com.example.trustcare.Logging.LogUtils;
import com.example.trustcare.TrustCareApplication;
import com.example.trustcare.config.DatabaseConnectorService;
import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.service.S3Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
public class CaregiverDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
    //get caretaker, view and search
    //queries
    static String GET_ALL_CAREGIVERS = "select * from Caregiver";
    static String GET_CAREGIVER_BY_ID = "select * from Caregiver where id=?";
    static String SEARCH_CAREGIVER = "select * from Caregiver where FullName=?";
    static String GET_AVAILABLE_DAYS="SELECT StartDate, EndDate FROM UnavailableSlot WHERE CaregiverID = ? " +
            "AND (StartDate <= ? AND EndDate >= ?)";
    static String SET_AVAILABLE_SLOTS="insert into UnavailableSlot(caregiverID, startDate, endDate, reason) values (?, ?, ?,?)";
    static String GET_INCOMING_BOOKINGS="select * from booking where caregiverID=? && status=PENDING";
    static String ACCEPT_BOOKING = "UPDATE Booking SET Status = 'Accepted' WHERE BookingID = ?";
    static String REJECT_BOOKING="UPDATE Booking SET Status='Rejected' WHERE BookingID = ?";
    static final String VIEW_COMPLAINTS_FOR_CAREGIVER =
            "SELECT * from Complaint JOIN Booking ON Booking.BookingID = Complaint.BookingID WHERE Booking.CaregiverID = ?";

    static final String SAVE_CNIC_URL="INSERT INTO CNICUploads (CaregiverID, CNICUrl) VALUES (?, ?)";
    StringBuilder updateCaregiverQuery = new StringBuilder("UPDATE Caregiver SET ");

    public List<Caregiver> getAllCareGivers() {
        List<Caregiver> caregivers = new ArrayList<>();
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_CAREGIVERS);
             ResultSet rs = statement.executeQuery();
        ) {
            while (rs.next()) {
                Caregiver caregiver = new Caregiver();
                caregiver.setCareGiverId(rs.getInt("caregiverid"));
                caregiver.setAddress(rs.getString("address"));
                caregiver.setBio(rs.getString("bio"));
                caregiver.setCNIC(rs.getString("cnic"));
                caregiver.setEmail(rs.getString("email"));
                caregiver.setContact(rs.getString("PhoneNumber"));
                caregiver.setExperienceYears(rs.getInt("experienceYears"));
                caregiver.setFullName(rs.getString("fullname"));
                caregiver.setMonthlyRate(rs.getInt("monthlyrate"));
                caregiver.setLocation(rs.getString("location"));
                caregiver.setPassword(rs.getString("password"));
                caregiver.setVerified(rs.getBoolean("isverified"));

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
    public List<LocalDate> getAvailableDays(int caregiverId, LocalDate fromDate, LocalDate toDate) {
        Set<LocalDate> allDays = new LinkedHashSet<>();
        Set<LocalDate> unavailableDays = new HashSet<>();

        LocalDate current = fromDate;
        while (!current.isAfter(toDate)) {
            allDays.add(current);
            current = current.plusDays(1);
        }

        try (Connection conn = DatabaseConnectorService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_AVAILABLE_DAYS)) {

            stmt.setInt(1, caregiverId);
            stmt.setDate(2, Date.valueOf(toDate));
            stmt.setDate(3, Date.valueOf(fromDate));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate start = rs.getDate("StartDate").toLocalDate();
                LocalDate end = rs.getDate("EndDate").toLocalDate();

                while (!start.isAfter(end)) {
                    if (!start.isBefore(fromDate) && !start.isAfter(toDate)) {
                        unavailableDays.add(start);
                    }
                    start = start.plusDays(1);
                }
            }

        } catch (SQLException e) {
            logger.error(LogUtils.error("Error getting unavailable slots: " + e.getMessage()));
        }

        allDays.removeAll(unavailableDays);

        return new ArrayList<>(allDays);
    }

    //set available time slots
    public void setAvailableSlots(int caregiverId, LocalDate startDay, LocalDate endDay, String reason) {

        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_AVAILABLE_SLOTS);) {
            statement.setInt(1, caregiverId);
            statement.setDate(2, Date.valueOf(startDay));
            statement.setDate(3, Date.valueOf(endDay));
            statement.setString(4, reason);
                    statement.executeUpdate();
            }
        catch (SQLException e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }



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
            logger.info(LogUtils.info("Bookings incoming: " + bookings.size()));
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
        return bookings;
    }

    //Accept booking
    public void acceptBooking(int bookingId) {
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(ACCEPT_BOOKING);) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
            logger.info(LogUtils.info("Booking accepted: " + bookingId));
        }
        catch (SQLException e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }



    //reject booking
    public void rejectBooking(int bookingId) {
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(REJECT_BOOKING);) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
            logger.info(LogUtils.info("Booking accepted: " + bookingId));
        }
        catch (SQLException e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }

    //View misconduct reports filed against them
    public List<Complaint> viewComplaints(int caregiverId) {
        List<Complaint> complaints = new ArrayList<>();
        try (Connection connection = DatabaseConnectorService.getConnection();
             PreparedStatement statement = connection.prepareStatement(VIEW_COMPLAINTS_FOR_CAREGIVER);
        ) {
            statement.setInt(1, caregiverId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Complaint complaint = new Complaint();
                complaint.setComplaintId(rs.getInt("complaint_id"));
                complaint.setBookingId(rs.getInt("booking_id"));
                complaint.setDescription(complaint.getDescription());
                complaint.setSubmittedBy(complaint.getSubmittedBy());
                complaint.setCreatedAt(complaint.getCreatedAt());
                complaints.add(complaint);
            }
            logger.info(LogUtils.info("Bookings incoming: " + complaints.size()));
        } catch (Exception e) {
            logger.error(LogUtils.error(e.getMessage()));
        }
        return complaints;
    }


    public void saveCNICURL(int caregiverId, String url) {

        try(Connection connection = DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(SAVE_CNIC_URL);

        ){
            statement.setInt(1, caregiverId);
            statement.setString(2, url);
            statement.executeUpdate();
            logger.info(LogUtils.info("CNIC URL saved: " + url));
        }catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }


    public void uploadCNIC(int caregiverId, String imageFile) throws IOException {
        S3Uploader s3Uploader = new S3Uploader();
        String url = s3Uploader.uploadCNIC( caregiverId,imageFile);
        try(Connection connection = DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(SAVE_CNIC_URL);
        ) {
            statement.setInt(1, caregiverId);
            statement.setString(2, url);
            statement.executeUpdate();
        }
        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }
    }
    public void editCaregiverProfile(int caregiverId, Caregiver caregiver) {
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if(caregiver.getFullName()!=null){
            updates.add("FullName = ?");
            params.add(caregiver.getFullName());
        }
        if(caregiver.getEmail()!=null){
            updates.add("Email = ?");
            params.add(caregiver.getEmail());
        }
        if(caregiver.getAddress()!=null){
            updates.add("Address = ?");
            params.add(caregiver.getAddress());
        }
        if(caregiver.getBio()!=null){
            updates.add("Bio = ?");
            params.add(caregiver.getBio());
        }
        if(caregiver.getContact()!=null){
            updates.add("Contact = ?");
            params.add(caregiver.getContact());
        }
        if(caregiver.getLocation()!=null){
            updates.add("Location = ?");
            params.add(caregiver.getLocation());
        }
        if(caregiver.getExperienceYears()!=0){
            updates.add("ExperienceYears = ?");
            params.add(caregiver.getExperienceYears());
        }
        if(caregiver.getMonthlyRate()!=0){
            updates.add("MonthlyRate = ?");
            params.add(caregiver.getMonthlyRate());
        }
        if(caregiver.getPassword()!=null){
            updates.add("Password = ?");
            params.add(caregiver.getPassword());
        }
        if(updates.size()==0){
            logger.info(LogUtils.info("Nothing to update: " + caregiver.getFullName()));
            return;
        }

        updateCaregiverQuery.append(String.join(", ", updates));
        updateCaregiverQuery.append(" WHERE CaregiverID = ?");
        params.add(caregiverId);
        try(Connection connection = DatabaseConnectorService.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.valueOf(updateCaregiverQuery));


        ) {


            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            statement.executeUpdate();
        }

        catch (Exception e){
            logger.error(LogUtils.error(e.getMessage()));
        }

    }
}