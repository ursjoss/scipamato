package ch.difty.scipamato.core.sync.launcher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Launcher for the reference dat sync job. Comprises of the following
 * synchronization jobs:
 * <ol>
 * <li>languages</li>
 * <li>NewStudyPage links</li>
 * <li>code_classes</li>
 * <li>codes</li>
 * <li>papers</li>
 * <li>newsletters</li>
 * <li>newsletterTopics</li>
 * <li>newStudyTopics</li>
 * <li>newStudies</li>
 * <li>keywords</li>
 * </ol>
 *
 * @author u.joss
 */
@Slf4j
@Component
@Profile("!wickettest")
public class RefDataSyncJobLauncher implements SyncJobLauncher {

    private final Job         syncLanguageJob;
    private final Job         syncNewStudyPageLinkJob;
    private final Job         syncCodeClassJob;
    private final Job         syncCodeJob;
    private final Job         syncPaperJob;
    private final Job         syncNewsletterJob;
    private final Job         syncNewsletterTopicJob;
    private final Job         syncNewStudyJob;
    private final Job         syncNewStudyTopicJob;
    private final Job         syncKeywordJob;
    private final JobLauncher jobLauncher;

    private final Warner warner;

    public RefDataSyncJobLauncher(@NotNull final JobLauncher jobLauncher,
        @Qualifier("syncLanguageJob") @NotNull final Job syncLanguageJob,
        @Qualifier("syncNewStudyPageLinkJob") @NotNull final Job syncNewStudyPageLinkJob,
        @Qualifier("syncCodeClassJob") @NotNull final Job syncCodeClassJob,
        @Qualifier("syncCodeJob") @NotNull final Job syncCodeJob,
        @Qualifier("syncPaperJob") @NotNull final Job syncPaperJob,
        @Qualifier("syncNewsletterJob") @NotNull final Job syncNewsletterJob,
        @Qualifier("syncNewsletterTopicJob") @NotNull final Job syncNewsletterTopicJob,
        @Qualifier("syncNewStudyJob") @NotNull final Job syncNewStudyJob,
        @Qualifier("syncNewStudyTopicJob") @NotNull final Job syncNewStudyTopicJob,
        @Qualifier("syncKeywordJob") @NotNull final Job syncKeywordJob, @NotNull final Warner warner) {
        this.jobLauncher = jobLauncher;
        this.syncNewStudyPageLinkJob = syncNewStudyPageLinkJob;
        this.syncLanguageJob = syncLanguageJob;
        this.syncCodeClassJob = syncCodeClassJob;
        this.syncCodeJob = syncCodeJob;
        this.syncPaperJob = syncPaperJob;
        this.syncNewsletterJob = syncNewsletterJob;
        this.syncNewsletterTopicJob = syncNewsletterTopicJob;
        this.syncNewStudyJob = syncNewStudyJob;
        this.syncNewStudyTopicJob = syncNewStudyTopicJob;
        this.syncKeywordJob = syncKeywordJob;
        this.warner = warner;
    }

    @NotNull
    @Override
    public SyncJobResult launch() {
        log.info("Starting synchronization job from scipamato-core to scipamato-public...");
        final SyncJobResult result = new SyncJobResult();
        final JobParameters jobParameters = getJobParameters();
        try {
            warnAboutUnsynchronizedEntities(result);

            runSingleJob("languages", syncLanguageJob, result, jobParameters);
            runSingleJob("newStudyPage_links", syncNewStudyPageLinkJob, result, jobParameters);
            runSingleJob("code_classes", syncCodeClassJob, result, jobParameters);
            runSingleJob("codes", syncCodeJob, result, jobParameters);
            runSingleJob("papers", syncPaperJob, result, jobParameters);
            runSingleJob("newsletters", syncNewsletterJob, result, jobParameters);
            runSingleJob("newsletterTopics", syncNewsletterTopicJob, result, jobParameters);
            runSingleJob("newStudyTopics", syncNewStudyTopicJob, result, jobParameters);
            runSingleJob("newStudies", syncNewStudyJob, result, jobParameters);
            runSingleJob("keywords", syncKeywordJob, result, jobParameters);

            if (result.isSuccessful())
                log.info("Job finished successfully.");
            else
                log.warn("Job finished with issues.");
        } catch (final Exception ex) {
            log.error("Job terminated.", ex);
            result.setFailure("Unexpected exception of type " + ex.getClass() + ": " + ex.getMessage());
        }
        return result;
    }

    // package protected for testing
    @NotNull
    JobParameters getJobParameters() {
        return new JobParametersBuilder()
            .addDate("runDate", Date.from(LocalDateTime
                .now()
                .atZone(ZoneId.systemDefault())
                .toInstant()), true)
            .toJobParameters();
    }

    private void runSingleJob(final String topic, final Job job, final SyncJobResult result,
        final JobParameters jobParameters)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {
        setResultFrom(jobLauncher.run(job, jobParameters), result, topic);
    }

    private void setResultFrom(final JobExecution jobExecution, final SyncJobResult result, final String topic) {
        final Long id = jobExecution.getId();
        final String exitCode = jobExecution
            .getExitStatus()
            .getExitCode();
        final String status = jobExecution
            .getStatus()
            .name();
        final int writeCount = jobExecution
            .getStepExecutions()
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

    private void warnAboutUnsynchronizedEntities(final SyncJobResult result) {
        String unsynchronized = warner.findUnsynchronizedPapers();
        if (unsynchronized != null)
            result.setWarning(unsynchronized);
    }
}
