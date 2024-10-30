package org.cws.web.software.engineer.task.sync.controller;

import org.cws.web.software.engineer.task.sync.scheduler.GithubUsersSyncJobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobSchedulerController {

	private ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/scheduler/activate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void activate() {
		GithubUsersSyncJobScheduler scheduler = applicationContext.getBean(GithubUsersSyncJobScheduler.class);
		scheduler.enable();
	}

    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/scheduler/deactivate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deactivate() {
		GithubUsersSyncJobScheduler scheduler = applicationContext.getBean(GithubUsersSyncJobScheduler.class);
		scheduler.disable();
	}

}
