package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Complaint {

    @Schema(description = "Unique identifier for the complaint",
            format = "int32",
            type = "integer")
    private int complaintId;

    @Schema(description = "Booking ID linked to the complaint", type = "integer", example = "1501")
    private int bookingId;

    @Schema(description = "User ID of the person who submitted the complaint", type = "integer", example = "101")
    private int submittedBy;

    @Schema(description = "Detailed description of the complaint",type = "String", example = "The caregiver arrived 2 hours late without prior notice.")
    private String description;

    @Schema(description = "Timestamp when the complaint was submitted", example = "2025-08-22T10:30:45", type = "Local date and time")
    private LocalDateTime createdAt;

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(int submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
