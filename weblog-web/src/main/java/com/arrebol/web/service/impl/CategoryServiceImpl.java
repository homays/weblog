package com.arrebol.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.common.domain.dos.ArticleCategoryRelDO;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.dos.CategoryDO;
import com.arrebol.common.domain.mapper.ArticleCategoryRelMapper;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.arrebol.web.convert.ArticleConvert;
import com.arrebol.web.model.vo.category.FindCategoryArticlePageListReqVO;
import com.arrebol.web.model.vo.category.FindCategoryArticlePageListRspVO;
import com.arrebol.web.model.vo.category.FindCategoryListReqVO;
import com.arrebol.web.model.vo.category.FindCategoryListRspVO;
import com.arrebol.web.service.CategoryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/31
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final ArticleMapper articleMapper;

    @Override
    public Response findCategoryList(FindCategoryListReqVO findCategoryListReqVO) {
        Long size = findCategoryListReqVO.getSize();

        List<CategoryDO> categoryDOS = null;
        // 如果接口入参中未指定 size
        if (Objects.isNull(size) || size == 0) {
            // 查询所有分类
            categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
        } else {
            // 否则查询指定的数量
            categoryDOS = categoryMapper.selectByLimit(size);
        }

        // DO 转 VO
        List<FindCategoryListRspVO> vos = null;
        if (CollUtil.isNotEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .articlesTotal(categoryDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    @Override
    public Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        Long current = findCategoryArticlePageListReqVO.getCurrent();
        Long size = findCategoryArticlePageListReqVO.getSize();
        Long categoryId = findCategoryArticlePageListReqVO.getId();

        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (ObjectUtil.isNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectListByCategoryId(categoryId);

        if (CollUtil.isEmpty(articleCategoryRelDOS)) {
            return PageResponse.success(null, null);
        }

        List<Long> articleIds = articleCategoryRelDOS.stream().map(ArticleCategoryRelDO::getArticleId).collect(Collectors.toList());

        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = page.getRecords();

        List<FindCategoryArticlePageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2CategoryArticleVO)
                    .collect(Collectors.toList());
        }
        return PageResponse.success(page, vos);

    }
}
