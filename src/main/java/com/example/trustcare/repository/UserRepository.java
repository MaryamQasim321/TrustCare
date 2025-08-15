package com.example.trustcare.repository;

import com.example.trustcare.model.Booking;
import com.example.trustcare.model.Payment;
import com.example.trustcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //     static String GET_USER_BY_ID="select * from User where userID=?";
User findByUserId(Integer userId);

    Optional<User> findByEmail(String email);//     StringBuilder  updateUserQuery=new StringBuilder("UPDATE User SET ");


//     static String VIEW_ALL_BOOKINGS_FOR_USER="select * from Booking where userID=?";
//@Query("select b from Booking b where b.userId=:userId")
//List<Booking> findBookingByUserId(@Param("userId") String userId);

//     static String CREATE_BOOKING="insert into booking (userId, caregiverId, startTime, endTime, status, totalAmount) values (?,?,?,?,?,?)";

//     static String SUBMIT_COMPLAINT="insert into complaint (bookingId, submittedBy, description,createdAt ) values (?,?,?,?)";

//     static String VIEW_BOOKING_BY_ID="select * from Booking where bookingID=?";
//Booking findByBookingID(int bookingId);


//     static String VIEW_PAYMENT="select * from Payments where userID=?";
//List<Payment> findByUserId(int userId);



//     static String SUBMIT_REVIEW="insert into review(bookingId, Rating, Comment, createdAt) values (?,?,?,?)";

}
