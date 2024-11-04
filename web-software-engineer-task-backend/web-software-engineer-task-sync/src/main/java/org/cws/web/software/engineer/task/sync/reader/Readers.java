package org.cws.web.software.engineer.task.sync.reader;

import java.util.HashMap;
import java.util.Map;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.service.GithubUsersService;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class Readers {

	private EntityManagerFactory entityManagerFactory;

	private GithubUsersService githubUsersService;

	private int maxUserCount;

	private int userPageSize;

	public Readers(GithubUsersService githubUsersService, @Value("${cws.github.user.page.size:100}") int userPageSize,
			@Value("${cws.github.user.count.max:1000}") int maxUserCount, EntityManagerFactory entityManagerFactory) {
		this.githubUsersService = githubUsersService;
		this.userPageSize = userPageSize;
		this.maxUserCount = maxUserCount;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Bean
	@JobScope
	ItemStreamReader<GithubUserDTO> userReader() {
		SynchronizedItemStreamReader<GithubUserDTO> synchReader = new SynchronizedItemStreamReader<>();

		synchReader.setDelegate(new GithubUserReader(githubUsersService, userPageSize, maxUserCount));

		return synchReader;
	}

	@Bean
	@StepScope
	ItemStreamReader<Long> userToDeletionReader(@Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
		SynchronizedItemStreamReader<Long> syncReader = new SynchronizedItemStreamReader<>();

		Long modificationId = jobExecution.getId();
		LoggerFactory.getLogger(ItemStreamReader.class).info("modification id: {}", modificationId);
		Map<String, Object> params = new HashMap<>();
		params.put("modificationId", modificationId);
		//@formatter:off
        syncReader.setDelegate(new JpaPagingItemReaderBuilder<Long>()
                .name("userToDeletionReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select distinct u.id from GithubUser u where u.modificationId < :modificationId")
                .parameterValues(params)
                .build());
        //@formatter:on
		return syncReader;
	}
}
