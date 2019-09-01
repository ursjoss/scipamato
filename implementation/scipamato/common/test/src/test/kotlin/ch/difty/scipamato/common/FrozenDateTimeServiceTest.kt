package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class FrozenDateTimeServiceTest {

    private val service = FrozenDateTimeService()

    @Test
    fun gettingDateTime() {
        assertThat(service.currentDateTime.toString()).isEqualTo("2016-12-09T06:02:13")
    }

    @Test
    fun gettingTimestamp() {
        assertThat(service.currentTimestamp.toString()).isEqualTo("2016-12-09 06:02:13.0")
    }

    @Test
    fun gettingCurrentDate() {
        assertThat(service.currentDate.toString()).isEqualTo("2016-12-09")
    }
}
