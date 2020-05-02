package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeOnOrAfter
import org.amshove.kluent.shouldBeOnOrBefore
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class CurrentDateTimeServiceTest {

    private val dts = CurrentDateTimeService()

    @Test
    fun gettingCurrentDateTime() {
        val snapshot = LocalDateTime.now()
        val ldt = dts.currentDateTime
        ldt.shouldBeOnOrAfter(snapshot)
        ldt.shouldBeOnOrBefore(snapshot.plusSeconds(1))
    }

    @Test
    fun gettingCurrentDate() {
        val snapshot = LocalDate.now()
        val ld = dts.currentDate
        ld shouldBeEqualTo snapshot
    }

    @Test
    fun gettingCurrentTimeStamp() {
        val snapshot = LocalDateTime.now()
        val ldt = dts.currentTimestamp.toLocalDateTime()
        ldt.shouldBeOnOrAfter(snapshot.minusSeconds(1))
        ldt.shouldBeOnOrBefore(snapshot.plusSeconds(1))
    }
}
