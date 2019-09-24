package ch.difty.scipamato.common

import mu.KotlinLogging
import java.sql.Timestamp
import java.time.LocalDateTime

fun logger() = KotlinLogging.logger {}

private val log = logger()

/**
 * Converts a [LocalDateTime] into a [Timestamp].
 */
fun LocalDateTime.toTimestamp(): Timestamp = Timestamp.valueOf(this)


/**
 * Derive an enum of type [T] from a configuration [this@toProperty], all [values] of the enum (`T.values`),
 * a [defaultValue] in case the property value does not match and a [propertyKey] used for logging.
 */
fun <T : Enum<T>> String.asProperty(values: Array<T>, defaultValue: T, propertyKey: String): T {
    if (isNotBlank()) {
        values.filter { equals(it.name, ignoreCase = true) }.take(1).apply {
            if (isNotEmpty()) {
                log.info("{}={}", propertyKey, this@asProperty)
                return first()
            }
        }
    }
    val msg = "{} is not properly defined. Current value: '{}' - now using {} - " + "specify one of {} in your property configuration (e.g. application.properties)."
    log.warn(msg, propertyKey, this, defaultValue, values)
    return defaultValue
}
