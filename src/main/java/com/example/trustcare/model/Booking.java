package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Booking {

    @Schema(description = "Unique ID for the booking", example = "101")
    private int bookingId;

    @Schema(description = "ID of the user who made the booking", example = "15")
    private int userId;

    @Schema(description = "ID of the caregiver assigned", example = "7")
    private int caregiverId;

    @Schema(description = "Booking start time", example = "2025-08-22T10:00:00Z")
    private Timestamp startTime;

    @Schema(description = "Booking end time", example = "2025-08-22T12:00:00Z")
    private Timestamp endTime;

    @Schema(description = "Current status of booking", example = "CONFIRMED")
    private String status;

    @Schema(description = "Total amount for the booking", example = "1500.50")
    private BigDecimal totalAmount;
//
//    public int getBookingId() {
//        return bookingId;
//    }
//
//    public void setBookingId(int bookingId) {
//        this.bookingId = bookingId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getCaregiverId() {
//        return caregiverId;
//    }
//
//    public void setCaregiverId(int caregiverId) {
//        this.caregiverId = caregiverId;
//    }
//
//    public Timestamp getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Timestamp startTime) {
//        this.startTime = startTime;
//    }
//
//    public Timestamp getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Timestamp endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public BigDecimal getTotalAmount() {
//        return totalAmount;
//    }
//
//    public void setTotalAmount(BigDecimal totalAmount) {
//        this.totalAmount = totalAmount;
//    }
}
