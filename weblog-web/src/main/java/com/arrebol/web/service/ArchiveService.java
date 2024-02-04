package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.archive.FindArchiveArticlePageListReqVO;

public interface ArchiveService {
    /**
     * 获取文章归档分页数据
     */
    Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO);
}