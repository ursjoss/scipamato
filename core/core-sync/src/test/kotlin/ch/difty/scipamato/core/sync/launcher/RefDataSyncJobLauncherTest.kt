package ch.difty.scipamato.core.sync.launcher

import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.batch.core.*
import org.springframework.batch.core.launch.JobLauncher

internal class RefDataSyncJobLauncherTest {

    private val jobLauncher = mock<JobLauncher>()
    private val warner = mock<Warner>()

    private val syncLanguageJob = mock<Job>()
    private val syncNewStudyPageLinkJob = mock<Job>()
    private val syncCodeClassJob = mock<Job>()
    private val syncCodeJob = mock<Job>()
    private val syncPaperJob = mock<Job>()
    private val syncNewsletterJob = mock<Job>()
    private val syncNewsletterTopicJob = mock<Job>()
    private val syncNewStudyJob = mock<Job>()
    private val syncNewStudyTopicJob = mock<Job>()
    private val syncKeywordJob = mock<Job>()

    private var launcher = RefDataSyncJobLauncher(jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob,
        syncCodeClassJob, syncCodeJob, syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob,
        syncNewStudyTopicJob, syncKeywordJob, warner)

    private var jobMap = jobsPerTopic()

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob, syncCodeClassJob, syncCodeJob,
            syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob, syncNewStudyTopicJob,
            syncKeywordJob, warner)
    }

    private fun jobsPerTopic(): Map<String, Job> {
        return mapOf(
            "languages" to syncLanguageJob,
            "newStudyPage_links" to syncNewStudyPageLinkJob,
            "code_classes" to syncCodeClassJob,
            "codes" to syncCodeJob,
            "papers" to syncPaperJob,
            "newsletters" to syncNewsletterJob,
            "newsletterTopics" to syncNewsletterTopicJob,
            "newStudyTopics" to syncNewStudyTopicJob,
            "newStudies" to syncNewStudyJob,
            "keywords" to syncKeywordJob)
    }

    @Test
    fun jobParameters_haveSingleIdentifyingParameterRunDate_withCurrentDate() {
        val params = launcher.jobParameters
        assertThat(params.parameters).hasSize(1)
        assertThat(params.getDate("runDate")).isCloseTo(java.util.Date(), 1000)
        assertThat(params.parameters.values.first().isIdentifying).isTrue()
    }

    @Test
    fun launching_withUnsynchronizedPapersAndAllStepsSuccessful_addsWarningBeforeStepResultsAndSucceeds() {
        whenever(warner.findUnsynchronizedPapers()).thenReturn(java.util.Optional.of(UNSYNCHED_PAPERS_MSG))
        val expectedMessages = messagesWithAllStepsSuccessful(jobMap, true)

        val result = launcher.launch()

        assertThat(result.isSuccessful).isTrue()
        assertThat(result.isFailed).isFalse()
        assertAllJobsSuccessfulButWithUnsynchedPapers(expectedMessages, result)

        verifyMocks(jobMap)
    }

    private fun messagesWithAllStepsSuccessful(jobMap: Map<String, Job>, withUnsynchedPapers: Boolean): List<String> {
        val expectedMessages = ArrayList<String>()

        if (withUnsynchedPapers)
            expectedMessages.add(UNSYNCHED_PAPERS_MSG)

        var jobId = JOB_STEP_ID_START
        jobMap.keys.forEach { key ->
            // simple fixture using jobId to get variance in records written
            val writtenRecords = jobId + BATCH_SIZE
            val msg = "Job $jobId has returned with exitCode COMPLETED (status COMPLETED): " +
                "$writtenRecords $key were synchronized."
            expectedMessages.add(msg)
            jobId++
        }

        jobId = JOB_STEP_ID_START
        jobMap.values.forEach { value ->
            jobLauncherFixture(jobId++.toLong(), BatchStatus.COMPLETED, ExitStatus.COMPLETED, value)
        }

        return expectedMessages
    }

    private fun jobLauncherFixture(executionID: Long, status: BatchStatus, exitStatus: ExitStatus, job: Job) {
        val jobExecution = jobExecutionFixture(executionID, status, exitStatus)
        doReturn(jobExecution).whenever(jobLauncher).run(eq(job), any())
    }

    private fun jobExecutionFixture(id: Long, status: BatchStatus, exitStatus: ExitStatus): JobExecution {
        val jobExecution = Mockito.mock(JobExecution::class.java)
        whenever(jobExecution.id).thenReturn(id)
        whenever(jobExecution.status).thenReturn(status)
        whenever(jobExecution.exitStatus).thenReturn(exitStatus)

        val stepExecution1 = Mockito.mock(StepExecution::class.java)
        val stepExecution2 = Mockito.mock(StepExecution::class.java)
        whenever(stepExecution1.writeCount).thenReturn(BATCH_SIZE)
        // simple fixture to get some variance in the returned records
        whenever(stepExecution2.writeCount).thenReturn(id.toInt())

        whenever(jobExecution.stepExecutions).thenReturn(listOf(stepExecution1, stepExecution2))

        return jobExecution
    }

    private fun assertAllJobsSuccessfulButWithUnsynchedPapers(expectedMessages: List<String>, result: SyncJobResult) {
        assertThat(result.messages).hasSize(11)

        // warning due to unsynchronized papers
        val logMessage = result.messages[0]
        assertThat(logMessage.message).isEqualTo(UNSYNCHED_PAPERS_MSG)
        assertThat(logMessage.messageLevel).isEqualTo(SyncJobResult.MessageLevel.WARNING)

        // job step results
        assertThat(result.messages.subList(1, result.messages.size).map { it.messageLevel })
            .containsOnly(SyncJobResult.MessageLevel.INFO)
        assertThat(result.messages.map { it.message }).containsExactlyElementsOf(expectedMessages)
    }

    private fun assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages: List<String>, result: SyncJobResult) {
        assertThat(result.messages).hasSize(10)
        assertThat(result.messages.map { it.messageLevel }).containsOnly(SyncJobResult.MessageLevel.INFO)
        assertThat(result.messages.map { it.message }).containsExactlyElementsOf(expectedMessages)
    }

    private fun verifyMocks(jobMap: Map<String, Job>) {
        verify(warner).findUnsynchronizedPapers()
        jobMap.values.forEach { value -> verify(jobLauncher).run(eq(value), any()) }
    }

    @Test
    fun launching_withoutUnsynchronizedPapers_onlyAddsInfoMessages() {
        whenever(warner.findUnsynchronizedPapers()).thenReturn(java.util.Optional.empty())
        val expectedMessages = messagesWithAllStepsSuccessful(jobMap, false)

        val result = launcher.launch()
        assertThat(result.isSuccessful).isTrue()

        assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages, result)

        verifyMocks(jobMap)
    }

    @Test
    fun launching_withFailingStep_failsJob() {
        whenever(warner.findUnsynchronizedPapers()).thenReturn(java.util.Optional.empty())
        val expectedMessages = messagesWithFailingStepInPosition3(jobMap)

        val result = launcher.launch()
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.isFailed).isTrue()

        assertAllJobsSuccessfulExceptThird(expectedMessages, result)

        verifyMocks(jobMap)
    }

    private fun messagesWithFailingStepInPosition3(jobMap: Map<String, Job>): List<String> {
        val failAfter = 2

        val expectedMessages = ArrayList<String>()
        var jobId = JOB_STEP_ID_START
        jobMap.keys.forEach { key ->
            // simple fixture using jobId to get variance in records written
            val writtenRecords = jobId + BATCH_SIZE
            if (jobId != JOB_STEP_ID_START + failAfter) {
                val msg = "Job $jobId has returned with exitCode COMPLETED (status COMPLETED): " +
                    "$writtenRecords $key were synchronized."
                expectedMessages.add(msg)
            } else {
                val msg = "Job $jobId has returned with exitCode FAILED (status FAILED): " +
                    "$writtenRecords $key were synchronized."
                expectedMessages.add(msg)
            }
            jobId++
        }

        jobId = JOB_STEP_ID_START
        jobMap.values.forEach { value ->
            if (jobId != JOB_STEP_ID_START + failAfter) {
                jobLauncherFixture(jobId++.toLong(), BatchStatus.COMPLETED, ExitStatus.COMPLETED, value)
            } else {
                jobLauncherFixture(jobId++.toLong(), BatchStatus.FAILED, ExitStatus.FAILED, value)
            }
        }
        return expectedMessages
    }

    private fun assertAllJobsSuccessfulExceptThird(expectedMessages: List<String>, result: SyncJobResult) {
        assertThat(result.messages).hasSize(10)
        assertThat(result.messages.map { it.messageLevel }).containsExactly(
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.ERROR, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO)
        assertThat(result.messages.map { it.message }).containsExactlyElementsOf(expectedMessages)
    }

    @Test
    fun launching_withUnexpectedException_stopsRunningSubsequentJobs() {
        whenever(warner.findUnsynchronizedPapers()).thenReturn(java.util.Optional.empty())
        val expectedMessages = messagesWithExceptionAfter2nd(jobMap)
        expectedMessages.add(
            "Unexpected exception of type class java.lang.RuntimeException: unexpected exception somewhere"
        )

        val result = launcher.launch()
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.isFailed).isTrue()

        assertThat(result.messages.map { it.messageLevel }).containsExactly(
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.ERROR
        )
        assertThat(result.messages.map { it.message }).containsExactlyElementsOf(expectedMessages)

        verify(warner).findUnsynchronizedPapers()
        jobMap.entries.take(3).forEach { entry ->
            verify(jobLauncher).run(eq(entry.value), any())
        }
    }

    @Suppress("ReturnCount")
    private fun messagesWithExceptionAfter2nd(jobMap: Map<String, Job>): MutableList<String> {
        val failAfter = 2

        val expectedMessages = ArrayList<String>()
        var jobId = JOB_STEP_ID_START
        run keyLoop@{
            jobMap.keys.forEach { key ->
                // simple fixture using jobId to get variance in records written
                val writtenRecords = jobId + BATCH_SIZE
                val msg = "Job $jobId has returned with exitCode COMPLETED (status COMPLETED): " +
                    "$writtenRecords $key were synchronized."
                expectedMessages.add(msg)
                if (++jobId == JOB_STEP_ID_START + failAfter) return@keyLoop
            }
        }

        jobId = JOB_STEP_ID_START
        run valueLoop@{
            jobMap.values.forEach { value ->
                jobLauncherFixture(jobId++.toLong(), BatchStatus.COMPLETED, ExitStatus.COMPLETED, value)
                if (jobId == JOB_STEP_ID_START + failAfter) return@valueLoop
            }
        }

        doThrow(RuntimeException("unexpected exception somewhere"))
            .whenever(jobLauncher).run(eq(syncCodeClassJob), any())

        return expectedMessages
    }

    companion object {
        private const val JOB_STEP_ID_START = 75
        private const val BATCH_SIZE = 100
        private const val UNSYNCHED_PAPERS_MSG = "unsynched papers"
    }
}
