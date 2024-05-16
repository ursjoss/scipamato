package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.common.persistence.paging.Sort
import java.io.Serializable
import kotlin.math.min

/**
 * The [SyncJobResult] collects log messages for one or more job steps
 * and keeps track of the entire jobs result. If all steps succeed, the jobs
 * result is success. If a single step fails, the entire job fails.
 */
class SyncJobResult {
    private val logMessages: MutableList<LogMessage> = ArrayList()
    private var result = JobResult.UNKNOWN
    private var running = false
    val isRunning get() = running

    val isSuccessful: Boolean
        get() = result == JobResult.SUCCESS

    val isFailed: Boolean
        get() = result == JobResult.FAILURE

    val messages: List<LogMessage>
        get() = ArrayList(logMessages)

    fun messageCount() = messages.size.toLong()

    fun setSuccess(msg: String) {
        running = true
        if (result != JobResult.FAILURE) result = JobResult.SUCCESS
        logMessages.add(LogMessage(msg, MessageLevel.INFO))
    }

    fun setFailure(msg: String) {
        running = true
        result = JobResult.FAILURE
        logMessages.add(LogMessage(msg, MessageLevel.ERROR))
    }

    fun setWarning(msg: String) {
        running = true
        logMessages.add(LogMessage(msg, MessageLevel.WARNING))
    }

    fun setDone() {
        running = false
    }

    fun getPagedResultMessages(first: Int, count: Int, sortProp: String, dir: Sort.Direction): Iterator<LogMessage> {
        fun <T, R : Comparable<R>> List<T>.sortMethod(selector: (T) -> R?): List<T> =
            if (dir == Sort.Direction.ASC) this.sortedBy(selector)
            else sortedByDescending(selector)

        val sorted = when (sortProp) {
            "messageLevel" -> messages.sortMethod { it.messageLevel }
            else -> messages.sortMethod { it.message }
        }
        return sorted.subList(first, min(first + count, messages.size)).iterator()
    }

    fun clear() {
        logMessages.clear()
        result = JobResult.UNKNOWN
        setDone()
    }

    private enum class JobResult {
        UNKNOWN, SUCCESS, FAILURE
    }

    enum class MessageLevel {
        INFO, WARNING, ERROR
    }

    data class LogMessage(
        val message: String? = null,
        val messageLevel: MessageLevel = MessageLevel.INFO,
    ) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }
}
