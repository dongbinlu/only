package com.only.multids.exception;

import com.only.multids.enumuration.MultiDsErrorEnum;

/**
 * 入参中没有包含路由字段异常
 * Created by 爆裂无球 on 2019/4/18.
 */

public class ParamsNotContainsRoutingField extends MultiDsError {


    public ParamsNotContainsRoutingField(MultiDsErrorEnum paramsNotContainsRouting) {
        super();
        setErrorCode(MultiDsErrorEnum.PARAMS_NOT_CONTAINS_ROUTINGFIELD.getCode());
        setErrorMsg(MultiDsErrorEnum.PARAMS_NOT_CONTAINS_ROUTINGFIELD.getMsg());

    }
}
