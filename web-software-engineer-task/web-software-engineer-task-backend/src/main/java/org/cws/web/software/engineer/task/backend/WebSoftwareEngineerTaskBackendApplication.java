package org.cws.web.software.engineer.task.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
public class WebSoftwareEngineerTaskBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSoftwareEngineerTaskBackendApplication.class, args);
	}

}
