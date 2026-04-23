package com.graduatemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "exam_year", nullable = false, length = 4)
    private String examYear;

    @Column(name = "first_choice_school", nullable = false, length = 100)
    private String firstChoiceSchool;

    @Column(name = "first_choice_major", nullable = false, length = 100)
    private String firstChoiceMajor;

    @Column(name = "second_choice_school", length = 100)
    private String secondChoiceSchool;

    @Column(name = "second_choice_major", length = 100)
    private String secondChoiceMajor;

    @Column(name = "exam_subject1", length = 100)
    private String examSubject1;

    @Column(name = "exam_subject2", length = 100)
    private String examSubject2;

    @Column(name = "exam_subject3", length = 100)
    private String examSubject3;

    @Column(name = "exam_subject4", length = 100)
    private String examSubject4;

    @Column(name = "initial_exam_score")
    private Integer initialExamScore;

    @Column(name = "recheck_exam_score")
    private Integer recheckExamScore;

    @Column(length = 20)
    private String status;

    @Column(name = "exam_location", length = 100)
    private String examLocation;

    @Column(name = "exam_date")
    private String examDate;

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
