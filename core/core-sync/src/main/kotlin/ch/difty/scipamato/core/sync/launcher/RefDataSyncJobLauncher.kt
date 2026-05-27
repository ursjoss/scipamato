package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.common.logger
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.JobExecution
import org.springframework.batch.core.job.parameters.JobParameters
import org.springframework.batch.core.job.parameters.JobParametersBuilder
import org.springframework.batch.core.launch.JobOperator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private val log = logger()

/**
 * Launcher for the reference dat sync job. Consists of the following synchronization jobs:
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
    private val jobOperator: JobOperator,
    @Qualifier("syncLanguageJob") private val syncLanguageJob: Job,
    @Qualifier("syncNewStudyPageLinkJob") private val syncNewStudyPageLinkJob: Job,
    @Qualifier("syncCodeClassJob") private val syncCodeClassJob: Job,
    @Qualifier("syncCodeJob") private val syncCodeJob: Job,
    @Qualifier("syncPaperJob") private val syncPaperJob: Job,
    @Qualifier("syncNewsletterJob") private val syncNewsletterJob: Job,
    @Qualifier("syncNewsletterTopicJob") private val syncNewsletterTopicJob: Job,
    @Qualifier("syncNewStudyJob") private val syncNewStudyJob: Job,
    @Qualifier("syncNewStudyTopicJob") private val syncNewStudyTopicJob: Job,
    @Qualifier("syncKeywordJob") private val syncKeywordJob: Job, private val warner: Warner,
) : SyncJobLauncher {

    private var jobSuccess: BatchStatus = BatchStatus.UNKNOWN

    val jobParameters: JobParameters
        get() = JobParametersBuilder()
            .addDate("runDate", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), true)
            .toJobParameters()

    @Suppress("TooGenericExceptionCaught")
    override fun launch(
        setSuccess: (String) -> Unit,
        setFailure: (String) -> Unit,
        setWarning: (String) -> Unit,
        setDone: () -> Unit,
    ) {
        log.info { "Starting synchronization job from scipamato-core to scipamato-public..." }
        val jobParameters = jobParameters
        try {
            warner.findUnsynchronizedPapers()?.let {
                setWarning(it)
            }
            warner.findNewsletterswithUnsynchronizedPapers()?.let {
                setWarning(it)
            }
            jobOperator.start(syncLanguageJob, jobParameters).handle("languages", setSuccess, setFailure)
            jobOperator.start(syncNewStudyPageLinkJob, jobParameters).handle("newStudyPage_links", setSuccess, setFailure)
            jobOperator.start(syncCodeClassJob, jobParameters).handle("code_classes", setSuccess, setFailure)
            jobOperator.start(syncCodeJob, jobParameters).handle("codes", setSuccess, setFailure)
            jobOperator.start(syncPaperJob, jobParameters).handle("papers", setSuccess, setFailure)
            jobOperator.start(syncNewsletterJob, jobParameters).handle("newsletters", setSuccess, setFailure)
            jobOperator.start(syncNewsletterTopicJob, jobParameters).handle("newsletterTopics", setSuccess, setFailure)
            jobOperator.start(syncNewStudyTopicJob, jobParameters).handle("newStudyTopics", setSuccess, setFailure)
            jobOperator.start(syncNewStudyJob, jobParameters).handle("newStudies", setSuccess, setFailure)
            jobOperator.start(syncKeywordJob, jobParameters).handle("keywords", setSuccess, setFailure)

            log.info { "Job finished ${if (jobSuccess.isUnsuccessful) "with issues" else "successfully"}." }
            setDone()
        } catch (ex: Exception) {
            log.error(ex) { "Job terminated." }
            setFailure("Unexpected exception of type ${ex.javaClass}: ${ex.message}")
        }
    }

    private fun JobExecution.handle(
        topic: String,
        setSuccess: (String) -> Unit,
        setFailure: (String) -> Unit,
    ) {
        val msg = String.format(
            Locale.US,
            "Job $id has returned with exitCode ${exitStatus.exitCode} (" +
                "status ${this.status.name}" +
                "): ${stepExecutions.sumOf { it.writeCount }} $topic were synchronized.",
        )
        if (this.status == BatchStatus.COMPLETED) {
            setSuccess(msg)
        } else {
            setFailure(msg)
        }
    }
}
