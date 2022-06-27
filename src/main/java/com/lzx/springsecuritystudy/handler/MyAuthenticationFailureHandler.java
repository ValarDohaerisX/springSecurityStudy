package com.lzx.springsecuritystudy.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/23 下午 09:21
 * @description:
 */
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        result.put("msg","登录失败");
        result.put("reason",exception.getMessage());
        response.setContentType("application/json;charset=utf-8");
        log.info("result:{}", JSON.toJSONString(result,true));
        response.getWriter().println(JSON.toJSONString(result,true));
    }
}
