package com.wjc.config;

import com.google.common.collect.Lists;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/3/17--8:43
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean(value="defaultApi2")
    public Docket webAPI(){
        return createDocket("wjc","com.wjc.controller");
    }

    public Docket createDocket(String groupName, String basePackage) {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                //将日期时间类型全部转为String类型
                .directModelSubstitute(LocalDateTime.class, String.class)
                //将日期类型全部转为String类型
                .directModelSubstitute(LocalDate.class, String.class)
                //将时间类型全部转为String类型
                .directModelSubstitute(LocalTime.class, String.class)
                //分组名称
                .groupName(groupName)
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(generateRequestParameters());
        return docket;
    }

    private List<RequestParameter> generateRequestParameters() {
        RequestParameterBuilder token = new RequestParameterBuilder();
        RequestParameterBuilder language = new RequestParameterBuilder();
        List<RequestParameter> parameters = Lists.newArrayList();
        token.name("Authorization").description("token令牌").in(In.HEADER.toValue()).required(true).build();
        language.name("language").description("语言").in(In.HEADER.toValue()).required(false).build();
        parameters.add(token.build());
        parameters.add(language.build());
        return parameters;

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
