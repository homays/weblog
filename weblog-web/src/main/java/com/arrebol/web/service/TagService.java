package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.tag.FindTagArticlePageListReqVO;

public interface TagService {
    /**
     * 获取标签列表
     */
    Response findTagList();

    /**
     * 获取标签下文章分页列表
     */
    Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO);
}