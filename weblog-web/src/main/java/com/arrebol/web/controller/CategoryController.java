package com.arrebol.web.controller;

import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.category.FindCategoryArticlePageListReqVO;
import com.arrebol.web.model.vo.category.FindCategoryListReqVO;
import com.arrebol.web.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/31
 */
@RestController
@RequestMapping("/category")
@Api(tags = "分类")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/list")
    @ApiOperation(value = "前台获取分类列表")
    @ApiOperationLog(description = "前台获取分类列表")
    public Response findCategoryList(@RequestBody @Validated FindCategoryListReqVO findCategoryListReqVO) {
        return categoryService.findCategoryList(findCategoryListReqVO);
    }

    @PostMapping("/article/list")
    @ApiOperation(value = "前台获取分类下文章分页数据")
    @ApiOperationLog(description = "前台获取分类下文章分页数据")
    public Response findCategoryArticlePageList(@RequestBody @Validated FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        return categoryService.findCategoryArticlePageList(findCategoryArticlePageListReqVO);
    }

}