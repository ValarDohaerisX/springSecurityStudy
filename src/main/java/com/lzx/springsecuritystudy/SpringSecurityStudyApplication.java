package com.lzx.springsecuritystudy;

import com.lzx.springsecuritystudy.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@MapperScan(basePackages = "com.lzx.springsecuritystudy.dao")
public class SpringSecurityStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityStudyApplication.class, args);
    }
}

@Slf4j
@RestController
class HelloController {
    @GetMapping("hello")
    public String print() {
        //1.获取认证信息
        SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        final User user = (User) authentication.getPrincipal();
        log.info("身份信息：{}", user.getUsername());
        log.info("权限信息：{}", authentication.getAuthorities());
        new Thread(() -> {
            SecurityContext context = SecurityContextHolder.getContext();
            final Authentication authentication1 = context.getAuthentication();
            final User user2 = (User) authentication1.getPrincipal();
            log.info("身份信息2：{}", user2.getUsername());
            log.info("权限信息2：{}", authentication1.getAuthorities());
        }).start();

        return "hello springSecurity";
    }
}

@RestController
class IndexController {
    @GetMapping("index")
    public String print() {
        return "index springSecurity";
    }
}

@Slf4j
@Controller
class LoginController {
    @RequestMapping("/login.html")
    public String print() {
        return "login";
    }

}
