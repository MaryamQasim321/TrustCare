package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UnavailableSlot {

    @Schema(description = "Unique identifier for the unavailable slot", type = "integer", example = "2001")
    private int slotId;

    @Schema(description = "Unique identifier of the caregiver", type = "String", example = "101")
    private int caregiverId;

    @Schema(description = "Reason why the caregiver is unavailable", type = "String", example = "On vacation")
    private String reason;

    @Schema(description = "Start date of unavailability", type = "local date time", example = "2025-09-01")
    private LocalDate startDate;

    @Schema(description = "End date of unavailability", type = "local date time", example = "2025-09-10")
    private LocalDate endDate;

    @Schema(description = "Timestamp when this slot was created", type = "local date time", example = "2025-08-22T09:30:00")
    private LocalDateTime createdAt;

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
