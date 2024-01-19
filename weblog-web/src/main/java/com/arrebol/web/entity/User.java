package com.arrebol.web.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@Data
@ApiModel(value = "用户实体类")
public class User {

    @NotBlank
    private String username;

    @NotBlank
    private String sex;

    @Email
    private String email;

    // 创建时间
    private LocalDateTime createTime;
    // 更新日期
    private LocalDate updateDate;
    // 时间
    private LocalTime time;

}
