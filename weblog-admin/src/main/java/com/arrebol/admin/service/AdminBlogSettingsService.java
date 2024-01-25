package com.arrebol.admin.service;

import com.arrebol.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.arrebol.common.util.Response;

public interface AdminBlogSettingsService {
    /**
     * 更新博客设置信息
     * @param updateBlogSettingsReqVO
     * @return
     */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 获取博客设置详情
     * @return
     */
    Response findDetail();
}