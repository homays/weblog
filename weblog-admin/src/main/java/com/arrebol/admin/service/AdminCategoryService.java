package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.category.EditCategoryReqVO;
import com.arrebol.admin.model.vo.category.FindCategoryPageListReqVO;
import com.arrebol.admin.model.vo.category.AddCategoryReqVO;
import com.arrebol.admin.model.vo.category.DeleteCategoryReqVO;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;

public interface AdminCategoryService {
    /**
     * 添加分类
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 分类分页数据查询
     */
    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /**
     * 编辑分类
     */
    Response editCategory(EditCategoryReqVO editCategoryReqVO);

    /**
     * 删除分类
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 获取文章分类的 Select 列表数据
     */
    Response findCategorySelectList();

}