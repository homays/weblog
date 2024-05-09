package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.wiki.AddWikiReqVO;
import com.arrebol.common.util.Response;

public interface AdminWikiService {

    /**
     * 新增知识库
     * @param addWikiReqVO
     * @return
     */
    Response addWiki(AddWikiReqVO addWikiReqVO);
}