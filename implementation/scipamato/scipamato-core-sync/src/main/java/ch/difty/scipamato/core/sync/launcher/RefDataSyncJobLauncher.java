package ch.difty.scipamato.core.sync.launcher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Launcher for the reference dat sync job. Comprises of the following
 * synchronization jobs:
 * <ol>
 * <li>code_class</li>
 * <li>codes</li>
 * <li>keywords TODO</li>
 * <li>papers</li>
 * </ol>
 * 
 * @author u.joss
 */
@Slf4j
@Component
public class RefDataSyncJobLauncher implements SyncJobLauncher {

    private final Job           syncCodeClassJob;
    private final Job           syncCodeJob;
    private final Job           syncPaperJob;
    protected final JobLauncher jobLauncher;

    public RefDataSyncJobLauncher(final JobLauncher jobLauncher,
            @Qualifier("syncCodeClassJob") final Job syncCodeClassJob, @Qualifier("syncCodeJob") final Job syncCodeJob,
            @Qualifier("syncPaperJob") final Job syncPaperJob) {
        this.jobLauncher = jobLauncher;
        this.syncCodeClassJob = syncCodeClassJob;
        this.syncCodeJob = syncCodeJob;
        this.syncPaperJob = syncPaperJob;
    }

    @Override
    public SyncJobResult launch() {
        log.info("Starting synchronization job from scipamato-core to scipamato-public...");
        final SyncJobResult result = new SyncJobResult();
        final JobParameters jobParameters = new JobParametersBuilder().addDate("runDate", Date.from(LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()), true)
            .toJobParameters();
        try {
            runSingleJob("code_classes", syncCodeClassJob, result, jobParameters);
            runSingleJob("codes", syncCodeJob, result, jobParameters);
            runSingleJob("papers", syncPaperJob, result, jobParameters);
            log.info("Job finished successfully.");
        } catch (final Exception ex) {
            log.error("Job terminated.", ex);
            result.setFailure("Unexpected exception of type " + ex.getClass() + ": " + ex.getMessage());
        }
        return result;
    }

    private void runSingleJob(final String topic, final Job job, final SyncJobResult result,
            final JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        setResultFrom(jobLauncher.run(job, jobParameters), result, topic);
    }

    private void setResultFrom(final JobExecution jobExecution, final SyncJobResult result, final String topic) {
        final Long id = jobExecution.getId();
        final String exitCode = jobExecution.getExitStatus()
            .getExitCode();
        final String status = jobExecution.getStatus()
            .name();
        final int writeCount = jobExecution.getStepExecutions()
            .stream()
            .mapToInt(StepExecution::getWriteCount)
            .sum();
        final String msg = String.format("Job %d has returned with exitCode %s (status %s): %d %s were synchronized.",
            id, exitCode, status, writeCount, topic);

        setMessageToResult(result, msg, jobExecution.getStatus());
    }

    private void setMessageToResult(final SyncJobResult result, final String msg, final BatchStatus status) {
        if (status == BatchStatus.COMPLETED)
            result.setSuccess(msg);
        else
            result.setFailure(msg);
    }

}