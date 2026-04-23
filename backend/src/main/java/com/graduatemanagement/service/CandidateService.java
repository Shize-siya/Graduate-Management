package com.graduatemanagement.service;

import com.graduatemanagement.entity.Candidate;
import com.graduatemanagement.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate update(Long id, Candidate candidate) {
        candidate.setId(id);
        return candidateRepository.save(candidate);
    }

    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }

    public List<Candidate> search(String keyword) {
        return candidateRepository.searchByKeyword(keyword);
    }

    public long count() {
        return candidateRepository.count();
    }
}
