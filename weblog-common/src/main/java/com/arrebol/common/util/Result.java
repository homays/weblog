package com.arrebol.common.util;

import com.arrebol.common.exception.BaseExceptionInterface;
import com.arrebol.common.exception.BizException;
import lombok.Data;

import javax.xml.ws.Response;
import java.io.Serializable;

/**
 * 结果响应类
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@Data
public class Result<T> implements Serializable {

    // 是否成功，默认为 true
    private boolean success = true;
    // 响应消息
    private String message;
    // 异常码
    private String errorCode;
    // 响应数据
    private T data;

    // =================================== 成功响应 ===================================
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    // =================================== 失败响应 ===================================
    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> fail(String errorMessage) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setMessage(errorMessage);
        return result;
    }

    public static <T> Result<T> fail(String errorCode, String errorMessage) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setMessage(errorMessage);
        return result;
    }

    public static <T> Result<T> fail(BizException bizException) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrorCode(bizException.getErrorCode());
        result.setMessage(bizException.getErrorMessage());
        return result;
    }

    public static <T> Result<T> fail(BaseExceptionInterface baseExceptionInterface) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrorCode(baseExceptionInterface.getErrorCode());
        result.setMessage(baseExceptionInterface.getErrorMessage());
        return result;
    }
}
