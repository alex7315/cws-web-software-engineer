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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/job")
public class JobSchedulerController {

	private ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

    @Operation(summary = "Activated synch job")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Synch job is activated") })
    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/scheduler/activate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void activate() {
        GithubUsersSyncJobScheduler scheduler = applicationContext.getBean(GithubUsersSyncJobScheduler.class);
		scheduler.enable();
	}

    @Operation(summary = "Deactivated synch job")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Synch job is deactivated") })
    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/scheduler/deactivate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deactivate() {
        GithubUsersSyncJobScheduler scheduler = applicationContext.getBean(GithubUsersSyncJobScheduler.class);
		scheduler.disable();
	}

}
