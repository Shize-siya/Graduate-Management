package com.graduatemanagement.service;

import com.graduatemanagement.entity.Application;
import com.graduatemanagement.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> findByCandidateId(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public Application update(Long id, Application application) {
        application.setId(id);
        return applicationRepository.save(application);
    }

    public void deleteById(Long id) {
        applicationRepository.deleteById(id);
    }

    public List<Application> search(String keyword) {
        return applicationRepository.searchByKeyword(keyword);
    }

    public long count() {
        return applicationRepository.count();
    }

    public long countByStatus(String status) {
        return applicationRepository.countByStatus(status);
    }
}
