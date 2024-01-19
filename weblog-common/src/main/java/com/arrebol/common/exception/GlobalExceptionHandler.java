package com.arrebol.common.exception;

import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 其他类型异常
     */
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public Result<Object> handleOtherException(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        return Result.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 捕获自定义业务异常
     */
    @ExceptionHandler({ BizException.class })
    public Result<Object> handleBizException(HttpServletRequest request, BizException e) {
        log.warn("{} request fail, errorCode: {}, errorMessage: {}", request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Result.fail(e);
    }

}
