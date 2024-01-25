package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.article.DeleteArticleReqVO;
import com.arrebol.admin.model.vo.article.PublishArticleReqVO;
import com.arrebol.common.domain.dos.ArticleContentDO;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

public interface AdminArticleService {
    /**
     * 发布文章
     * @param publishArticleReqVO
     * @return
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);

    /**
     * 删除文章
     * @param deleteArticleReqVO
     * @return
     */
    Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO);
}