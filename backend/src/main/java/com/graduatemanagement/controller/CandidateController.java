package com.graduatemanagement.controller;

import com.graduatemanagement.dto.ApiResponse;
import com.graduatemanagement.entity.Candidate;
import com.graduatemanagement.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public ApiResponse<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.findAll();
        return ApiResponse.success(candidates);
    }

    @GetMapping("/{id}")
    public ApiResponse<Candidate> getCandidateById(@PathVariable Long id) {
        return candidateService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("考生不存在"));
    }

    @PostMapping
    public ApiResponse<Candidate> createCandidate(@RequestBody Candidate candidate) {
        try {
            Candidate saved = candidateService.save(candidate);
            return ApiResponse.success("考生信息创建成功", saved);
        } catch (Exception e) {
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate candidate) {
        try {
            Candidate updated = candidateService.update(id, candidate);
            return ApiResponse.success("考生信息更新成功", updated);
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCandidate(@PathVariable Long id) {
        try {
            candidateService.deleteById(id);
            return ApiResponse.success("考生信息删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<Candidate>> searchCandidates(@RequestParam String keyword) {
        List<Candidate> candidates = candidateService.search(keyword);
        return ApiResponse.success(candidates);
    }
}
