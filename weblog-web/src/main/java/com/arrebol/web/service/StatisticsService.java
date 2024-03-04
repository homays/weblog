package com.arrebol.web.service;

import com.arrebol.common.util.Response;

public interface StatisticsService {
    /**
     * 获取文章总数、分类总数、标签总数、总访问量统计信息
     */
    Response findInfo();
}