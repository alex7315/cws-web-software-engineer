package org.cws.web.software.engineer.task.sync.tasklet;

import java.time.Instant;

import org.cws.web.software.engineer.task.sync.constants.ContextParameterConstants;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ModificationTimestampCreationTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        Instant timestamp = Instant.now();
        jobExecutionContext.put(ContextParameterConstants.CONTEXT_PARAM_MODIFICATION_TIMESTAMP, timestamp);
        LoggerFactory.getLogger(this.getClass().getName()).info("modification timestamp: {}", timestamp);

        return RepeatStatus.FINISHED;
    }

}
