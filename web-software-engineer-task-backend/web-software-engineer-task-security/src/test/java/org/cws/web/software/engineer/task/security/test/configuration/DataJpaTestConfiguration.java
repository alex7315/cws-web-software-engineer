package org.cws.web.software.engineer.task.security.test.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({ "org.cws.web.software.engineer.task.security.service" })
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
@ComponentScan({ "org.cws.web.software.engineer.task.security.service", "org.cws.web.software.engineer.task.security.jwt" })
public class DataJpaTestConfiguration {

}
