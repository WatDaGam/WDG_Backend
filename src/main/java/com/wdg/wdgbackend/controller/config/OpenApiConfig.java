package com.wdg.wdgbackend.controller.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.title("왔다감 프로젝트 API Document")
				.version("v0.0.1");

		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("Bearer")
				.bearerFormat("JWT")
				.name("JWT Authentication");

		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
				.info(info)
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
}
