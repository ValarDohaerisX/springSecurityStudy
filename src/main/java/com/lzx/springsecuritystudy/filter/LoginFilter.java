package com.lzx.springsecuritystudy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/26 下午 11:56
 * @description:
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

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


        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,String> req = objectMapper.readValue(request.getInputStream(),Map.class);
            final String username = req.get(getUsernameParameter());
            final String password = req.get(getPasswordParameter());
            System.out.println("用户名：" + username + " , 密码：" + password);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
