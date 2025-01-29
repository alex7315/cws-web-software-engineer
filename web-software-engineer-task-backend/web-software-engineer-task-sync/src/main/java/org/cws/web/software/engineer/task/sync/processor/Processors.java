package org.cws.web.software.engineer.task.sync.processor;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class Processors {

    @Autowired
    TaskExecutor taskExecutor;

    @Bean("userProcessor")
    @StepScope
    ItemProcessor<GithubUserDTO, GithubUser> userProcessor(GithubUserRepository githubUserRepository,
            @Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
        return new GithubUserProcessor(githubUserRepository, jobExecution.getId());
    }

    @Bean("asyncUserProcessor")
    @StepScope
    AsyncItemProcessor<GithubUserDTO, GithubUser> asyncUserProcessor(GithubUserRepository githubUserRepository,
            @Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
        AsyncItemProcessor<GithubUserDTO, GithubUser> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(new GithubUserProcessor(githubUserRepository, jobExecution.getId()));
        asyncItemProcessor.setTaskExecutor(taskExecutor);

        return asyncItemProcessor;
    }
}
