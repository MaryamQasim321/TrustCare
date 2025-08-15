package com.example.trustcare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaintID")
    private int complaintId;

    @ManyToOne
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    @Column(name = "submittedby", nullable = false)
    private int submittedBy;

    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "createdat", updatable = false)
    private LocalDateTime createdAt;

    // Getters and setters

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public Booking getBooking() {

        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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

    // No setter for createdAt if you want it to be auto-set only
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
