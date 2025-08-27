package com.example.trustcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request object for submitting a complaint")
public class ComplaintRequest {

    @Schema(description = "Associated booking ID", example = "2001")
    private Integer bookingId;

    @Schema(description = "Detailed complaint description", example = "The caregiver arrived late.")
    private String description;

    @NotBlank
    @Schema(description = "Complaint creation timestamp in ISO format", example = "2025-08-22T10:30:00")
    private String createdAt;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
