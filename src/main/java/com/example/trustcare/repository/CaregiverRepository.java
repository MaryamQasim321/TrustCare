package com.example.trustcare.repository;

import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.Complaint;
import com.example.trustcare.model.UnavailableSlot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver,Integer> {

//    static String GET_ALL_CAREGIVERS = "select * from Caregiver";
List<Caregiver> findAll();
    Optional<Caregiver> findByEmail(String email);



//    static String GET_CAREGIVER_BY_ID = "select * from Caregiver where caregiverid=?";
//Caregiver findByCareGiverId(int careGiverId);



//    static String SEARCH_CAREGIVER = "select * from Caregiver where FullName=?";
//List<Caregiver> findAllByFullName(String fullName);


//    static String GET_AVAILABLE_DAYS="SELECT StartDate, EndDate FROM UnavailableSlot WHERE CaregiverID = ? " +
//            "AND (StartDate <= ? AND EndDate >= ?)";
//@Query("select u from UnavailableSlot u where u.caregiverID=:caregiverId and u.startDate<=:endDate and u.endDate>=:startDate")
//List<UnavailableSlot> findUnavailableSlots(int caregiverId,  int startDate, int endDate);
//


    //    static String SET_AVAILABLE_SLOTS="insert into UnavailableSlot(caregiverID, startDate, endDate, reason) values (?, ?, ?,?)";

    //    static String GET_INCOMING_BOOKINGS="select * from booking where caregiverID=? && status='PENDING'";
//@Query("SELECT b FROM Booking b WHERE b.caregiverId = :caregiverId AND b.status = 'PENDING'")
//List<Booking> findPendingBookings(int caregiverId);



//    static String ACCEPT_BOOKING = "UPDATE Booking SET Status = 'Accepted' WHERE BookingID = ?";
//@Modifying
//    @Transactional
//    @Query("UPDATE Booking b set b.status='ACCEPTED' where b.bookingId=:bookingId and b.caregiverId=:caregiverId")
//int acceptBooking(int bookingId,  int caregiverId);


//    static String REJECT_BOOKING="UPDATE Booking SET Status='Rejected' WHERE BookingID = ?";
//@Modifying
//@Transactional
//@Query("UPDATE Booking b set b.status='REJECTED' where b.bookingId=:bookingId and b.caregiverId=:caregiverId")
//int rejectBooking(int bookingId,  int caregiverId);

//    static final String VIEW_COMPLAINTS_FOR_CAREGIVER =
//    "SELECT * from Complaint JOIN Booking ON Booking.BookingID = Complaint.BookingID WHERE Booking.CaregiverID = ?";
//@Query("SELECT c.complaints FROM Caregiver c WHERE c.id = :caregiverId")
//List<Complaint> findComplaintsByCaregiverId(@Param("caregiverId") int caregiverId);

//    static final String SAVE_CNIC_URL="INSERT INTO CNICUploads (CaregiverID, CNICUrl) VALUES (?, ?)";

//    StringBuilder updateCaregiverQuery = new StringBuilder("UPDATE Caregiver SET ");

}
