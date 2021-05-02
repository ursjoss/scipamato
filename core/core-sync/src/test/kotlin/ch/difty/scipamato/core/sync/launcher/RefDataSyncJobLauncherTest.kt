@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.launcher

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.launch.JobLauncher

internal class RefDataSyncJobLauncherTest {

    private val jobLauncher = mockk<JobLauncher>()
    private val warner = mockk<Warner>()

    private val syncLanguageJob = mockk<Job>()
    private val syncNewStudyPageLinkJob = mockk<Job>()
    private val syncCodeClassJob = mockk<Job>()
    private val syncCodeJob = mockk<Job>()
    private val syncPaperJob = mockk<Job>()
    private val syncNewsletterJob = mockk<Job>()
    private val syncNewsletterTopicJob = mockk<Job>()
    private val syncNewStudyJob = mockk<Job>()
    private val syncNewStudyTopicJob = mockk<Job>()
    private val syncKeywordJob = mockk<Job>()

    private var launcher = RefDataSyncJobLauncher(
        jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob,
        syncCodeClassJob, syncCodeJob, syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob,
        syncNewStudyTopicJob, syncKeywordJob, warner
    )

    private var jobMap = jobsPerTopic()

    @AfterEach
    fun tearDown() {
//        confirmVerified(jobLauncher, syncLanguageJob, syncNewStudyPageLinkJob, syncCodeClassJob, syncCodeJob,
//            syncPaperJob, syncNewsletterJob, syncNewsletterTopicJob, syncNewStudyJob, syncNewStudyTopicJob,
//            syncKeywordJob, warner)
        confirmVerified(jobLauncher, warner)
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
            "keywords" to syncKeywordJob
        )
    }

    @Test
    fun jobParameters_haveSingleIdentifyingParameterRunDate_withCurrentDate() {
        val params = launcher.jobParameters
        params.parameters.shouldHaveSize(1)
        params.parameters.values.first().isIdentifying.shouldBeTrue()
    }

    @Test
    fun launching_withUnsynchronizedPapersAndAllStepsSuccessful_addsWarningBeforeStepResultsAndSucceeds() {
        every { warner.findUnsynchronizedPapers() } returns UNSYNCHED_PAPERS_MSG
        val expectedMessages = messagesWithAllStepsSuccessful(jobMap, true)

        val result = launcher.launch()

        result.isSuccessful.shouldBeTrue()
        result.isFailed.shouldBeFalse()
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
        every { jobLauncher.run(eq(job), any()) } returns jobExecution
    }

    private fun jobExecutionFixture(id: Long, status: BatchStatus, exitStatus: ExitStatus): JobExecution {
        val jobExecution = mockk<JobExecution>()
        every { jobExecution.id } returns id
        every { jobExecution.status } returns status
        every { jobExecution.exitStatus } returns exitStatus

        val stepExecution1 = mockk<StepExecution>()
        val stepExecution2 = mockk<StepExecution>()
        every { stepExecution1.writeCount } returns BATCH_SIZE
        // simple fixture to get some variance in the returned records
        every { stepExecution2.writeCount } returns id.toInt()

        every { jobExecution.stepExecutions } returns listOf(stepExecution1, stepExecution2)

        return jobExecution
    }

    private fun assertAllJobsSuccessfulButWithUnsynchedPapers(expectedMessages: List<String>, result: SyncJobResult) {
        result.messages shouldHaveSize 11

        // warning due to unsynchronized papers
        val logMessage = result.messages[0]
        logMessage.message shouldBeEqualTo UNSYNCHED_PAPERS_MSG
        logMessage.messageLevel shouldBeEqualTo SyncJobResult.MessageLevel.WARNING

        // job step results
        result.messages.subList(1, result.messages.size).map { it.messageLevel } shouldContain
            SyncJobResult.MessageLevel.INFO
        result.messages.map { it.message } shouldContainAll expectedMessages
    }

    private fun assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages: List<String>, result: SyncJobResult) {
        result.messages shouldHaveSize 10
        result.messages.map { it.messageLevel } shouldContain SyncJobResult.MessageLevel.INFO
        result.messages.map { it.message } shouldContainSame expectedMessages
    }

    private fun verifyMocks(jobMap: Map<String, Job>) {
        verify { warner.findUnsynchronizedPapers() }
        jobMap.values.forEach { job -> verify { jobLauncher.run(job, any()) } }
    }

    @Test
    fun launching_withoutUnsynchronizedPapers_onlyAddsInfoMessages() {
        every { warner.findUnsynchronizedPapers() } returns null
        val expectedMessages = messagesWithAllStepsSuccessful(jobMap, false)

        val result = launcher.launch()
        result.isSuccessful.shouldBeTrue()

        assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages, result)

        verifyMocks(jobMap)
    }

    @Test
    fun launching_withFailingStep_failsJob() {
        every { warner.findUnsynchronizedPapers() } returns null
        val expectedMessages = messagesWithFailingStepInPosition3(jobMap)

        val result = launcher.launch()
        result.isSuccessful.shouldBeFalse()
        result.isFailed.shouldBeTrue()

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
        result.messages shouldHaveSize 10
        result.messages.map { it.messageLevel } shouldContainAll listOf(
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.ERROR, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO,
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO
        )
        result.messages.map { it.message } shouldContainSame expectedMessages
    }

    @Test
    fun launching_withUnexpectedException_stopsRunningSubsequentJobs() {
        every { warner.findUnsynchronizedPapers() } returns null
        val expectedMessages = messagesWithExceptionAfter2nd(jobMap)
        expectedMessages.add(
            "Unexpected exception of type class java.lang.RuntimeException: unexpected exception somewhere"
        )

        val result = launcher.launch()
        result.isSuccessful.shouldBeFalse()
        result.isFailed.shouldBeTrue()

        result.messages.map { it.messageLevel } shouldContainAll listOf(
            SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.INFO, SyncJobResult.MessageLevel.ERROR
        )
        result.messages.map { it.message } shouldContainSame expectedMessages

        verify { warner.findUnsynchronizedPapers() }
        jobMap.values.take(3).forEach { job -> verify { jobLauncher.run(job, any()) } }
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

        every { jobLauncher.run(syncCodeClassJob, any()) } throws RuntimeException("unexpected exception somewhere")

        return expectedMessages
    }

    companion object {
        private const val JOB_STEP_ID_START = 75
        private const val BATCH_SIZE = 100
        private const val UNSYNCHED_PAPERS_MSG = "unsynched papers"
    }
}
