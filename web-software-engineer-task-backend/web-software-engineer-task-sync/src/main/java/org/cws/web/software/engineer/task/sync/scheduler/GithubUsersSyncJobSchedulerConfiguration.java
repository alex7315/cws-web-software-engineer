package org.cws.web.software.engineer.task.sync.scheduler;

import org.cws.web.software.engineer.task.sync.scheduler.manager.JobTaskSchedulingManager;
import org.cws.web.software.engineer.task.sync.scheduler.runner.GithubUsersSyncJobRunner;
import org.cws.web.software.engineer.task.sync.scheduler.runner.JobRunner;
import org.cws.web.software.engineer.task.sync.scheduler.trigger.FixedRateSchedulingTrigger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class GithubUsersSyncJobSchedulerConfiguration {

	@Bean
	Trigger fixedRateSchedulingTrigger(@Value("${cws.github.sync.scheduled.rate}") long scheduledRate) {
		return new FixedRateSchedulingTrigger(scheduledRate);
	}

    @Bean
    JobRunner githubUsersSyncJobRunner(@Autowired JobLauncher jobLauncher, @Autowired @Qualifier("githubUsersSyncJob") Job githubUsersSyncJob) {
        return new GithubUsersSyncJobRunner(jobLauncher, githubUsersSyncJob);
    }

	@Bean
	//@formatter:off
    JobTaskSchedulingManager githubUsersSyncJobTaskSchedulingManager(
            @Autowired TaskScheduler scheduler,
            JobRunner githubUsersSyncJobRunner,
            Trigger fixedRateSchedulingTrigger ) {
        return new JobTaskSchedulingManager(scheduler, githubUsersSyncJobRunner, fixedRateSchedulingTrigger);
    }
    //@formatter:on
}
