package ch.difty.scipamato.common

import mu.KotlinLogging
import java.sql.Timestamp
import java.time.LocalDateTime

fun logger() = KotlinLogging.logger {}

/**
 * Converts a [LocalDateTime] into a [Timestamp].
 */
fun LocalDateTime.toTimestamp(): Timestamp = Timestamp.valueOf(this)
