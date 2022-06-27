package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.common.logger
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.batch.core.repository.JobRestartException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private val log = logger()

/**
 * Launcher for the reference dat sync job. Comprises of the following synchronization jobs:
 *
 *  1. languages
 *  1. NewStudyPage links
 *  1. code_classes
 *  1. codes
 *  1. papers
 *  1. newsletters
 *  1. newsletterTopics
 *  1. newStudyTopics
 *  1. newStudies
 *  1. keywords
 */
@Component
@Profile("!wickettest")
@Suppress("LongParameterList")
open class RefDataSyncJobLauncher(
    private val jobLauncher: JobLauncher,
    @Qualifier("syncLanguageJob") private val syncLanguageJob: Job,
    @Qualifier("syncNewStudyPageLinkJob") private val syncNewStudyPageLinkJob: Job,
    @Qualifier("syncCodeClassJob") private val syncCodeClassJob: Job,
    @Qualifier("syncCodeJob") private val syncCodeJob: Job,
    @Qualifier("syncPaperJob") private val syncPaperJob: Job,
    @Qualifier("syncNewsletterJob") private val syncNewsletterJob: Job,
    @Qualifier("syncNewsletterTopicJob") private val syncNewsletterTopicJob: Job,
    @Qualifier("syncNewStudyJob") private val syncNewStudyJob: Job,
    @Qualifier("syncNewStudyTopicJob") private val syncNewStudyTopicJob: Job,
    @Qualifier("syncKeywordJob") private val syncKeywordJob: Job, private val warner: Warner
) : SyncJobLauncher {

    override fun launch(): SyncJobResult {
        log.info("Starting synchronization job from scipamato-core to scipamato-public...")
        val result = SyncJobResult()
        val jobParameters = jobParameters
        try {
            warnAboutUnsynchronizedEntities(result)
            runSingleJob("languages", syncLanguageJob, result, jobParameters)
            runSingleJob("newStudyPage_links", syncNewStudyPageLinkJob, result, jobParameters)
            runSingleJob("code_classes", syncCodeClassJob, result, jobParameters)
            runSingleJob("codes", syncCodeJob, result, jobParameters)
            runSingleJob("papers", syncPaperJob, result, jobParameters)
            runSingleJob("newsletters", syncNewsletterJob, result, jobParameters)
            runSingleJob("newsletterTopics", syncNewsletterTopicJob, result, jobParameters)
            runSingleJob("newStudyTopics", syncNewStudyTopicJob, result, jobParameters)
            runSingleJob("newStudies", syncNewStudyJob, result, jobParameters)
            runSingleJob("keywords", syncKeywordJob, result, jobParameters)

            log.info("Job finished ${if (result.isSuccessful) "successfully" else "with issues"}.")
        } catch (ex: Exception) {
            log.error("Job terminated.", ex)
            result.setFailure("Unexpected exception of type ${ex.javaClass}: ${ex.message}")
        }
        return result
    }

    // package protected for testing
    val jobParameters: JobParameters
        get() = JobParametersBuilder()
            .addDate(
                "runDate",
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                true
            ).toJobParameters()

    @Throws(JobExecutionAlreadyRunningException::class, JobRestartException::class, JobInstanceAlreadyCompleteException::class, JobParametersInvalidException::class)
    private fun runSingleJob(
        topic: String,
        job: Job,
        result: SyncJobResult,
        jobParameters: JobParameters
    ) = setResultFrom(jobLauncher.run(job, jobParameters), result, topic)

    private fun setResultFrom(jobExecution: JobExecution, result: SyncJobResult, topic: String) {
        val id = jobExecution.id
        val exitCode = jobExecution.exitStatus.exitCode
        val status = jobExecution.status.name
        val writeCount = jobExecution.stepExecutions.sumOf { it.writeCount }
        val msg = String.format("Job $id has returned with exitCode $exitCode (status $status): $writeCount $topic were synchronized.")
        setMessageToResult(result, msg, jobExecution.status)
    }

    private fun setMessageToResult(result: SyncJobResult, msg: String, status: BatchStatus) =
        if (status == BatchStatus.COMPLETED) result.setSuccess(msg) else result.setFailure(msg)

    private fun warnAboutUnsynchronizedEntities(result: SyncJobResult) {
        warner.findUnsynchronizedPapers()?.let {
            result.setWarning(it)
        }
        warner.findNewsletterswithUnsynchronizedPapers()?.let {
            result.setWarning(it)
        }
    }
}
