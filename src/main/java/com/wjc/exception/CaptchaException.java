package com.wjc.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 王建成
 * @date 2022/5/22--12:20
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}
