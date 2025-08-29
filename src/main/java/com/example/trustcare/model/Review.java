package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {

    @Schema(description = "Unique identifier of the review", type = "integer", example = "101")
    private int reviewId;

    @Schema(description = "ID of the booking this review is associated with", type = "integer", example = "3001")
    private int bookingId;

    @Schema(description = "Review comment provided by the customer", type = "String", example = "Excellent service and friendly staff.")
    private String comment;

    @Schema(description = "Date and time when the review was created", type = "local date time", example = "2025-08-22T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Rating given by the customer (1 to 5)", type = "String", example = "5")
    private int rating;

//    public int getReviewId() {
//        return reviewId;
//    }
//
//    public void setReviewId(int reviewId) {
//        this.reviewId = reviewId;
//    }
//
//    public int getBookingId() {
//        return bookingId;
//    }
//
//    public void setBookingId(int bookingId) {
//        this.bookingId = bookingId;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public int getRating() {
//        return rating;
//    }
//
//    public void setRating(int rating) {
//        this.rating = rating;
//    }
}
