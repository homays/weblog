package com.arrebol.admin.service;

import com.arrebol.common.util.Response;

public interface AdminDashboardService {

    /**
     * 获取仪表盘基础统计信息
     */
    Response findDashboardStatistics();
}