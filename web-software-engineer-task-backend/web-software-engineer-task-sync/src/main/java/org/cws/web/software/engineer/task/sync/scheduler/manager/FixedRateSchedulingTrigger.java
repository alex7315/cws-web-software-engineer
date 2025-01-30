package org.cws.web.software.engineer.task.sync.scheduler.manager;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

/**
 * This implementation of {@link Trigger} can be used to schedule Job execution at fixed rate.
 * Class expects that property {@code cws.github.sync.scheduled.rate} is set with rate value in seconds. 
 */
@Component
public class FixedRateSchedulingTrigger implements Trigger {

    private long scheduledRate;

    public FixedRateSchedulingTrigger(@Value("${cws.github.sync.scheduled.rate}") long scheduledRate) {
        this.scheduledRate = scheduledRate;
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        Instant lastActualExecution = triggerContext.lastActualExecution();
        Instant actualExecution = lastActualExecution == null ? Instant.now() : lastActualExecution;
        return actualExecution.plusSeconds(scheduledRate);
    }
}
