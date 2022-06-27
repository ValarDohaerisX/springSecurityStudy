package com.lzx.springsecuritystudy.filter;

import com.baomidou.kaptcha.Kaptcha;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzx.springsecuritystudy.exception.KaptchaNotMatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * author: lizhixin
 * description: 自定义验证码Filter
 * date: 2022/6/27 10:18
 */
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {
    private String kaptchaParameter;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    @Autowired
    private Kaptcha kaptcha;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (!request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException(
                    "Authentication Data Type not supported: " + request.getContentType());
        }

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> req = objectMapper.readValue(request.getInputStream(), Map.class);
            final String username = req.get(getUsernameParameter());
            final String password = req.get(getPasswordParameter());
            final String code = req.get(getKaptchaParameter());
            String kaptcha_session = (String) request.getSession().getAttribute(getKaptchaParameter());
            if (Objects.isNull(code) || Objects.isNull(kaptcha_session)){
                throw new KaptchaNotMatchException("验证码不存在！");
            }
            if (!kaptcha_session.isEmpty() && !code.isEmpty() && kaptcha.validate(code, 60)) {
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        username, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new KaptchaNotMatchException("验证码不匹配！");
    }
}
