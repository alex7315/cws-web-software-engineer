package org.cws.web.software.engineer.task.sync.reader;

import static org.cws.web.software.engineer.task.sync.constants.ContextParameterConstants.CONTEXT_PARAM_MODIFICATION_TIMESTAMP;

import java.time.Instant;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class Readers {

    private ItemStreamReader<GithubUser> githubUserReader;

    public Readers(ItemStreamReader<GithubUser> githubUserReader) {
        this.githubUserReader = githubUserReader;
    }

    @Bean
    ItemReader<GithubUser> userReader() {
        SynchronizedItemStreamReader<GithubUser> synchReader = new SynchronizedItemStreamReader<>();

        synchReader.setDelegate(githubUserReader);

        return synchReader;
    }

    @Bean
    @StepScope
    public ItemReader<Long> userToDeletionReader(EntityManagerFactory entityManagerFactory, @Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
        SynchronizedItemStreamReader<Long> syncReader = new SynchronizedItemStreamReader<>();
        Instant modificationTimestamp = (Instant) jobExecution.getExecutionContext().get(CONTEXT_PARAM_MODIFICATION_TIMESTAMP);
        syncReader.setDelegate(new JpaPagingItemReaderBuilder<Long>().name("userToDeletionReader").entityManagerFactory(entityManagerFactory)
                .queryString("select distinct u.id from GithubUser u where u.modifiedAt < " + modificationTimestamp).build());
        return syncReader;
    }
}
