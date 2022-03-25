package com.wjc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author 王建成
 * @date 2022/3/17--8:43
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger2.enable}")
    private boolean enable = false;
    @Bean
    public Docket docket(Environment environment){


        return  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("wjc")
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wjc"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfo("万无疫失接口文档",
                "万无疫失所有api接口",
                "1.0",
                "https:blog.csd.net/LuLuke_lucky",
                new Contact("luluke","未知","1959389778@qq.com"),
                "Apache 2.0",
                "http://wwww.apache.org/license/LICENSE-2.0",
                new ArrayList<VendorExtension>());

    }

}
