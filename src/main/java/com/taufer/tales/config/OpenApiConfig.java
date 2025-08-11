package com.taufer.tales.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class OpenApiConfig {
  @Bean GroupedOpenApi api(){ return GroupedOpenApi.builder().group("taufer-tales").pathsToMatch("/api/**").build(); }
  @Bean io.swagger.v3.oas.models.OpenAPI meta(){ return new io.swagger.v3.oas.models.OpenAPI().info(new Info().title("TauferTales API").version("0.1.0")); }
}
