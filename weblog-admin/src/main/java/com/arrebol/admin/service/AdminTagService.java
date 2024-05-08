package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.tag.*;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;

public interface AdminTagService {

    /**
     * 添加标签集合
     */
    Response addTags(AddTagReqVO addTagReqVO);

    /**
     * 查询标签分页
     */
    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);

    /**
     * 删除标签
     */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    /**
     * 根据标签关键词模糊查询
     */
    Response searchTags(SearchTagsReqVO searchTagsReqVO);

    /**
     * 查询标签 Select 列表数据
     */
    Response findTagSelectList();

    /**
     * 编辑标签
     */
    Response editTag(EditTagReqVO editTagReqVO);
}
