package org.cws.web.software.engineer.task.sync.scheduler.manager;

import java.util.concurrent.ScheduledFuture;

import org.cws.web.software.engineer.task.sync.scheduler.runner.JobRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * This class configures Job task scheduling using {@link JobRunner} and {@link Trigger} <br/>
 * Class implements simple {@link SchedulingManager} to enable/disable of job task scheduling
 */
public class JobTaskSchedulingManager implements SchedulingConfigurer, SchedulingManager {

    private static final Logger LOG = LoggerFactory.getLogger(JobTaskSchedulingManager.class);

    private JobRunner           jobRunner;
    private TaskScheduler       scheduler;
    private Trigger             trigger;
    private ScheduledFuture<?>  future;

    public JobTaskSchedulingManager(TaskScheduler scheduler, JobRunner jobRunner, Trigger trigger) {
        this.scheduler = scheduler;
        this.jobRunner = jobRunner;
        this.trigger = trigger;
    }

    @Override
    public void enable() {
        LOG.info("Enable job scheduling");
        future = scheduler.schedule(jobRunner, trigger);
    }

    @Override
    public void disable() {
        LOG.info("Disable job scheduling");
        future.cancel(true);

    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        enable();
    }

    public int getJobRunCounter() {
        return jobRunner.getRunCounter();
    }

}
