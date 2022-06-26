package com.lzx.springsecuritystudy.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
 * @date 2022/06/23 下午 10:16
 * @description:
 */
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        result.put("msg","注销成功");
        result.put("data",authentication);
        response.setContentType("application/json;charset=utf-8");
        log.info("注销成功:{}",JSON.toJSONString(result,true));
        try {
            response.getWriter().println(JSON.toJSONString(result,true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
