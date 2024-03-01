package com.arrebol.admin.controller;

import com.arrebol.admin.service.AdminDashboardService;
import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@Api(tags = "Admin 仪表盘")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;

    @PostMapping("/statistics")
    @ApiOperation(value = "获取后台仪表盘基础统计信息")
    @ApiOperationLog(description = "获取后台仪表盘基础统计信息")
    public Response findDashboardStatistics() {
        return dashboardService.findDashboardStatistics();
    }

}