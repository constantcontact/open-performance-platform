package com.opp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by ctobe on 6/13/16.
 */
@Configuration
@EnableSwagger2
@ComponentScan("com.opp")
public class SwaggerConfiguration {

    /*
       swagger implementation docs
       http://springfox.github.io/springfox/docs/current/
     */
    @Bean
    public Docket oppApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("opp-apis")
                .apiInfo(apiInfo())
                .select()
               // .paths(regex("/api/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Open Performance Platform API Docs")
                .description("APIs for accessing OPP")
                // .termsOfServiceUrl("")
                .contact(new Contact("OPP Team", "yeah you know me", ""))
                //  .license("Performance Team")
                //.licenseUrl("https://wiki.roving.com/display/EngDev/Performance+Team+Home")
                .version("1.0")
                .build();
    }
}
