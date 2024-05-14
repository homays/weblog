package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.comment.FindCommentPageListReqVO;
import com.arrebol.common.util.Response;

public interface AdminCommentService {
    
    /**
     * 查询评论分页数据
     */
    Response findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO);

}