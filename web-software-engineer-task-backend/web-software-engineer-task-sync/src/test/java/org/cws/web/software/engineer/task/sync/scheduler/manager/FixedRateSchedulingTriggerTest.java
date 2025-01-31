package org.cws.web.software.engineer.task.sync.scheduler.manager;

import static java.time.Instant.now;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.SimpleTriggerContext;

class FixedRateSchedulingTriggerTest {

    private static final int     SCHEDULING_RATE_IN_SECONG = 100;
    private static final Instant INITIAL_EXECUTION_INSTANT = LocalDateTime.parse("2025-01-01T01:01:00", ISO_DATE_TIME).atZone(ZoneId.systemDefault())
            .toInstant();

    @Test
    void schouldReturnExecutionTimeIfLastActualExecutionIsNotAvailable() {
        FixedRateSchedulingTrigger trigger = new FixedRateSchedulingTrigger(SCHEDULING_RATE_IN_SECONG);
        TriggerContext triggerContext = new SimpleTriggerContext();

        assertThat(trigger.nextExecution(triggerContext)).isAfter(now());
    }

    @Test
    void schuldReturnExecutionTimeGreaterThenLastActualExecution() {
        FixedRateSchedulingTrigger trigger = new FixedRateSchedulingTrigger(SCHEDULING_RATE_IN_SECONG);

        TriggerContext triggerContext = new SimpleTriggerContext(INITIAL_EXECUTION_INSTANT, INITIAL_EXECUTION_INSTANT, INITIAL_EXECUTION_INSTANT);
        assertThat(trigger.nextExecution(triggerContext)).isAfterOrEqualTo(INITIAL_EXECUTION_INSTANT.plusSeconds(SCHEDULING_RATE_IN_SECONG));
    }
}
