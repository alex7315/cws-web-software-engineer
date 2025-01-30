package org.cws.web.software.engineer.task.sync.scheduler.manager;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * This class configures Job task scheduling using {@link Trigger} and
 * implements simple {@link SchedulingManager} to enable/disable scheduling
 */
@Component
public class GithubUsersSyncJobTaskSchedulingManager implements SchedulingConfigurer, SchedulingManager {

	private final Logger logger = LoggerFactory.getLogger(GithubUsersSyncJobTaskSchedulingManager.class);

	private TaskScheduler scheduler;

	private Trigger trigger;

	private ScheduledFuture<?> future;

	private AtomicInteger batchRunCounter = new AtomicInteger(0);

	private JobLauncher jobLauncher;

	private Job githubUsersSyncJob;

	//@formatter:off
    public GithubUsersSyncJobTaskSchedulingManager(TaskScheduler scheduler, 
            Trigger trigger,
            JobLauncher jobLauncher, 
            @Qualifier("githubUsersSyncJob") Job githubUsersSyncJob) {
    //@formatter:on
		this.scheduler = scheduler;
		this.trigger = trigger;
		this.jobLauncher = jobLauncher;
		this.githubUsersSyncJob = githubUsersSyncJob;
	}

	@Override
	public void enable() {
		logger.info("Enable job scheduling");
		future = scheduler.schedule(new JobRunner(), trigger);
	}

	private class JobRunner implements Runnable {

		@Override
		public void run() {
			logger.debug("Using launcher {}", jobLauncher.getClass());
			try {
				logger.info("Running job {}", githubUsersSyncJob.getName());
				JobExecution jobExecution = jobLauncher.run(githubUsersSyncJob,
						new JobParametersBuilder().toJobParameters());
				batchRunCounter.incrementAndGet();
				logger.debug("Job {} executed with status {} ", githubUsersSyncJob.getName(), jobExecution.getStatus());
			} catch (JobExecutionException e) {
				logger.error("Can not run job " + githubUsersSyncJob.getName(), e);
			} catch (Exception e) {
				logger.error("Can not get job explorer object" + githubUsersSyncJob.getName(), e);
			}
		}

	}

	@Override
	public void disable() {
		logger.info("Disable job scheduling");
		future.cancel(true);

	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		enable();
	}

	public AtomicInteger getBatchRunCounter() {
		return batchRunCounter;
	}

}
