package com.example.trustcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AvailabilityRequest {

    @Schema(description = "Start date of caregiver availability", example = "2025-08-25")
    private String startDate;

    @Schema(description = "End date of caregiver availability", example = "2025-08-30")
    private String endDate;

    @Schema(description = "Reason for availability update", example = "Vacation")
    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
