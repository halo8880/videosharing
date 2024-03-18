package com.example.videoauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

//@Configuration
public class CorsConfig {
	@Bean
	public CorsConfiguration corsConfiguration(CorsConfiguration corsConfiguration) {
		corsConfiguration.setAllowedOrigins(List.of("*"));
		corsConfiguration.setAllowCredentials(false);
		corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		corsConfiguration.setAllowedHeaders(List.of("authorization", "content-type", "xsrf-token"));
		corsConfiguration.setExposedHeaders(List.of("xsrf-token"));
		corsConfiguration.setMaxAge(3600L);
		return corsConfiguration;
	}
}
