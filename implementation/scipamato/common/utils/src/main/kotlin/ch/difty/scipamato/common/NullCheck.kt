package ch.difty.scipamato.common

@Deprecated(message = "Use kotlin nullability features instead")
class NullArgumentException(argName: String?) : IllegalArgumentException("${argName ?: "Argument"} must not be null.")

// TODO move as top level functions
object AssertAs {
    @Deprecated(message = "Use kotlin nullability features instead")
    fun <T> notNull(value: T?, name: String): T = value ?: throw NullArgumentException(name)

    @Deprecated(message = "Use kotlin nullability features instead")
    fun <T> notNull(value: T?): T = value ?: throw NullArgumentException(null)
}