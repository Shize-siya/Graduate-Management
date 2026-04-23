package com.graduatemanagement.controller;

import com.graduatemanagement.dto.ApiResponse;
import com.graduatemanagement.entity.Application;
import com.graduatemanagement.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public ApiResponse<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.findAll();
        return ApiResponse.success(applications);
    }

    @GetMapping("/{id}")
    public ApiResponse<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("报考信息不存在"));
    }

    @GetMapping("/candidate/{candidateId}")
    public ApiResponse<List<Application>> getApplicationsByCandidate(@PathVariable Long candidateId) {
        List<Application> applications = applicationService.findByCandidateId(candidateId);
        return ApiResponse.success(applications);
    }

    @PostMapping
    public ApiResponse<Application> createApplication(@RequestBody Application application) {
        try {
            Application saved = applicationService.save(application);
            return ApiResponse.success("报考信息创建成功", saved);
        } catch (Exception e) {
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Application> updateApplication(@PathVariable Long id, @RequestBody Application application) {
        try {
            Application updated = applicationService.update(id, application);
            return ApiResponse.success("报考信息更新成功", updated);
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteById(id);
            return ApiResponse.success("报考信息删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<Application>> searchApplications(@RequestParam String keyword) {
        List<Application> applications = applicationService.search(keyword);
        return ApiResponse.success(applications);
    }
}
