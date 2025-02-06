package org.cws.web.software.engineer.task.sync.scheduler.trigger;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

/**
 * This implementation of {@link Trigger} can be used to schedule Job execution
 * at fixed rate. It is expected that {@code cws.github.sync.scheduled.rate}
 * property is set with rate value in seconds.
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
