package com.safecode.cyberzone.auth.validate.code;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 6654540693781264204L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
