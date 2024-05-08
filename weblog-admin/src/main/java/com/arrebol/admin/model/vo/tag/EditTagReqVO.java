package com.arrebol.admin.model.vo.tag;

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
@ApiModel(value = "编辑标签 VO")
public class EditTagReqVO {

    @NotNull(message = "标签 ID 不能为空")
    private Long tagId;

    @NotEmpty(message = "标签名称 不能为空")
    private String tagName;

}
