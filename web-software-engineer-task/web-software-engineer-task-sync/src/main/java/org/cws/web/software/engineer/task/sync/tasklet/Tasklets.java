package org.cws.web.software.engineer.task.sync.tasklet;

import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Tasklets {

    @Bean
    Tasklet modificationTimestampCreationTasklet() {
        return new ModificationTimestampCreationTasklet();
    }
}
