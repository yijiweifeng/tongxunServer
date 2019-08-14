package com.lt;

import com.lt.domain.bean.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在Application.java同级或子包中创建SwaggerConfig.java
 */
@Configuration
@ComponentScan(basePackages = {"com.lt.web.controller"})
@EnableSwagger2
public class Swagger2 {

    boolean test=true;

    String system_cnname="IM";
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .genericModelSubstitutes(new Class[]{JsonResult.class})
                .useDefaultResponseMessages(false)
                .enable(this.test);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title( "IM接口文档")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}