package com.lzx.springsecuritystudy.controller;

import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.kaptcha.exception.*;
import com.lzx.springsecuritystudy.exception.KaptchaNotMatchException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * author: lizhixin
 * description: VerifyController
 * date: 2022/6/27 10:06
 */
@RestController
@Api(tags = "验证码controller")
@RequestMapping("/verfyCode")
public class VerifyController {
//    private final Producer producer;

//    @Autowired
//    public VerifyController(Producer producer) {
//        this.producer = producer;
//    }

    @Autowired
    private Kaptcha kaptcha;

    @GetMapping("/render")
    @ApiOperation(value = "获取图形验证码", notes = "图形验证")
    public void render() {
        kaptcha.render();
    }

    @PostMapping("/valid")
    @ApiOperation(value = "图形验证码验证", notes = "图形验证")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "code", value = "输入验证码", dataType = "String")})
    public Object validDefaultTime(@RequestParam String code) {
        boolean validate = false;
        String errMsg = "";
        try {
            validate = kaptcha.validate(code);
        } catch (KaptchaIncorrectException e) {
            errMsg = "验证码错误！";
        } catch (KaptchaNotFoundException e) {
            errMsg = "验证码不存在！";
        } catch (KaptchaRenderException e) {
            errMsg = "验证码生成异常！";
        } catch (KaptchaTimeoutException e) {
            errMsg = "验证码超时！";
        } finally {
            if (!StringUtils.isEmpty(errMsg)) {
                throw new KaptchaNotMatchException(errMsg);
            }
            return validate;
        }
    }


    @PostMapping("/validTime")
    @ApiOperation(value = "图形验证码验证，有效期60秒", notes = "图形验证")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "code", value = "输入验证码", dataType = "String")})
    public Object validWithTime(@RequestParam String code) throws KaptchaException {
        boolean validate = false;
        String errMsg = "";
        try {
            validate = kaptcha.validate(code, 60);
        } catch (KaptchaIncorrectException e) {
            errMsg = "验证码错误！";
        } catch (KaptchaNotFoundException e) {
            errMsg = "验证码不存在！";
        } catch (KaptchaRenderException e) {
            errMsg = "验证码生成异常！";
        } catch (KaptchaTimeoutException e) {
            errMsg = "验证码超时！";
        } finally {
            if (!StringUtils.isEmpty(errMsg)) {
                throw new KaptchaNotMatchException("验证码校验错误！");
            }
            return validate;
        }
    }

    @GetMapping("/vc.jpg")
    public String getVerifyCode(HttpSession session) throws IOException {
        /**
         * 1.生成验证码
         * 2.放入redis/session
         * 3.生成图片
         * 4.图片转base64
         */
//        String text = producer.createText();
//        session.setAttribute("kaptcha", text);
//        BufferedImage bi = producer.createImage(text);
//        FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
//        ImageIO.write(bi, "jpg", fbaos);
//        Map<String, Object> result = new HashMap<>();
//        result.put("code", 200);
//        result.put("result", Base64.encodeBase64String(fbaos.toByteArray()));
//
//
//        return JSON.toJSONString(result, false);
        return "";
    }
}
