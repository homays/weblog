package com.arrebol.common.exception;

import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.Optional;

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

    /**
     * 捕获参数校验异常
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Result<Object> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        // 参数错误异常码
        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        // 获取 BindingResult
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder sb = new StringBuilder();

        // 获取校验不通过的字段，并组合错误信息，格式为： email 邮箱格式不正确, 当前值: '123124qq.com';
        Optional.ofNullable(bindingResult.getFieldErrors()).ifPresent(errors -> {
            errors.forEach(error ->
                    sb.append(error.getField())
                            .append(" ")
                            .append(error.getDefaultMessage())
                            .append(", 当前值: '")
                            .append(error.getRejectedValue())
                            .append("'; ")

            );
        });

        // 错误信息
        String errorMessage = sb.toString();

        log.warn("{} request error, errorCode: {}, errorMessage: {}", request.getRequestURI(), errorCode, errorMessage);

        return Result.fail(errorCode, errorMessage);
    }

}
