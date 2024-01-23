package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.FindCategoryPageListReqVO;
import com.arrebol.admin.model.vo.category.AddCategoryReqVO;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;

public interface AdminCategoryService {
    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);
}