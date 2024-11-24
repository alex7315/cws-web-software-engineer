package org.cws.web.software.engineer.task.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableTask
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
public class WebSoftwareEngineerTaskSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSoftwareEngineerTaskSyncApplication.class, args);
	}

}
