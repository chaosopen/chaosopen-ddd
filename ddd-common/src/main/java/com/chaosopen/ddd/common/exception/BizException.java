package com.chaosopen.ddd.common.exception;

import com.chaosopen.ddd.common.enums.ErrorCode;

public class BizException extends RuntimeException {

    private final ErrorCode errorCode;

    public BizException(String message) {
        super(message);
        this.errorCode = ErrorCode.FAILED;
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
