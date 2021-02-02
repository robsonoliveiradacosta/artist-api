package br.gov.mt.pjc.artist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI artistOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Artist REST API").description("API REST sobre artistas e álbuns").version("1"));
	}

}
