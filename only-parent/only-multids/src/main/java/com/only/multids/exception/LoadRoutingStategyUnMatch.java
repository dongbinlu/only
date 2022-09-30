package com.only.multids.exception;

import com.only.multids.enumuration.MultiDsErrorEnum;
import lombok.Data;

/**
 * 加载路由策略和配置配置文件不匹配
 */
@Data
public class LoadRoutingStategyUnMatch extends MultiDsError {

    public LoadRoutingStategyUnMatch(MultiDsErrorEnum multiDsErrorEnum) {
        super();
        setErrorCode(MultiDsErrorEnum.LOADING_STATEGY_UN_MATCH.getCode());
        setErrorMsg(MultiDsErrorEnum.LOADING_STATEGY_UN_MATCH.getMsg());

    }
}
