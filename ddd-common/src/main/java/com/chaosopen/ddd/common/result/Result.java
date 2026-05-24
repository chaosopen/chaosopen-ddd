package com.chaosopen.ddd.common.result;

import com.chaosopen.ddd.common.enums.ErrorCode;

public class Result<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.success = true;
        result.code = ErrorCode.SUCCESS.name();
        result.message = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<T>();
        result.success = false;
        result.code = ErrorCode.FAILED.name();
        result.message = message;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
