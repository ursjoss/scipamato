package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class CurrentDateTimeServiceTest {

    private val dts = CurrentDateTimeService()

    @Test
    fun gettingCurrentDateTime() {
        val snapshot = LocalDateTime.now()
        val ldt = dts.currentDateTime
        assertThat(ldt).isBetween(snapshot, snapshot.plusSeconds(1))
    }

    @Test
    fun gettingCurrentDate() {
        val snapshot = LocalDate.now()
        val ld = dts.currentDate
        assertThat(ld).isEqualTo(snapshot)
    }

    @Test
    fun gettingCurrentTimeStamp() {
        val snapshot = LocalDateTime.now()
        val ldt = dts.currentTimestamp.toLocalDateTime()
        assertThat(ldt).isBetween(snapshot.minusSeconds(1), snapshot.plusSeconds(1))
    }
}
