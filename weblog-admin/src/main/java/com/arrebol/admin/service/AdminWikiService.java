package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.wiki.*;
import com.arrebol.common.util.Response;

public interface AdminWikiService {

    /**
     * 新增知识库
     */
    Response addWiki(AddWikiReqVO addWikiReqVO);

    /**
     * 删除知识库
     */
    Response deleteWiki(DeleteWikiReqVO deleteWikiReqVO);

    /**
     * 知识库分页查询
     */
    Response findWikiPageList(FindWikiPageListReqVO findWikiPageListReqVO);

    /**
     * 更新知识库置顶状态
     */
    Response updateWikiIsTop(UpdateWikiIsTopReqVO updateWikiIsTopReqVO);

    /**
     * 更新知识库发布状态
     */
    Response updateWikiIsPublish(UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO);

    /**
     * 更新知识库
     */
    Response updateWiki(UpdateWikiReqVO updateWikiReqVO);

    /**
     * 查询知识库目录
     */
    Response findWikiCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO);

    /**
     * 更新知识库目录
     */
    Response updateWikiCatalogs(UpdateWikiCatalogReqVO updateWikiCatalogReqVO);

}