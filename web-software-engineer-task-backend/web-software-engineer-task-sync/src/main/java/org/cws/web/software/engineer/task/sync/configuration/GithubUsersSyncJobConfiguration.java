package org.cws.web.software.engineer.task.sync.configuration;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.listener.CountChunkListener;
import org.cws.web.software.engineer.task.sync.listener.CountStepExecutionListener;
import org.cws.web.software.engineer.task.sync.processor.Processors;
import org.cws.web.software.engineer.task.sync.reader.Readers;
import org.cws.web.software.engineer.task.sync.writer.Writers;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
@Import({ Readers.class, Processors.class, Writers.class })
public class GithubUsersSyncJobConfiguration {

	@Bean
	//@formatter:off
    Job githubUsersSyncJob(JobRepository jobRepository,  
    		@Qualifier("createAndUpdateStep") Step createAndUpdateStep, 
    		@Qualifier("deletionStep") Step deletionStep) {    
        return new JobBuilder("githubUsersSyncJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(createAndUpdateStep)
                .next(deletionStep)
                .end()
                .build();
        //@formatter:on
	}

	@Bean
	Step createAndUpdateStep(@Qualifier("userReader") ItemReader<GithubUserDTO> userReader,
			@Qualifier("userProcessor") ItemProcessor<GithubUserDTO, GithubUser> userProcessor,
			@Qualifier("userWriter") ItemWriter<GithubUser> userWriter, JobRepository jobRepository,
			@Qualifier("transactionManager") PlatformTransactionManager transactionManager,
			@Qualifier("countChunkListener") ChunkListener countChunkListener,
			@Qualifier("countStepExecutionListener") CountStepExecutionListener countStepExecutionListener) {
		//@formatter:off
        return new StepBuilder("createAndUpdateStep", jobRepository)
                .<GithubUserDTO, GithubUser> chunk(100, transactionManager)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .allowStartIfComplete(true)
                .listener(countChunkListener)
                .listener(countStepExecutionListener)
                .build();
        //@formatter:on

	}

	@Bean
	Step deletionStep(@Qualifier("userToDeletionReader") ItemReader<Long> userToDeletionReader,
			@Qualifier("deletionUserWriter") ItemWriter<Long> deletionUserWriter, JobRepository jobRepository,
			@Qualifier("transactionManager") PlatformTransactionManager transactionManager,
			@Qualifier("countChunkListener") ChunkListener countChunkListener,
			@Qualifier("countStepExecutionListener") CountStepExecutionListener countStepExecutionListener) {
		//@formatter:off
        return new StepBuilder("deletionStep", jobRepository)
                .<Long, Long> chunk(100, transactionManager)
                .reader(userToDeletionReader)
                .writer(deletionUserWriter)
                .allowStartIfComplete(true)
                .listener(countChunkListener)
                .listener(countStepExecutionListener)
                .build();
        //@formatter:on
	}

	@Bean
	ChunkListener countChunkListener() {
		return new CountChunkListener(10);
	}

	@Bean
	CountStepExecutionListener countStepExecutionListener() {
		return new CountStepExecutionListener();
	}

}
