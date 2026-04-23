package com.graduatemanagement.service;

import com.graduatemanagement.entity.Admission;
import com.graduatemanagement.repository.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdmissionService {

    @Autowired
    private AdmissionRepository admissionRepository;

    public List<Admission> findAll() {
        return admissionRepository.findAll();
    }

    public Optional<Admission> findById(Long id) {
        return admissionRepository.findById(id);
    }

    public List<Admission> findByCandidateId(Long candidateId) {
        return admissionRepository.findByCandidateId(candidateId);
    }

    public Admission save(Admission admission) {
        return admissionRepository.save(admission);
    }

    public Admission update(Long id, Admission admission) {
        admission.setId(id);
        return admissionRepository.save(admission);
    }

    public void deleteById(Long id) {
        admissionRepository.deleteById(id);
    }

    public List<Admission> search(String keyword) {
        return admissionRepository.searchByKeyword(keyword);
    }

    public long count() {
        return admissionRepository.count();
    }

    public long countByStatus(String status) {
        return admissionRepository.countByStatus(status);
    }
}
