package com.safecode.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 6654540693781264204L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
