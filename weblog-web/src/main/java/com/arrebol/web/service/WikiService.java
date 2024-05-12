package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.wiki.FindWikiArticlePreNextReqVO;
import com.arrebol.web.model.vo.wiki.FindWikiCatalogListReqVO;

public interface WikiService {

    /**
     * 获取知识库
     */
    Response findWikiList();

    /**
     * 获取知识库目录
     */
    Response findWikiCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO);

    /**
     * 获取上下页
     */
    Response findArticlePreNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO);

}