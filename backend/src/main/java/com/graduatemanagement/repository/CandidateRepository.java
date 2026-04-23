package com.graduatemanagement.repository;

import com.graduatemanagement.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByNameContaining(String name);
    List<Candidate> findByIdCard(String idCard);

    @Query("SELECT c FROM Candidate c WHERE c.name LIKE %:keyword% OR c.idCard LIKE %:keyword% OR c.phone LIKE %:keyword%")
    List<Candidate> searchByKeyword(@Param("keyword") String keyword);

    long count();
}
