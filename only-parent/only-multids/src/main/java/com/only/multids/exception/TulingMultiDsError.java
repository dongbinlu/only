package com.only.multids.exception;

import lombok.Data;

/**
 * 所有异常的父类
 */
@Data
public class TulingMultiDsError extends RuntimeException {

    private Integer errorCode;

    private String errorMsg;
}
