package com.lzx.springsecuritystudy.conf;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger2配置
 *
 * @author huang cheng
 * 2021/8/13
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /*引入Knife4j提供的扩展类*/
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public Swagger2Config(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    private final static String groupName = "cheng";//组群名称

    private final static String headerName = "Authorization";//需要swagger每次调接口前携带的头信息的key
    //private final static String headerName2 = "test";//如果要多个请求头信息，自行解放注释

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//文档信息
                .groupName(groupName)//组群名称
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lzx.springsecuritystudy"))//需要扫描的api所在目录
                .paths(PathSelectors.any())//匹配全部地址路径
                .build()
                .securitySchemes(securitySchemes())//配置安全方案
                .securityContexts(securityContexts())//配置安全方案所实现的上下文
                .extensions(openApiExtensionResolver.buildExtensions(groupName))//赋予插件体系
                ;
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        //配置header头1
        ApiKey token_access = new ApiKey(headerName, headerName, "header");
        apiKeyList.add(token_access);
        //配置header头2
        //ApiKey token_access2 = new ApiKey( headerName2, headerName2, "header");
        //apiKeyList.add(token_access2);
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        //为每个api添加请求头
        securityReferenceList.add(new SecurityReference(headerName, scopes()));
        //以此类推
        //securityReferenceList.add(new SecurityReference(headerName2, scopes()));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .forPaths(PathSelectors.any())
                .build()
        );
        return securityContextList;
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")};//作用域为全局
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger2文档")
                .description("更多精彩博客请关注：https://blog.csdn.net/qq_42495847?spm=1000.2115.3001.5343")
                .termsOfServiceUrl("https://blog.csdn.net/qq_42495847?spm=1000.2115.3001.5343")
                .contact(new Contact("cheng", "https://blog.csdn.net/qq_42495847?spm=1000.2115.3001.5343", "1003816735@qq.com"))
                .version("1.0")
                .build();
    }

}
