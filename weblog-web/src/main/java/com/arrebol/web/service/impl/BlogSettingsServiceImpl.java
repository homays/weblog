package com.arrebol.web.service.impl;

import com.arrebol.common.domain.dos.BlogSettingsDO;
import com.arrebol.common.domain.mapper.BlogSettingsMapper;
import com.arrebol.common.util.Response;
import com.arrebol.web.convert.BlogSettingsConvert;
import com.arrebol.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.arrebol.web.service.BlogSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogSettingsServiceImpl implements BlogSettingsService {

    private final BlogSettingsMapper blogSettingsMapper;

    @Override
    public Response findDetail() {
        // 查询博客设置信息（约定的 ID 为 1）
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);
        // DO 转 VO
        FindBlogSettingsDetailRspVO vo = BlogSettingsConvert.INSTANCE.convertDO2VO(blogSettingsDO);

        return Response.success(vo);
    }
}