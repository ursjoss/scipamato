package ch.difty.scipamato.core.sync.launcher

import java.util.ArrayList

/**
 * The [SyncJobResult] collects log messages for one or more job steps
 * and keeps track of the entire jobs result. If all steps succeed, the jobs
 * result is success. If only one step fails, the job fails.
 */
class SyncJobResult {
    private val logMessages: MutableList<LogMessage> = ArrayList()
    private var result = JobResult.UNKNOWN

    val isSuccessful: Boolean
        get() = result == JobResult.SUCCESS

    val isFailed: Boolean
        get() = result == JobResult.FAILURE

    val messages: List<LogMessage>
        get() = ArrayList(logMessages)

    fun setSuccess(msg: String) {
        if (result != JobResult.FAILURE) result = JobResult.SUCCESS
        logMessages.add(LogMessage(msg, MessageLevel.INFO))
    }

    fun setFailure(msg: String) {
        result = JobResult.FAILURE
        logMessages.add(LogMessage(msg, MessageLevel.ERROR))
    }

    fun setWarning(msg: String) {
        logMessages.add(LogMessage(msg, MessageLevel.WARNING))
    }

    private enum class JobResult {
        UNKNOWN, SUCCESS, FAILURE
    }

    enum class MessageLevel {
        INFO, WARNING, ERROR
    }

    data class LogMessage(
        val message: String? = null,
        val messageLevel: MessageLevel? = null,
    )
}
