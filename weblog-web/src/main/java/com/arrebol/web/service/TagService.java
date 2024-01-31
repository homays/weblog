package com.arrebol.web.service;

import com.arrebol.common.util.Response;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/31
 */
public interface TagService {
    /**
     * 获取标签列表
     * @return
     */
    Response findTagList();
}