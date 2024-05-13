package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.comment.FindQQUserInfoReqVO;
import com.arrebol.web.model.vo.comment.PublishCommentReqVO;

public interface CommentService {

    /**
     * 根据 QQ 号获取用户信息
     */
    Response findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO);

    /**
     * 发布评论
     */
    Response publishComment(PublishCommentReqVO publishCommentReqVO);

}