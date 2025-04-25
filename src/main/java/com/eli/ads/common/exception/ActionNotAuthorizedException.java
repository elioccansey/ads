package com.eli.ads.common.exception;

public class ActionNotAuthorizedException extends RuntimeException {
    public ActionNotAuthorizedException(String s) {
        super(s);
    }
}
