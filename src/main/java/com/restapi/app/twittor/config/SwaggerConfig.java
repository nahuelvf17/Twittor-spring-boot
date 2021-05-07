package com.restapi.app.twittor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    //.apis(RequestHandlerSelectors.basePackage("com.restapi.app.twittor.Controller"))

	public static final String AUTHORIZATION_HEADER = "Authorization : Bearer";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

	
    @Bean
    public Docket exampleApi() {
    	ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .description("JWT token")
                .required(true)
                .build();
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(parameterBuilder.build());
    
    	
    	return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .securitySchemes(Collections.singletonList(new ApiKey("authorization", "authorization", "header")))
                .securityContexts(Collections.singletonList(securityContext()))
                .globalOperationParameters(parameters);
    }
    
    private ApiKey apiKey() {
        return new ApiKey("Authorization", AUTHORIZATION_HEADER, "header");
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot Swagger API TWITTOR",
                "Spring Boot Swagger for twittor app",
                "1.0",
                "Terms of Service",
                new Contact("yourname", "https://github.com/nahuelvf17",
                        "mibauti2021@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", Collections.emptyList()
        );

        return apiInfo;
    }
    
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/v1/api/.*")).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessNothing");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("authorization", authorizationScopes));
    }
}