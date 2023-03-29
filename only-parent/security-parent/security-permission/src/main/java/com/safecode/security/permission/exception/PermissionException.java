package com.safecode.security.permission.exception;

public class PermissionException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7360537153514861862L;

    public PermissionException() {
        super();
    }

    public PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

}
