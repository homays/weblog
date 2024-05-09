package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.wiki.AddWikiReqVO;
import com.arrebol.admin.model.vo.wiki.DeleteWikiReqVO;
import com.arrebol.admin.model.vo.wiki.FindWikiPageListReqVO;
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

}