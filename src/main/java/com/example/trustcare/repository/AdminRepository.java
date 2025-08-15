package com.example.trustcare.repository;

import com.example.trustcare.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    //static String GET_UNVERIFIED_CAREGIVERS="select * from Caregiver where isverified=false";
@Query("select c from Caregiver c where c.isVerified=false")
    List<Caregiver> findUnverifiedCaregivers();

    Optional<Admin> findByEmail(String email);


//    static String APPROVE_CAREGIVER="set isverified=true where caregiverid=?";
//@Modifying
//    @Transactional
//    @Query("update Caregiver c set c.isVerified=true where c.careGiverId=:caregiverId")
//    int approveCaregiver();



//    static String REJECT_CAREGIVER="set isverified=false where caregiverid=?";
//    @Modifying
//    @Transactional
//    @Query("update Caregiver  c set c.isVerified=false where c.careGiverId=:caregiverId")
//    int rejectCaregiver(int caregiverId);



//    static String VIEW_COMPLAINTS="select complaint.*, booking.caregiverid from complaint join on complaint.bookingid=booking.bookingid ";
//@Query("SELECT c FROM Complaint c JOIN c.booking b")
//List<Complaint> findAllComplaintsWithBooking();


//    static String VIEW_PAYMENTS="select * from payments";
//    List<Payment> findAllPayments();


//    static String ADD_CAREGIVER="insert into caregiver (   CaregiverID,fullName,  Email,CNIC, ExperienceYears, MonthlyRate, Location, Address,PhoneNumber, Bio, isVerified, createdAt, password )  values (?,?,?,?,?,?,?,?,?,?,?,?, ?)";

}
