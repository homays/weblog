package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.arrebol.common.util.Response;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/22
 */
public interface AdminUserService {
    /**
     * 修改密码
     * @param updateAdminUserPasswordReqVO
     * @return
     */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

    /**
     * 获取当前登录用户信息
     * @return
     */
    Response findUserInfo();
}
