package org.cws.web.software.engineer.task.sync.scheduler;

import org.cws.web.software.engineer.task.sync.scheduler.manager.FixedRateSchedulingTrigger;
import org.cws.web.software.engineer.task.sync.scheduler.manager.GithubUsersSyncJobTaskSchedulingManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

//@Configuration
//@EnableScheduling
public class GithubUsersSyncJobSchedulerConfiguration {

    @Bean
    Trigger fixedRateSchedulingTrigger(@Value("${cws.github.sync.scheduled.rate}") long scheduledRate) {
        return new FixedRateSchedulingTrigger(scheduledRate);
    }

    @Bean
    //@formatter:off
    GithubUsersSyncJobTaskSchedulingManager githubUsersSyncJobTaskSchedulingManager(
            @Autowired TaskScheduler scheduler, 
            @Autowired JobLauncher   jobLauncher, 
            Trigger fixedRateSchedulingTrigger, 
            @Autowired @Qualifier("githubUsersSyncJob") Job githubUsersSyncJob) {
        return new GithubUsersSyncJobTaskSchedulingManager(scheduler, fixedRateSchedulingTrigger, jobLauncher, githubUsersSyncJob);
    }
    //@formatter:on
}
