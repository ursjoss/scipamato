package ch.difty.scipamato.common

// TODO remove and replace with kotlin nullability features
class NullArgumentException(argName: String?) : IllegalArgumentException("${argName ?: "Argument"} must not be null.")

object AssertAs {
    fun <T> notNull(value: T?, name: String): T = value ?: throw NullArgumentException(name)
    fun <T> notNull(value: T?): T = value ?: throw NullArgumentException(null)
}
