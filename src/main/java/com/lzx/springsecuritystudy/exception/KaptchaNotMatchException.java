package com.lzx.springsecuritystudy.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * author: lizhixin
 * description: 自定义验证码异常类
 * date: 2022/6/27 10:30
 */
public class KaptchaNotMatchException extends AuthenticationException {

    public KaptchaNotMatchException(String msg) {
        super(msg);
    }

    public KaptchaNotMatchException(String msg, Throwable t) {
        super(msg, t);
    }
}
