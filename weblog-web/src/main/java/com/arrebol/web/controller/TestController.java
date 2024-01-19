package com.arrebol.web.controller;

import com.arrebol.common.aspect.ApiOperationLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@RestController
public class TestController {
    @GetMapping("/test")
    @ApiOperationLog(description = "测试接口")
    public String test() {
        // 返参
        return "hello world";
    }
}
