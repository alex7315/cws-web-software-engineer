package org.cws.web.software.engineer.task.sync.processor;

import java.time.Instant;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.constants.ContextParameterConstants;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Processors {

    @Bean
    @StepScope
    ItemProcessor<GithubUserDTO, GithubUser> userProcessor(GithubUserRepository githubUserRepository,
            @Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
        return new GithubUserProcessor(githubUserRepository,
                jobExecution.getExecutionContext().get(ContextParameterConstants.CONTEXT_PARAM_MODIFICATION_TIMESTAMP, Instant.class));

    }
}
