package org.cws.web.software.engineer.task.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * This configuration defines {@link TimedAspect} aspect
 * allows processing of @Timed annotation, 
 * that adds Micrometer custom Timer metric  
 */
@Configuration
public class TimedConfiguration {
    
    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
