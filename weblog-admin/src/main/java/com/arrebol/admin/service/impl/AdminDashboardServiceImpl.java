package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.arrebol.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.arrebol.admin.service.AdminDashboardService;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.dos.ArticlePublishCountDO;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.arrebol.common.domain.mapper.TagMapper;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.LocaleData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public Response findDashboardPublishArticleStatistics() {
        // 当前日期
        LocalDate currDate = LocalDate.now();

        // 当前日期倒退一年的日期
        LocalDate startDate = currDate.minusYears(1);

        // 查找这一年内，每日发布的文章数量
        List<ArticlePublishCountDO> articlePublishCountDOS = articleMapper.selectDateArticlePublishCount(startDate, currDate.plusDays(1));

        Map<LocalDate, Long> map = null;
        if (CollUtil.isNotEmpty(articlePublishCountDOS)) {
            // DO 转 Map
            Map<LocalDate, Long> dateArticleCountMap = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate, ArticlePublishCountDO::getCount));

            // 有序 Map, 返回的日期文章数需要以升序排列
            map = Maps.newLinkedHashMap();
            // 从上一年的今天循环到今天
            for (; startDate.isBefore(currDate) || startDate.isEqual(currDate); startDate = startDate.plusDays(1)) {
                // 以日期作为 key 从 dateArticleCountMap 中取文章发布总量
                Long count = dateArticleCountMap.get(startDate);
                // 设置到返参 Map
                map.put(startDate, Objects.isNull(count) ? 0 : count);
            }
        }
        return Response.success(map);
    }
}
