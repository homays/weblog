package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.tag.AddTagReqVO;
import com.arrebol.admin.model.vo.tag.DeleteTagReqVO;
import com.arrebol.admin.model.vo.tag.FindTagPageListReqVO;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;

public interface AdminTagService {

    /**
     * 添加标签集合
     * @param addTagReqVO
     * @return
     */
    Response addTags(AddTagReqVO addTagReqVO);

    /**
     * 查询标签分页
     * @param findTagPageListReqVO
     * @return
     */
    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);

    /**
     * 删除标签
     * @param deleteTagReqVO
     * @return
     */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);
}
