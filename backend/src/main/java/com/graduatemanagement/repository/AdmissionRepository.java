package com.graduatemanagement.repository;

import com.graduatemanagement.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByCandidateId(Long candidateId);
    List<Admission> findByAdmissionYear(String year);
    List<Admission> findByAdmittedSchool(String school);
    List<Admission> findByStatus(String status);

    @Query("SELECT a FROM Admission a WHERE a.admittedSchool LIKE %:keyword% OR a.admittedMajor LIKE %:keyword%")
    List<Admission> searchByKeyword(@Param("keyword") String keyword);

    long count();
    long countByStatus(String status);
    long countByAdmissionYear(String year);
    long countByAdmissionYearAndStatus(String admissionYear, String status);
}
