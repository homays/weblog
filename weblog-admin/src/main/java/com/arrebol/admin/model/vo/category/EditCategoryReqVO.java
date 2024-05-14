package com.arrebol.admin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "编辑分类 VO")
public class EditCategoryReqVO {

    @NotNull(message = "分类 ID 不能为空")
    private Long categoryId;

    @NotEmpty(message = "分类名称 不能为空")
    private String name;

}