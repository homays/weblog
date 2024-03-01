package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.arrebol.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.arrebol.admin.service.AdminDashboardService;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.arrebol.common.domain.mapper.TagMapper;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/3/1
 */
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public Response findDashboardStatistics() {
        // 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum();
        long pvTotalCount = 0L;

        if (CollUtil.isNotEmpty(articleDOS)) {
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        // 组装 VO 类
        FindDashboardStatisticsInfoRspVO vo = FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();

        return Response.success(vo);
    }
}
