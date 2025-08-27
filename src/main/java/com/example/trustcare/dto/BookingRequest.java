package com.example.trustcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request object for creating a booking")
public class BookingRequest {

    @Schema(description = "Unique identifier of the caregiver", example = "101")
    private Integer caregiverId;

    @Schema(description = "Start date of the booking in ISO format (yyyy-MM-dd)", example = "2025-08-22")
    private String startDate;

    @Schema(description = "End date of the booking in ISO format (yyyy-MM-dd)", example = "2025-08-25")
    private String endDate;

    @Schema(description = "Current booking status", example = "PENDING")
    private String status;

    @Positive
    @Schema(description = "Total amount for the booking", example = "1500.00")
    private BigDecimal totalAmount;

    public Integer getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Integer caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
