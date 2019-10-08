package ch.difty.scipamato.common

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Implementations provide access to date and time. It's useful to have this as service in order to be mockable.
 */
interface DateTimeService {
    val currentDateTime: LocalDateTime
    val currentDate: LocalDate
    val currentTimestamp: Timestamp
}

/**
 * Implementation of [DateTimeService] actually providing access to the system clock. To be used in production.
 */
class CurrentDateTimeService : DateTimeService {
    override val currentDateTime: LocalDateTime
        get() = LocalDateTime.now()
    override val currentTimestamp: Timestamp
        get() = Timestamp(System.currentTimeMillis())
    override val currentDate: LocalDate
        get() = LocalDate.now()
}
