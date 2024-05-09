package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.category.FindCategoryArticlePageListReqVO;
import com.arrebol.web.model.vo.category.FindCategoryListReqVO;

public interface CategoryService {
    /**
     * 获取分类列表
     */
    Response findCategoryList(FindCategoryListReqVO findCategoryListReqVO);

    /**
     * 获取分类下文章分页数据
     */
    Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);
}