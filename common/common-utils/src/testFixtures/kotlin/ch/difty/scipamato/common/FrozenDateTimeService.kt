package ch.difty.scipamato.common

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Implementation of [DateTimeService] constantly returning a frozen moment.
 * This is the implementation to be used in test context.
 */
class FrozenDateTimeService : DateTimeService {

    override val currentDateTime: LocalDateTime
        get() = FROZEN

    override val currentTimestamp: Timestamp
        get() = Timestamp.valueOf(FROZEN)

    override val currentDate: LocalDate
        get() = LocalDate.from(FROZEN)

    companion object {
        private val FROZEN = LocalDateTime.parse("2016-12-09T06:02:13")
    }
}
