package org.cws.web.software.engineer.task.sync.tasklet;

import static org.cws.web.software.engineer.task.sync.constants.ContextParameterConstants.CONTEXT_PARAM_MODIFICATION_TIMESTAMP;

import java.time.Instant;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ModificationTimestampDetectionTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        jobExecutionContext.put(CONTEXT_PARAM_MODIFICATION_TIMESTAMP, Instant.now());
        return RepeatStatus.FINISHED;
    }

}
