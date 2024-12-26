package org.cws.web.software.engineer.task.sync.batch;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.cws.web.software.engineer.task.sync.configuration.GithubUsersSyncJobConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@EnableAutoConfiguration
@SpringJUnitConfig(GithubUsersSyncJobConfiguration.class)
@EnableJpaRepositories(basePackages = { "org.cws.web.software.engineer.task.persistence.repository" })
@EntityScan("org.cws.web.software.engineer.task.persistence.model")
@ComponentScan({ "org.cws.web.software.engineer.task.sync.service" })
@PropertySource({ "classpath:test-properties/test-github-users-sync-batch-h2.properties" })
class GithubUsersSynchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Autowired
    private DataSource           dataSource;

    private JdbcTemplate         jdbcTemplate;

    @Autowired
    private Job                  githubUsersSyncJob;

    @BeforeEach
    void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void shouldSuccessfulyExecuteJob() throws Exception {
        jobLauncherTestUtils.setJob(githubUsersSyncJob);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        Long githubUserCount = jdbcTemplate.queryForObject("select count(id) from github_user", Long.class);

        assertThat(githubUserCount).isEqualTo(10);
    }
}
