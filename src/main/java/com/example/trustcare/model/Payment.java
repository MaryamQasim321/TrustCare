package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {

    @Schema(description = "Unique identifier of the payment", type = "integer", example = "9001")
    private int paymentId;

    @Schema(description = "ID of the related booking", type = "integer", example = "3001")
    private int bookingId;

    @Schema(description = "Amount paid", type = "big decimal", example = "1500.00")
    private BigDecimal amount;

    @Schema(description = "Payment method used", type = "String", example = "Credit Card")
    private String method;

    @Schema(description = "Current status of the payment", type = "String", example = "COMPLETED")
    private String status;

    @Schema(description = "Date and time when the payment was made", type = "local date time", example = "2025-08-22T10:30:00")
    private LocalDateTime paymentDate;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
