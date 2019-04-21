package ch.difty.scipamato.core.sync.launcher;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

@SuppressWarnings("SameParameterValue")
@ExtendWith(MockitoExtension.class)
public class RefDataSyncJobLauncherTest {

    private static final int    JOB_STEP_ID_START    = 75;
    private static final int    BATCH_SIZE           = 100;
    private static final String UNSYNCHED_PAPERS_MSG = "unsynched papers";

    private RefDataSyncJobLauncher launcher;

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Warner      warner;

    @Mock
    private Job syncLanguageJob, syncNewStudyPageLinkJob, syncCodeClassJob, syncCodeJob, syncPaperJob;
    @Mock
    private Job syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob, syncNewStudyTopicJob, syncKeywordJob;

    private Map<String, Job> jobMap;

    @BeforeEach
    public void setUp() {
        launcher = new RefDataSyncJobLauncher(jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob, syncCodeClassJob,
            syncCodeJob, syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob, syncNewStudyTopicJob,
            syncKeywordJob, warner);

        jobMap = jobsPerTopic();
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob, syncCodeClassJob, syncCodeJob,
            syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob, syncNewStudyTopicJob,
            syncKeywordJob, warner);
    }

    private Map<String, Job> jobsPerTopic() {
        final Map<String, Job> jobMap = new LinkedHashMap<>();
        jobMap.put("languages", syncLanguageJob);
        jobMap.put("newStudyPage_links", syncNewStudyPageLinkJob);
        jobMap.put("code_classes", syncCodeClassJob);
        jobMap.put("codes", syncCodeJob);
        jobMap.put("papers", syncPaperJob);
        jobMap.put("newsletters", syncNewsletterJob);
        jobMap.put("newsletterTopics", syncNewsletterTopicJob);
        jobMap.put("newStudyTopics", syncNewStudyTopicJob);
        jobMap.put("newStudies", syncNewStudyJob);
        jobMap.put("keywords", syncKeywordJob);
        return jobMap;
    }

    @Test
    public void jobParameters_haveSingleIdentifyingParameterRunDate_withCurrentDate() {
        JobParameters params = launcher.getJobParameters();
        assertThat(params.getParameters()).hasSize(1);
        assertThat(params.getDate("runDate")).isCloseTo(new Date(), 1000);
        assertThat(params
            .getParameters()
            .values()
            .iterator()
            .next()
            .isIdentifying()).isTrue();
    }

    @Test
    public void launching_withUnsynchronizedPapersAndAllStepsSuccessful_addsWarningBeforeStepResultsAndSucceeds()
        throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
        JobInstanceAlreadyCompleteException {

        when(warner.findUnsynchronizedPapers()).thenReturn(Optional.of(UNSYNCHED_PAPERS_MSG));
        final List<String> expectedMessages = messagesWithAllStepsSuccessful(jobMap, true);

        final SyncJobResult result = launcher.launch();

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.isFailed()).isFalse();
        assertAllJobsSuccessfulButWithUnsynchedPapers(expectedMessages, result);

        verifyMocks(jobMap);
    }

    private List<String> messagesWithAllStepsSuccessful(final Map<String, Job> jobMap, boolean withUnsynchedPapers)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {

        final List<String> expectedMessages = new ArrayList<>();

        if (withUnsynchedPapers)
            expectedMessages.add(UNSYNCHED_PAPERS_MSG);

        int jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            // simple fixture using jobId to get variance in records written
            final int writtenRecords = jobId + BATCH_SIZE;
            final String msg = String.format(
                "Job %d has returned with exitCode COMPLETED (status COMPLETED): %d %s were synchronized.", jobId,
                writtenRecords, entry.getKey());
            expectedMessages.add(msg);
            jobId++;
        }

        jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            jobLauncherFixture(jobId++, BatchStatus.COMPLETED, ExitStatus.COMPLETED, entry.getValue());
        }
        return expectedMessages;
    }

    private void jobLauncherFixture(final long executionID, final BatchStatus status, final ExitStatus exitStatus,
        final Job job)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {
        final JobExecution jobExecution = jobExecutionFixture(executionID, status, exitStatus);
        when(jobLauncher.run(eq(job), isA(JobParameters.class))).thenReturn(jobExecution);
    }

    private JobExecution jobExecutionFixture(final long id, final BatchStatus status, final ExitStatus exitStatus) {
        final JobExecution jobExecution = mock(JobExecution.class);
        when(jobExecution.getId()).thenReturn(id);
        when(jobExecution.getStatus()).thenReturn(status);
        when(jobExecution.getExitStatus()).thenReturn(exitStatus);

        StepExecution stepExecution1 = mock(StepExecution.class);
        StepExecution stepExecution2 = mock(StepExecution.class);
        when(stepExecution1.getWriteCount()).thenReturn(BATCH_SIZE);
        // simple fixture to get some variance in the returned records
        when(stepExecution2.getWriteCount()).thenReturn((int) id);

        when(jobExecution.getStepExecutions()).thenReturn(List.of(stepExecution1, stepExecution2));

        return jobExecution;
    }

    private void assertAllJobsSuccessfulButWithUnsynchedPapers(final List<String> expectedMessages,
        final SyncJobResult result) {
        assertThat(result.getMessages()).hasSize(11);

        // warning due to unsynchronized papers
        final SyncJobResult.LogMessage logMessage = result
            .getMessages()
            .get(0);
        assertThat(logMessage.getMessage()).isEqualTo(UNSYNCHED_PAPERS_MSG);
        assertThat(logMessage.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.WARNING);

        // job step results
        assertThat(result
            .getMessages()
            .subList(1, result
                .getMessages()
                .size()))
            .extracting("messageLevel")
            .containsOnly(SyncJobResult.MessageLevel.INFO);
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactlyElementsOf(expectedMessages);
    }

    private void assertAllJobsSuccessfulWithNoUnsynchedPapers(final List<String> expectedMessages,
        final SyncJobResult result) {
        assertThat(result.getMessages()).hasSize(10);
        assertThat(result.getMessages())
            .extracting("messageLevel")
            .containsOnly(SyncJobResult.MessageLevel.INFO);
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactlyElementsOf(expectedMessages);
    }

    private void verifyMocks(final Map<String, Job> jobMap)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {
        verify(warner).findUnsynchronizedPapers();
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            verify(jobLauncher).run(eq(entry.getValue()), isA(JobParameters.class));
        }
    }

    @Test
    public void launching_withoutUnsynchronizedPapers_onlyAddsInfoMessages()
        throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
        JobInstanceAlreadyCompleteException {
        when(warner.findUnsynchronizedPapers()).thenReturn(Optional.empty());
        final List<String> expectedMessages = messagesWithAllStepsSuccessful(jobMap, false);

        final SyncJobResult result = launcher.launch();
        assertThat(result.isSuccessful()).isTrue();

        assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages, result);

        verifyMocks(jobMap);
    }

    @Test
    public void launching_withFailingStep_failsJob()
        throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
        JobInstanceAlreadyCompleteException {
        when(warner.findUnsynchronizedPapers()).thenReturn(Optional.empty());
        final List<String> expectedMessages = messagesWithFailingStepInPosition3(jobMap);

        final SyncJobResult result = launcher.launch();
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isTrue();

        assertAllJobsSuccessfulExceptThird(expectedMessages, result);

        verifyMocks(jobMap);
    }

    private List<String> messagesWithFailingStepInPosition3(final Map<String, Job> jobMap)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {
        final int failAfter = 2;

        final List<String> expectedMessages = new ArrayList<>();
        int jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            // simple fixture using jobId to get variance in records written
            final int writtenRecords = jobId + BATCH_SIZE;
            if (jobId != JOB_STEP_ID_START + failAfter) {
                final String msg = String.format(
                    "Job %d has returned with exitCode COMPLETED (status COMPLETED): %d %s were synchronized.", jobId,
                    writtenRecords, entry.getKey());
                expectedMessages.add(msg);
            } else {
                final String msg = String.format(
                    "Job %d has returned with exitCode FAILED (status FAILED): %d %s were synchronized.", jobId,
                    writtenRecords, entry.getKey());
                expectedMessages.add(msg);
            }
            jobId++;
        }

        jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            if (jobId != JOB_STEP_ID_START + failAfter) {
                jobLauncherFixture(jobId++, BatchStatus.COMPLETED, ExitStatus.COMPLETED, entry.getValue());
            } else {
                jobLauncherFixture(jobId++, BatchStatus.FAILED, ExitStatus.FAILED, entry.getValue());
            }
        }
        return expectedMessages;
    }

    private void assertAllJobsSuccessfulExceptThird(final List<String> expectedMessages, final SyncJobResult result) {
        assertThat(result.getMessages()).hasSize(10);
        assertThat(result.getMessages())
            .extracting("messageLevel")
            .containsExactly(SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
                SyncJobResult.MessageLevel.ERROR, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
                SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
                SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO);
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactlyElementsOf(expectedMessages);
    }

    @Test
    public void launching_withUnexpectedException_stopsRunningSubsequentJobs()
        throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
        JobInstanceAlreadyCompleteException {
        when(warner.findUnsynchronizedPapers()).thenReturn(Optional.empty());
        final List<String> expectedMessages = messagesWithExceptionAfter2nd(jobMap);
        expectedMessages.add(
            "Unexpected exception of type class java.lang.RuntimeException: unexpected exception somewhere");

        final SyncJobResult result = launcher.launch();
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isTrue();

        assertThat(result.getMessages())
            .extracting("messageLevel")
            .containsExactly(SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
                SyncJobResult.MessageLevel.ERROR);
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactlyElementsOf(expectedMessages);

        verify(warner).findUnsynchronizedPapers();
        for (final Map.Entry<String, Job> entry : jobMap
            .entrySet()
            .stream()
            .limit(3)
            .collect(toList())) {
            verify(jobLauncher).run(eq(entry.getValue()), isA(JobParameters.class));
        }
    }

    private List<String> messagesWithExceptionAfter2nd(final Map<String, Job> jobMap)
        throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
        JobParametersInvalidException {
        final int failAfter = 2;

        final List<String> expectedMessages = new ArrayList<>();
        int jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            // simple fixture using jobId to get variance in records written
            final int writtenRecords = jobId + BATCH_SIZE;
            final String msg = String.format(
                "Job %d has returned with exitCode COMPLETED (status COMPLETED): %d %s were synchronized.", jobId,
                writtenRecords, entry.getKey());
            expectedMessages.add(msg);
            jobId++;
            if (jobId == JOB_STEP_ID_START + failAfter)
                break;
        }

        jobId = JOB_STEP_ID_START;
        for (final Map.Entry<String, Job> entry : jobMap.entrySet()) {
            jobLauncherFixture(jobId++, BatchStatus.COMPLETED, ExitStatus.COMPLETED, entry.getValue());
            if (jobId == JOB_STEP_ID_START + failAfter)
                break;
        }

        when(jobLauncher.run(eq(syncCodeClassJob), isA(JobParameters.class))).thenThrow(
            new RuntimeException("unexpected exception somewhere"));
        return expectedMessages;
    }
}
