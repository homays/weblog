package com.arrebol.web.service;

import com.arrebol.common.util.Response;

public interface CategoryService {
    /**
     * 获取分类列表
     * @return
     */
    Response findCategoryList();
}