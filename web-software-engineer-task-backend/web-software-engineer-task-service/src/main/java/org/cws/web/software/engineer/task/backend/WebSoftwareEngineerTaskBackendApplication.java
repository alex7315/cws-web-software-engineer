package org.cws.web.software.engineer.task.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WebSoftwareEngineerTaskBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSoftwareEngineerTaskBackendApplication.class, args);
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				//@formatter:off
                registry
                    .addMapping("/**")
                    .allowedOrigins("*"); 
                //@formatter:on
			}
		};
	}

}
