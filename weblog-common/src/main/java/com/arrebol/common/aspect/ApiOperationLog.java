package com.arrebol.common.aspect;

import java.lang.annotation.*;

/**
 * Aop日志自定义注解
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {

    /**
     * API 功能描述
     */
    String description() default "";

}
