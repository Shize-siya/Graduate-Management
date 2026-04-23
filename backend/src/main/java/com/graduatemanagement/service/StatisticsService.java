package com.graduatemanagement.service;

import com.graduatemanagement.dto.StatisticsDTO;
import com.graduatemanagement.entity.Candidate;
import com.graduatemanagement.repository.CandidateRepository;
import com.graduatemanagement.repository.ApplicationRepository;
import com.graduatemanagement.repository.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    public StatisticsDTO getStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        stats.setTotalCandidates(candidateRepository.count());
        stats.setTotalApplications(applicationRepository.count());
        stats.setTotalAdmissions(admissionRepository.count());

        stats.setPendingApplications(applicationRepository.countByStatus("待审核"));
        stats.setAdmittedCount(admissionRepository.countByStatus("已录取"));

        stats.setApplication2024(applicationRepository.countByExamYear("2024"));
        stats.setApplication2025(applicationRepository.countByExamYear("2025"));
        stats.setAdmission2024(admissionRepository.countByAdmissionYear("2024"));
        stats.setAdmission2025(admissionRepository.countByAdmissionYear("2025"));

        stats.setPendingReview2025(applicationRepository.countByExamYearAndStatus("2025", "待审核"));
        stats.setAdmitted2024(admissionRepository.countByAdmissionYear("2024"));
        stats.setNotAdmitted2024(applicationRepository.countByExamYear("2024") - admissionRepository.countByAdmissionYear("2024"));

        return stats;
    }
}
