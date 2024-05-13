package com.arrebol.web.service;

import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.comment.FindQQUserInfoReqVO;

public interface CommentService {

    /**
     * 根据 QQ 号获取用户信息
     */
    Response findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO);

}