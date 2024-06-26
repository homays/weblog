package com.arrebol.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.arrebol.admin.model.vo.category.*;
import com.arrebol.admin.service.AdminCategoryService;
import com.arrebol.common.domain.dos.ArticleCategoryRelDO;
import com.arrebol.common.domain.dos.CategoryDO;
import com.arrebol.common.domain.dos.TagDO;
import com.arrebol.common.domain.mapper.ArticleCategoryRelMapper;
import com.arrebol.common.domain.mapper.ArticleTagRelMapper;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        String categoryReqVOName = addCategoryReqVO.getName();
        CategoryDO categoryDO = categoryMapper.selectByName(categoryReqVOName);
        if (Objects.nonNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }
        CategoryDO result = CategoryDO.builder().name(categoryReqVOName).build();
        categoryMapper.insert(result);
        return Response.success();
    }

    @Override
    public PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        Long current = findCategoryPageListReqVO.getCurrent();
        Long size = findCategoryPageListReqVO.getSize();
        Page<CategoryDO> page = new Page<>(current, size);
        String name = findCategoryPageListReqVO.getName();
        LocalDate startDate = findCategoryPageListReqVO.getStartDate();
        LocalDate endDate = findCategoryPageListReqVO.getEndDate();
        LambdaQueryWrapper<CategoryDO> wrapper = Wrappers.lambdaQuery(CategoryDO.class)
                .like(StringUtils.isNotBlank(name), CategoryDO::getName, name.trim()) // like 模块查询
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate) // 大于等于 startDate
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)  // 小于等于 endDate
                .orderByDesc(CategoryDO::getCreateTime);// 按创建时间倒叙
        Page<CategoryDO> categoryDOPage = categoryMapper.selectPage(page, wrapper);
        List<CategoryDO> records = categoryDOPage.getRecords();
        List<FindCategoryPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(records)) {
            vos = records.stream()
                    .map(item -> FindCategoryPageListRspVO.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .createTime(item.getCreateTime())
                            .articlesTotal(item.getArticlesTotal())
                            .build()
                    ).collect(Collectors.toList());
        }
        return PageResponse.success(categoryDOPage, vos);
    }

    @Override
    public Response editCategory(EditCategoryReqVO editCategoryReqVO) {
        String categoryName = editCategoryReqVO.getName();
        CategoryDO categoryDO = categoryMapper.selectByName(categoryName);
        if (ObjectUtil.isNotNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }
        // 更新标签
        categoryMapper.updateById(CategoryDO.builder()
                .id(editCategoryReqVO.getCategoryId())
                .name(categoryName)
                .updateTime(LocalDateTime.now())
                .build());
        return Response.success();
    }

    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        // 分类 ID
        Long categoryId = deleteCategoryReqVO.getId();

        // 校验该分类下是否已经有文章，若有，则提示需要先删除分类下所有文章，才能删除
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOneByCategoryId(categoryId);

        if (Objects.nonNull(articleCategoryRelDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_CAN_NOT_DELETE);
        }

        // 删除分类
        categoryMapper.deleteById(categoryId);

        return Response.success();
    }

    @Override
    public Response findCategorySelectList() {
        List<CategoryDO> categoryDOS = categoryMapper.selectList(null);
        List<SelectRspVO> selectRspVOS = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            // 将分类 ID 作为 Value 值，将分类名称作为 label 展示
            selectRspVOS = categoryDOS.stream()
                    .map(categoryDO -> SelectRspVO.builder()
                            .label(categoryDO.getName())
                            .value(categoryDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(selectRspVOS);
    }
}
