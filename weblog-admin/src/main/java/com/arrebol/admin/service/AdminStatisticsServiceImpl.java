package com.arrebol.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.arrebol.common.domain.dos.ArticleCategoryRelDO;
import com.arrebol.common.domain.dos.CategoryDO;
import com.arrebol.common.domain.mapper.ArticleCategoryRelMapper;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public void statisticsCategoryArticleTotal() {
        // 查询所有分类
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());

        // 查询所有文章-分类映射记录
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectList(Wrappers.emptyWrapper());

        // 按所属分类 ID 进行分组
        Map<Long, List<ArticleCategoryRelDO>> categoryIdAndArticleCategoryRelDOMap = Maps.newHashMap();
        // 如果不为空
        if (CollUtil.isNotEmpty(articleCategoryRelDOS)) {
            categoryIdAndArticleCategoryRelDOMap = articleCategoryRelDOS.stream()
                    .collect(Collectors.groupingBy(ArticleCategoryRelDO::getCategoryId));
        }

        if (CollUtil.isNotEmpty(categoryDOS)) {
            // 循环统计各分类下的文章总数
            for (CategoryDO categoryDO : categoryDOS) {
                Long categoryId = categoryDO.getId();
                // 获取此分类下所有映射记录
                List<ArticleCategoryRelDO> articleCategoryRelDOList = categoryIdAndArticleCategoryRelDOMap.get(categoryId);

                // 获取文章总数
                int articlesTotal = CollUtil.isEmpty(articleCategoryRelDOList) ? 0 : articleCategoryRelDOList.size();

                // 更新该分类的文章总数
                CategoryDO categoryDO1 = CategoryDO.builder()
                        .id(categoryId)
                        .articlesTotal(articlesTotal)
                        .build();
                categoryMapper.updateById(categoryDO1);
            }
        }
    }
}
