package com.arrebol.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Plus 配置文件
 *
 * @author Arrebol
 * @date 2024/1/21
 */
@Configuration
@MapperScan("com.arrebol.common.domain.mapper")
public class MybatisPlusConfig {
}
