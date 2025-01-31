package org.cws.web.software.engineer.task.sync.processor;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class Processors {

    @Bean("asyncUserProcessor")
    @StepScope
    AsyncItemProcessor<GithubUserDTO, GithubUser> asyncUserProcessor(GithubUserRepository githubUserRepository,
            @Value("#{stepExecution.jobExecution}") JobExecution jobExecution, @Autowired @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        AsyncItemProcessor<GithubUserDTO, GithubUser> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(new GithubUserProcessor(githubUserRepository, jobExecution.getId()));
        asyncItemProcessor.setTaskExecutor(taskExecutor);

        return asyncItemProcessor;
    }
}
