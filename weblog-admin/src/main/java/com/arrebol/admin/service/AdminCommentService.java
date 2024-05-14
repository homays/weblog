package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.comment.DeleteCommentReqVO;
import com.arrebol.admin.model.vo.comment.FindCommentPageListReqVO;
import com.arrebol.common.util.Response;

public interface AdminCommentService {
    
    /**
     * 查询评论分页数据
     */
    Response findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO);

    /**
     * 删除评论
     */
    Response deleteComment(DeleteCommentReqVO deleteCommentReqVO);

}