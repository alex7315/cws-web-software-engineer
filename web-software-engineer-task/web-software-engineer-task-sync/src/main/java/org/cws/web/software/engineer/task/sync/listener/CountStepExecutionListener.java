package org.cws.web.software.engineer.task.sync.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CountStepExecutionListener implements StepExecutionListener {

    public static final Logger LOG = LoggerFactory.getLogger(CountStepExecutionListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // nothing to do

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long readCount = stepExecution.getReadCount();
        long writeCount = stepExecution.getWriteCount();

        LOG.info("{} items are read  {} items are written", readCount, writeCount);
        return stepExecution.getExitStatus();
    }

}
