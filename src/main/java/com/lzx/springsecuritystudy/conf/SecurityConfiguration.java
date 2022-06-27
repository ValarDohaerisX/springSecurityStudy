package com.lzx.springsecuritystudy.conf;

import com.google.code.kaptcha.Constants;
import com.lzx.springsecuritystudy.filter.LoginKaptchaFilter;
import com.lzx.springsecuritystudy.handler.MyAuthenticationFailureHandler;
import com.lzx.springsecuritystudy.handler.MyAuthenticationSuccessHandler;
import com.lzx.springsecuritystudy.handler.MyLogoutSuccessHandler;
import com.lzx.springsecuritystudy.manager.MyUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/21 下午 10:04
 * @description:
 */
@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public LoginKaptchaFilter loginFilter() throws Exception {
        final LoginKaptchaFilter filter = new LoginKaptchaFilter();
        filter.setFilterProcessesUrl("/doLogin");
        filter.setUsernameParameter("uname");
        filter.setPasswordParameter("pwd");
        filter.setKaptchaParameter(Constants.KAPTCHA_SESSION_KEY);
        //指定默认的认证管理器AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        //指定认证成功的处理
        filter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        //指定认证失败的处理
        filter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return filter;
    }

    private MyUserDetailService myUserDetailService;

    @Autowired
    protected SecurityConfiguration(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    /**
     * 默认自定义全局AuthenticationManager，替换掉原有{@link org.springframework.security.authentication.AuthenticationManager}
     * configure方法必须进行注入，SpringSecurity不能够自动装配configure()没有set的bean,SpringSecurity自动装配的方法可以自动注入@Bean如果声明UserDetailsService这个Bean的话。
     * 如{@link org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration}
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("自定义AuthenticationManager：{}", auth);
        //userDetailService 校验用户数据源数据
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .mvcMatchers("/login.html")
//                .permitAll()
                .mvcMatchers(
                        "/index",
                        "/verfyCode/**",
                        "/swagger-ui.html/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config",
                        "/webjars/**",
                        "/doc.html",
                        "/favicon.ico"
                )  //放行index所有请求（放行资源放在任何请求之前！）
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    if (ex instanceof InsufficientAuthenticationException){
                        res.setStatus(HttpStatus.UNAUTHORIZED.value());
                        log.error("用户访问未认证请求地址：{}",req.getRequestURL().toString());
                        res.getWriter().print("请完成认证后再操作！");
                    }else {
                        res.getWriter().print(ex.getMessage());
                    }

                })
                .and()
                .formLogin()
//                .loginPage("/login.html")
//                .loginProcessingUrl("/doLogin") //指定处理登录请求Url
//                .usernameParameter("uname")
//                .passwordParameter("pwd")
                //前后端分离登录成功的解决方案
//                .successHandler(new MyAuthenticationSuccessHandler())
                //前后端分离登录失败的解决方案
//                .failureHandler(new MyAuthenticationFailureHandler())
                .and()
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/out1", HttpMethod.GET.name()),
                        new AntPathRequestMatcher("/out2", HttpMethod.POST.name())
                ))
                //非前后端分离退出登录成功的解决方案
                //.logoutUrl("/out") //转发，放在session作用域处理
                //.logoutSuccessUrl("/login.html") //重定向，放在request作用域处理
                //非前后端分离退出登录失败的解决方案
                //.failureForwardUrl("/login.html"); //转发，放在session作用域处理
                //.failureUrl("/login.html"); //重定向，放在request作用域处理
                //前后端分离退出登录解决方案
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                .csrf().disable();
    }
}
