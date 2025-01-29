package org.cws.web.software.engineer.task.sync.configuration;

import java.util.concurrent.Future;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
@Import({ Readers.class, Processors.class, Writers.class })
public class GithubUsersSyncJobConfiguration {

    private static final String GITHUB_USERS_SYNC_JOB  = "githubUsersSyncJob";

    private static final String THREAD_NAME_PREFIX     = "MultiThreaded-";

    public static final String  DELETION_STEP          = "deletionStep";
    public static final String  CREATE_AND_UPDATE_STEP = "createAndUpdateStep";

    private static final int    CHUNK_SIZE             = 100;
    private static final int    LOGGING_INTERVAL       = 10;

	@Bean
	//@formatter:off
    Job githubUsersSyncJob(JobRepository jobRepository,  
    		@Qualifier("createAndUpdateStep") Step createAndUpdateStep, 
    		@Qualifier("deletionStep") Step deletionStep) {    
        return new JobBuilder(GITHUB_USERS_SYNC_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(createAndUpdateStep)
                .next(deletionStep)
                .end()
                .build();
        //@formatter:on
	}

    @Bean
    //@formatter:off
    Step createAndUpdateStep(@Qualifier("userReader") ItemReader<GithubUserDTO> userReader,
            @Qualifier("asyncUserProcessor") ItemProcessor<GithubUserDTO, Future<GithubUser>> userProcessor,
            @Qualifier("asyncUserWriter") ItemWriter<Future<GithubUser>> asyncUserWriter, 
            JobRepository jobRepository,
            @Qualifier("transactionManager") PlatformTransactionManager transactionManager, 
            @Qualifier("countChunkListener") ChunkListener countChunkListener,
            @Qualifier("countStepExecutionListener") CountStepExecutionListener countStepExecutionListener) {
         
        return new StepBuilder(CREATE_AND_UPDATE_STEP, jobRepository)
                .<GithubUserDTO, Future<GithubUser>> chunk(CHUNK_SIZE, transactionManager)
                .reader(userReader)
                .processor(userProcessor)
                .writer(asyncUserWriter)
                .allowStartIfComplete(true)
                .listener(countChunkListener)
                .listener(countStepExecutionListener)
                .build();
        //@formatter:on

    }

    @Bean
    //@formatter:off
    Step deletionStep(@Qualifier("userToDeletionReader") ItemReader<Long> userToDeletionReader,
            @Qualifier("deletionUserWriter") ItemWriter<Long> deletionUserWriter, 
            JobRepository jobRepository,
            @Qualifier("transactionManager") PlatformTransactionManager transactionManager, 
            @Qualifier("countChunkListener") ChunkListener countChunkListener,
            @Qualifier("countStepExecutionListener") CountStepExecutionListener countStepExecutionListener) {    
        return new StepBuilder(DELETION_STEP, jobRepository)
                .<Long, Long> chunk(CHUNK_SIZE, transactionManager)
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
        return new CountChunkListener(LOGGING_INTERVAL);
	}

	@Bean
	CountStepExecutionListener countStepExecutionListener() {
		return new CountStepExecutionListener();
	}

    @Bean
    //@formatter:off
    TaskExecutor taskExecutor(@Value("${spring.task.execution.pool.core-size}") int corePoolSize,
            @Value("${spring.task.execution.pool.max-size}") int maxPoolSize, 
            @Value("${spring.task.execution.pool.queue-capacity}") int queueCapacity) {
        
        return new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(corePoolSize)
                .maxPoolSize(maxPoolSize)
                .queueCapacity(queueCapacity)
                .threadNamePrefix(THREAD_NAME_PREFIX)
                .build();
        //@formatter:on
    }

}
