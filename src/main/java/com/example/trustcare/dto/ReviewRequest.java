package com.example.trustcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request object for submitting a review")
public class ReviewRequest {

    @Schema(description = "Associated booking ID", example = "2001")
    private Integer bookingId;

    @Min(1)
    @Max(5)
    @Schema(description = "Rating value between 1 and 5", example = "4")
    private int rating;

    @Schema(description = "Additional comments for the review", example = "Great service!")
    private String comments;

    @NotNull
    @Schema(description = "Review creation timestamp in ISO format", example = "2025-08-22T11:45:00")
    private String createdAt;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
