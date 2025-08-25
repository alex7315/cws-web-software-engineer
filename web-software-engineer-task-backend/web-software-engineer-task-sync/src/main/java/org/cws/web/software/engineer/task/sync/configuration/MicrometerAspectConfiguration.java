package org.cws.web.software.engineer.task.sync.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * This configuration defines aspects like {@link TimedAspect} allows processing
 * annotations like @Timed, that adds Micrometer custom metric(e.g. Timer)
 */
@Configuration
public class MicrometerAspectConfiguration {
	@Bean
	TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

	@Bean
	CountedAspect countedAspect(MeterRegistry registry) {
		return new CountedAspect(registry);
	}
}
