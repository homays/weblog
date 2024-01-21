package com.arrebol.web.controller;

import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.domain.dos.UserDO;
import com.arrebol.common.util.JsonUtil;
import com.arrebol.common.util.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@RestController
@Slf4j
public class TestController {

    @PostMapping("/admin/test")
    @ApiOperationLog(description = "测试接口")
    @ApiOperation(value = "测试接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response test(@RequestBody @Validated UserDO user) {
        // 打印入参
        log.info(JsonUtil.toJsonString(user));

        // 设置三种日期字段值


        return Response.success(user);
    }

}
