package org.cws.web.software.engineer.task.sync.scheduler.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class GithubUsersSyncJobRunnerTest {

    @Mock
    private Job                      githubUsersSyncJob;

    @Mock
    private JobLauncher              jobLauncher;

    @InjectMocks
    private GithubUsersSyncJobRunner githubUsersSyncJobRunner;

    @Test
    void shouldRunJobCorrectly() throws Exception {
        when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class))).thenReturn(new JobExecution(1L));
        assertDoesNotThrow(() -> githubUsersSyncJobRunner.run());
    }

    @Test
    void shouldWriteLogByJobLaunchingError(CapturedOutput capturedOutput) throws Exception {
        when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class))).thenThrow(new JobRestartException("Error to restart job"));
        githubUsersSyncJobRunner.run();
        assertThat(capturedOutput.getOut()).containsPattern("Can not run job*");
    }

    @Test
    void testGetRunCounter() throws Exception {
        when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class))).thenReturn(new JobExecution(2L));
        githubUsersSyncJobRunner.run();
        githubUsersSyncJobRunner.run();
        assertThat(githubUsersSyncJobRunner.getRunCounter()).isGreaterThan(1);
    }

}
