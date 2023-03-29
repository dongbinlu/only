package com.safecode.cyberzone.authorize.exception;

@SuppressWarnings("serial")
public class AuthorizeException extends RuntimeException {

    public AuthorizeException() {
        super();
    }

    public AuthorizeException(String message) {
        super(message);
    }

    public AuthorizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizeException(Throwable cause) {
        super(cause);
    }

    protected AuthorizeException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
