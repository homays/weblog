package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.article.PublishArticleReqVO;
import com.arrebol.common.util.Response;

public interface AdminArticleService {
    /**
     * 发布文章
     * @param publishArticleReqVO
     * @return
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);
}