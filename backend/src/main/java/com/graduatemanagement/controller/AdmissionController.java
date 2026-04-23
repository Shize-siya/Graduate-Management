package com.graduatemanagement.controller;

import com.graduatemanagement.dto.ApiResponse;
import com.graduatemanagement.entity.Admission;
import com.graduatemanagement.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    @Autowired
    private AdmissionService admissionService;

    @GetMapping
    public ApiResponse<List<Admission>> getAllAdmissions() {
        List<Admission> admissions = admissionService.findAll();
        return ApiResponse.success(admissions);
    }

    @GetMapping("/{id}")
    public ApiResponse<Admission> getAdmissionById(@PathVariable Long id) {
        return admissionService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("录取信息不存在"));
    }

    @GetMapping("/candidate/{candidateId}")
    public ApiResponse<List<Admission>> getAdmissionsByCandidate(@PathVariable Long candidateId) {
        List<Admission> admissions = admissionService.findByCandidateId(candidateId);
        return ApiResponse.success(admissions);
    }

    @PostMapping
    public ApiResponse<Admission> createAdmission(@RequestBody Admission admission) {
        try {
            Admission saved = admissionService.save(admission);
            return ApiResponse.success("录取信息创建成功", saved);
        } catch (Exception e) {
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Admission> updateAdmission(@PathVariable Long id, @RequestBody Admission admission) {
        try {
            Admission updated = admissionService.update(id, admission);
            return ApiResponse.success("录取信息更新成功", updated);
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAdmission(@PathVariable Long id) {
        try {
            admissionService.deleteById(id);
            return ApiResponse.success("录取信息删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<Admission>> searchAdmissions(@RequestParam String keyword) {
        List<Admission> admissions = admissionService.search(keyword);
        return ApiResponse.success(admissions);
    }
}
