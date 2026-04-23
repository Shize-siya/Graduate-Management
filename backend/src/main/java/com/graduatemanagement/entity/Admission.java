package com.graduatemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admissions")
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "admission_year", nullable = false, length = 4)
    private String admissionYear;

    @Column(name = "admitted_school", nullable = false, length = 100)
    private String admittedSchool;

    @Column(name = "admitted_major", nullable = false, length = 100)
    private String admittedMajor;

    @Column(name = "admission_type", length = 20)
    private String admissionType;

    @Column(name = "supervisor", length = 50)
    private String supervisor;

    @Column(name = "admission_score")
    private Integer admissionScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(length = 20)
    private String status;

    @Column(name = "enrollment_date")
    private String enrollmentDate;

    @Column(name = "tuition_standard")
    private Double tuitionStandard;

    @Column(name = "scholarship_level", length = 20)
    private String scholarshipLevel;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
