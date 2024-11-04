package org.cws.web.software.engineer.task.sync.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class GithubUsersSyncJobScheduler {

    private final Logger                          logger          = LoggerFactory.getLogger(GithubUsersSyncJobScheduler.class);

    private AtomicBoolean                         enabled         = new AtomicBoolean(true);

    private AtomicInteger                         batchRunCounter = new AtomicInteger(0);

    private JobLauncher                           jobLauncher;

    private Job           githubUsersSyncJob;

    public GithubUsersSyncJobScheduler(JobLauncher jobLauncher, @Qualifier("githubUsersSyncJob") Job githubUsersSyncJob) {
        this.jobLauncher = jobLauncher;
        this.githubUsersSyncJob = githubUsersSyncJob;
    }

    @Scheduled(fixedRateString = "${cws.github.sync.scheduled.rate}", timeUnit = TimeUnit.SECONDS)
    public void launchJob() {
        logger.info("Scheduled job githubUsersSyncJob at {} Job start enabled: {}", LocalDateTime.now(), isEnabled());
        if (enabled.get()) {
            start(githubUsersSyncJob);
        }
        logger.debug("scheduler ends ");
    }

    public void disable() {
        enabled.set(false);
    }

    public void enable() {
        enabled.set(true);
    }

    public Boolean isEnabled() {
        return this.enabled.get();
    }

    private void start(Job job) {
        logger.debug("Using launcher {}", jobLauncher.getClass());
        try {
            logger.info("Running job {}", job.getName());
            JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
            batchRunCounter.incrementAndGet();
            logger.debug("Job {} executed with status {} ", job.getName(), jobExecution.getStatus());
        } catch (JobExecutionException e) {
            logger.error("Can not run job " + job.getName(), e);
        } catch (Exception e) {
            logger.error("Can not get job explorer object" + job.getName(), e);
        }
    }

    public AtomicInteger getBatchRunCounter() {
        return batchRunCounter;
    }

}
