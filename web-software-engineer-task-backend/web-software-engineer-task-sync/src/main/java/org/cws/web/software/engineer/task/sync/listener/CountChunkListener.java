package org.cws.web.software.engineer.task.sync.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class CountChunkListener implements ChunkListener {

    private static final Logger LOG = LoggerFactory.getLogger(CountChunkListener.class);

    private int                 loggingInterval;

    public CountChunkListener(int loggingInterval) {
        this.loggingInterval = loggingInterval;
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        //nothing to do here
    }

    @Override
    public void afterChunk(ChunkContext context) {
        long readCount = context.getStepContext().getStepExecution().getReadCount();
        long writeCount = context.getStepContext().getStepExecution().getWriteCount();

        logCounter(readCount, "processed by reader");
        logCounter(writeCount, "processed by writer");
    }

    private void logCounter(long counterValue, String action) {
        // If the number of records read and written so far is a multiple of the logging interval then output a log message.
        if (counterValue >= loggingInterval && counterValue % loggingInterval == 0) {
            LOG.info("{} are {}", counterValue, action);
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        List<Throwable> chunkExceptions = context.getStepContext().getStepExecution().getFailureExceptions();
        chunkExceptions.forEach(t -> LOG.error("Chunk error: ", t));
    }

}
