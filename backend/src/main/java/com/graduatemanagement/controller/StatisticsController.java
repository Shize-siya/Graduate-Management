package com.graduatemanagement.controller;

import com.graduatemanagement.dto.ApiResponse;
import com.graduatemanagement.dto.StatisticsDTO;
import com.graduatemanagement.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ApiResponse<StatisticsDTO> getStatistics() {
        StatisticsDTO stats = statisticsService.getStatistics();
        return ApiResponse.success(stats);
    }
}
