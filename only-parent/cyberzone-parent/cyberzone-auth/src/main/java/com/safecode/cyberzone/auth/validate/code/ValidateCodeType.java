package com.safecode.cyberzone.auth.validate.code;

import com.safecode.cyberzone.auth.properties.SecurityConstants;

/**
 * 校验码类型
 *
 * @author v_boy
 */

public enum ValidateCodeType {
    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }

    },
    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }

    };

    /**
     * 校验时从请求中获取的参数名字
     *
     * @return
     */
    public abstract String getParamNameOnValidate();

}
