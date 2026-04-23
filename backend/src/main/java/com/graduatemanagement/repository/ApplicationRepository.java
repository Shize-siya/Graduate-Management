package com.graduatemanagement.repository;

import com.graduatemanagement.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByExamYear(String examYear);
    List<Application> findByFirstChoiceSchool(String school);
    List<Application> findByStatus(String status);

    @Query("SELECT a FROM Application a WHERE a.firstChoiceSchool LIKE %:keyword% OR a.firstChoiceMajor LIKE %:keyword%")
    List<Application> searchByKeyword(@Param("keyword") String keyword);

    long count();
    long countByStatus(String status);
    long countByExamYear(String examYear);
    long countByExamYearAndStatus(String examYear, String status);
}
