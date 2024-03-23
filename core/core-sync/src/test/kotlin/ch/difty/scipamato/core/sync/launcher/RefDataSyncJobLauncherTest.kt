@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.launcher

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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

    private val success: MutableList<String> = mutableListOf()
    private val failure: MutableList<String> = mutableListOf()
    private val warning: MutableList<String> = mutableListOf()
    private var done: Boolean = false

    private val setSuccess: (String) -> Unit = { success.add(it) }
    private val setFailure: (String) -> Unit = { failure.add(it) }
    private val setWarning: (String) -> Unit = { warning.add(it) }
    private val setDone: () -> Unit = { done = true }

    private var jobMap = jobsPerTopic()

    @BeforeEach
    fun setUp() {
        success.clear()
        failure.clear()
        warning.clear()
        done = false
    }

    @AfterEach
    fun tearDown() {
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
        every { warner.findNewsletterswithUnsynchronizedPapers() } returns null
        val successMsg = messagesWithAllStepsSuccessful(jobMap)

        launcher.launch(setSuccess, setFailure, setWarning, setDone)

        assertAllJobsSuccessfulButWithUnsynchedPapers(successMsg, UNSYNCHED_PAPERS_MSG)

        verifyMocks(jobMap)
    }

    @Test
    fun launching_withUnsynchronizedPapersAndNewslettersAndAllStepsSuccessful_addsWarningBeforeStepResultsAndSucceeds() {
        every { warner.findUnsynchronizedPapers() } returns null
        every { warner.findNewsletterswithUnsynchronizedPapers() } returns UNSYNCHED_NEWSLETTER_MSG
        val successMsg = messagesWithAllStepsSuccessful(jobMap)

        launcher.launch(setSuccess, setFailure, setWarning, setDone)

        assertAllJobsSuccessfulButWithUnsynchedPapers(successMsg, UNSYNCHED_NEWSLETTER_MSG)

        verifyMocks(jobMap)
    }

    private fun messagesWithAllStepsSuccessful(jobMap: Map<String, Job>): List<String> {
        val expectedMessages = ArrayList<String>()

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
        every { stepExecution2.writeCount } returns id

        every { jobExecution.stepExecutions } returns listOf(stepExecution1, stepExecution2)

        return jobExecution
    }

    private fun assertAllJobsSuccessfulButWithUnsynchedPapers(successMsg: List<String>, warnMsg: String) {
        warning.shouldHaveSize(1)
        warning.first() shouldBeEqualTo warnMsg
        success shouldContainSame successMsg
        failure.shouldBeEmpty()
        done.shouldBeTrue()
    }

    private fun assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages: List<String>) {
        success shouldContainSame expectedMessages
        warning.shouldBeEmpty()
        failure.shouldBeEmpty()
        done.shouldBeTrue()
    }

    private fun verifyMocks(jobMap: Map<String, Job>) {
        verify { warner.findUnsynchronizedPapers() }
        verify { warner.findNewsletterswithUnsynchronizedPapers() }
        jobMap.values.forEach { job -> verify { jobLauncher.run(job, any()) } }
    }

    @Test
    fun launching_withoutUnsynchronizedPapers_onlyAddsInfoMessages() {
        every { warner.findUnsynchronizedPapers() } returns null
        every { warner.findNewsletterswithUnsynchronizedPapers() } returns null
        val expectedMessages = messagesWithAllStepsSuccessful(jobMap)

        launcher.launch(setSuccess, setFailure, setWarning, setDone)

        assertAllJobsSuccessfulWithNoUnsynchedPapers(expectedMessages)

        verifyMocks(jobMap)
    }

    @Test
    fun launching_withFailingStep_failsJob() {
        every { warner.findUnsynchronizedPapers() } returns null
        every { warner.findNewsletterswithUnsynchronizedPapers() } returns null
        val (successMsg, failureMsg) = messagesWithFailingStepInPosition3(jobMap)

        launcher.launch(setSuccess, setFailure, setWarning, setDone)

        success shouldContainSame successMsg
        failure shouldContainSame failureMsg
        warning.shouldBeEmpty()

        verifyMocks(jobMap)
    }

    private fun messagesWithFailingStepInPosition3(jobMap: Map<String, Job>): Pair<List<String>, List<String>> {
        val failAfter = 2

        val successMessages = ArrayList<String>()
        val failureMessages = ArrayList<String>()
        var jobId = JOB_STEP_ID_START
        jobMap.keys.forEach { key ->
            // simple fixture using jobId to get variance in records written
            val writtenRecords = jobId + BATCH_SIZE
            if (jobId != JOB_STEP_ID_START + failAfter) {
                val msg = "Job $jobId has returned with exitCode COMPLETED (status COMPLETED): " +
                    "$writtenRecords $key were synchronized."
                successMessages.add(msg)
            } else {
                val msg = "Job $jobId has returned with exitCode FAILED (status FAILED): " +
                    "$writtenRecords $key were synchronized."
                failureMessages.add(msg)
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
        return successMessages to failureMessages
    }

    @Test
    fun launching_withUnexpectedException_stopsRunningSubsequentJobs() {
        every { warner.findUnsynchronizedPapers() } returns null
        every { warner.findNewsletterswithUnsynchronizedPapers() } returns null

        val (successMsg, failureMsg) = messagesWithExceptionAfter2nd(jobMap)

        launcher.launch(setSuccess, setFailure, setWarning, setDone)

        success shouldContainSame successMsg
        failure shouldContainSame failureMsg

        verify { warner.findUnsynchronizedPapers() }
        verify { warner.findNewsletterswithUnsynchronizedPapers() }

        jobMap.values.take(3).forEach { job -> verify { jobLauncher.run(job, any()) } }
    }

    @Suppress("ReturnCount")
    private fun messagesWithExceptionAfter2nd(jobMap: Map<String, Job>): Pair<List<String>, List<String>> {
        val failAfter = 2

        val successMsg = ArrayList<String>()
        val failureMsg = ArrayList<String>()
        var jobId = JOB_STEP_ID_START
        run keyLoop@{
            jobMap.keys.forEach { key ->
                // simple fixture using jobId to get variance in records written
                val writtenRecords = jobId + BATCH_SIZE
                val msg = "Job $jobId has returned with exitCode COMPLETED (status COMPLETED): " +
                    "$writtenRecords $key were synchronized."
                successMsg.add(msg)
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

        failureMsg.add("Unexpected exception of type class java.lang.RuntimeException: unexpected exception somewhere")
        return successMsg to failureMsg
    }

    companion object {
        private const val JOB_STEP_ID_START = 75
        private const val BATCH_SIZE = 100L
        private const val UNSYNCHED_PAPERS_MSG = "unsynched papers"
        private const val UNSYNCHED_NEWSLETTER_MSG = "unsynched newsletters"
    }
}
