package org.cws.web.software.engineer.task.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
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
