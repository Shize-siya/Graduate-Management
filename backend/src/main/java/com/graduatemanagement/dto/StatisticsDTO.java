package com.graduatemanagement.dto;

import lombok.Data;

@Data
public class StatisticsDTO {
    private long totalCandidates;
    private long totalApplications;
    private long totalAdmissions;
    private long pendingApplications;
    private long admittedCount;

    private long maleCount;
    private long femaleCount;

    private long application2024;
    private long application2025;
    private long admission2024;
    private long admission2025;

    private long pendingReview2025;
    private long admitted2024;
    private long notAdmitted2024;
}
