package org.cws.web.software.engineer.task.sync.scheduler.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

//@formatter:off
@SpringBootTest(properties = {
      "spring.datasource.url=jdbc:h2:mem:cws_github",
      "spring.datasource.driverClassName=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=password",
      "spring.jpa.defer-datasource-initialization=true",
      "spring.jpa.open-in-view=false",
      "spring.data.web.pageable.default-page-size=5",
      "cws.github.sync.scheduled.rate=5",
      "cws.github.user.page.size=4",
      "cws.github.user.count.max=8",
      "cws.github.api.base.url=https://api.github.com",
      "cws.security.jwt.secret=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65",
      "cws.github.api.authorization.token=fake",
      "cws.security.jwt.expiration.ms=600",
      "cws.security.refresh.token.expiration.ms=1200",
      "cws.sync.port.exposed=8080"
      
})
//@formatter:on
class JobTaskSchedulingManagerTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void shouldStopJobWhenSchedulerDisabledAndRestartJobWhenSchedulerEnabled() throws Exception {
		JobTaskSchedulingManager schedulerBean = context
				.getBean(JobTaskSchedulingManager.class);
        await().forever().until(() -> schedulerBean.getJobRunCounter(), greaterThan(3));
		schedulerBean.disable();
        assertThat(schedulerBean.getJobRunCounter()).isGreaterThan(3);

		schedulerBean.enable();
        await().forever().until(() -> schedulerBean.getJobRunCounter(), greaterThan(6));
        assertThat(schedulerBean.getJobRunCounter()).isGreaterThan(6);
	}
}
