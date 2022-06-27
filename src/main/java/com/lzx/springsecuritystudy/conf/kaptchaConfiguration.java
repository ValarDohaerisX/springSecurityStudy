package com.lzx.springsecuritystudy.conf;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * author: lizhixin
 * description: 验证码配置类
 * date: 2022/6/27 10:01
 */

@Configuration
public class kaptchaConfiguration {

    @Bean
    public Producer kaptcha(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","150");
        properties.setProperty("kaptcha.image.height","50");
        properties.setProperty("kaptcha.testproducer.char.string","0123456789");
        properties.setProperty("kaptcha.testproducer.char.length","4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;

    }
}
