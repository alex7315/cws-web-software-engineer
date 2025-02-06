package org.cws.web.software.engineer.task.sync.scheduler.runner;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This class implements {@link JobRunner} interface to launch {@code GithubUsersSyncJob}. <br />
 * Count of job launching are calculated and provided by {@link GithubUsersSyncJobRunner#getRunCounter()} method
 */
public class GithubUsersSyncJobRunner implements JobRunner {

    private static final Logger LOG             = LoggerFactory.getLogger(GithubUsersSyncJobRunner.class);

    private final AtomicInteger batchRunCounter = new AtomicInteger(0);
    private JobLauncher         jobLauncher;
    private Job                 githubUsersSyncJob;

    public GithubUsersSyncJobRunner(JobLauncher jobLauncher, @Qualifier("githubUsersSyncJob") Job githubUsersSyncJob) {
        this.jobLauncher = jobLauncher;
        this.githubUsersSyncJob = githubUsersSyncJob;
    }

    @Override
    public void run() {
        LOG.debug("Using launcher {}", jobLauncher.getClass());
        try {
            LOG.info("Running job {}", githubUsersSyncJob.getName());
            JobExecution jobExecution = jobLauncher.run(githubUsersSyncJob, new JobParametersBuilder().toJobParameters());
            batchRunCounter.incrementAndGet();
            LOG.debug("Job {} executed with status {} ", githubUsersSyncJob.getName(), jobExecution.getStatus());
        } catch (JobExecutionException e) {
            LOG.error("Can not run job " + githubUsersSyncJob.getName(), e);
        } catch (Exception e) {
            LOG.error("Can not get job explorer object" + githubUsersSyncJob.getName(), e);
        }
    }

    @Override
    public int getRunCounter() {
        return batchRunCounter.get();
    }

}
