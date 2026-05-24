package com.chaosopen.ddd.adapter.config;

import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Result<String> handleBizException(BizException ex) {
        return Result.fail(ex.getErrorCode().name() + ":" + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        return Result.fail(ex.getMessage());
    }
}
