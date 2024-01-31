package com.arrebol.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.arrebol.common.domain.dos.CategoryDO;
import com.arrebol.common.domain.mapper.CategoryMapper;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.category.FindCategoryListRspVO;
import com.arrebol.web.service.CategoryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Response findCategoryList() {
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
        List<FindCategoryListRspVO> vos = null;
        if (CollUtil.isNotEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(item -> BeanUtil.copyProperties(item, FindCategoryListRspVO.class)
                            ).collect(Collectors.toList());
        }
        return Response.success(vos);
    }
}
