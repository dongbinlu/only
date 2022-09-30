package com.only.multids.exception;

import com.only.multids.enumuration.MultiDsErrorEnum;
import lombok.Data;

/**
 * 路由key 为空异常
 */
@Data
public class RoutingFiledArgsIsNull extends MultiDsError {

    public RoutingFiledArgsIsNull(MultiDsErrorEnum multiDsErrorEnum) {
        super();
        setErrorCode(MultiDsErrorEnum.ROUTINGFIELD_ARGS_ISNULL.getCode());
        setErrorMsg(MultiDsErrorEnum.ROUTINGFIELD_ARGS_ISNULL.getMsg());

    }
}
