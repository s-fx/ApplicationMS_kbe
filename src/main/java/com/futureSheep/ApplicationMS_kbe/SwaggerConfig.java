package com.futureSheep.ApplicationMS_kbe;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig {
    //funktioniert noch nicht richtig
    @Bean
    public Docket swaggerConfiguration() {
        //return a Docket instance that configures what is shown in swagger documentation
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                //.apis(RequestHandlerSelectors.basePackage("kbe_project"))
                .build();

    }

}


