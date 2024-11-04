package org.cws.web.software.engineer.task.sync.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@formatter:off
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:cws_github",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.defer-datasource-initialization=true",
        "spring.jpa.open-in-view=false",
        "spring.data.web.pageable.default-page-size=5",
        "cws.github.sync.scheduled.rate=8",
        "cws.github.user.page.size=4",
        "cws.github.user.count.max=8",
        "cws.github.authorization.token=Secret12Secret34Secret56Secret78Secret90Secret09Secret87Secret65",
        "cws.security.jwt.secret=secret",
        "cws.security.jwt.expiration.ms=600"
        
})
@EnableTransactionManagement
//@formatter:on
class GithubUsersSyncJobSchedulerTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void shouldStopJobWhenSchedulerDisabled() throws Exception {
		GithubUsersSyncJobScheduler schedulerBean = context.getBean(GithubUsersSyncJobScheduler.class);
		await().untilAsserted(() -> assertEquals(2, schedulerBean.getBatchRunCounter().get()));
		schedulerBean.disable();
		await().atLeast(20, TimeUnit.SECONDS);

		assertThat(schedulerBean.getBatchRunCounter().get()).isGreaterThan(1);
	}
}
