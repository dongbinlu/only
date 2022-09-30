package com.only.multids.exception;

import com.only.multids.enumuration.MultiDsErrorEnum;

/**
 * 格式化表后缀名称异常
 */
public class FormatTableSuffixException extends MultiDsError {

    public FormatTableSuffixException(MultiDsErrorEnum formatTableSuffixError) {
        super();
        setErrorCode(MultiDsErrorEnum.FORMAT_TABLE_SUFFIX_ERROR.getCode());
        setErrorMsg(MultiDsErrorEnum.FORMAT_TABLE_SUFFIX_ERROR.getMsg());

    }
}
