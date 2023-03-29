package com.safecode.security.subject.exception;

public class UserNotExistException extends RuntimeException {

    private static final long serialVersionUID = 3712820937187252798L;

    public UserNotExistException() {
        super("用户不存在");
    }
}
