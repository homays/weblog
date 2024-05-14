package com.arrebol.admin.controller;

import com.arrebol.admin.model.vo.category.AddCategoryReqVO;
import com.arrebol.admin.model.vo.category.DeleteCategoryReqVO;
import com.arrebol.admin.model.vo.category.EditCategoryReqVO;
import com.arrebol.admin.model.vo.category.FindCategoryPageListReqVO;
import com.arrebol.admin.model.vo.tag.EditTagReqVO;
import com.arrebol.admin.service.AdminCategoryService;
import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Resource
    private AdminCategoryService categoryService;

    @PostMapping("/add")
    @ApiOperation(value = "添加分类")
    @ApiOperationLog(description = "添加分类")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        return categoryService.addCategory(addCategoryReqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分类分页数据获取")
    @ApiOperationLog(description = "分类分页数据获取")
    public PageResponse findCategoryList(@RequestBody @Validated FindCategoryPageListReqVO findCategoryPageListReqVO) {
        return categoryService.findCategoryList(findCategoryPageListReqVO);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑分类")
    @ApiOperationLog(description = "编辑分类")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response editCategory(@RequestBody @Validated EditCategoryReqVO editCategoryReqVO) {
        return categoryService.editCategory(editCategoryReqVO);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除分类")
    @ApiOperationLog(description = "删除分类")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO) {
        return categoryService.deleteCategory(deleteCategoryReqVO);
    }

    @PostMapping("/select/list")
    @ApiOperation(value = "分类 Select 下拉列表数据获取")
    @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
    public Response findCategorySelectList() {
        return categoryService.findCategorySelectList();
    }

}
