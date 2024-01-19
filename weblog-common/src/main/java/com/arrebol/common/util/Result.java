package com.arrebol.common.util;

import lombok.Data;

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
        Result<T> Result = new Result<>();
        return Result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> Result = new Result<>();
        Result.setData(data);
        return Result;
    }

    // =================================== 失败响应 ===================================
    public static <T> Result<T> fail() {
        Result<T> Result = new Result<>();
        Result.setSuccess(false);
        return Result;
    }

    public static <T> Result<T> fail(String errorMessage) {
        Result<T> Result = new Result<>();
        Result.setSuccess(false);
        Result.setMessage(errorMessage);
        return Result;
    }

    public static <T> Result<T> fail(String errorCode, String errorMessage) {
        Result<T> Result = new Result<>();
        Result.setSuccess(false);
        Result.setErrorCode(errorCode);
        Result.setMessage(errorMessage);
        return Result;
    }
}
