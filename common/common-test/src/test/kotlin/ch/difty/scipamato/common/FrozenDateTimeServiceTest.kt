package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class FrozenDateTimeServiceTest {

    private val service = FrozenDateTimeService()

    @Test
    fun gettingDateTime() {
        service.currentDateTime.toString() shouldBeEqualTo "2016-12-09T06:02:13"
    }

    @Test
    fun gettingTimestamp() {
        service.currentTimestamp.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun gettingCurrentDate() {
        service.currentDate.toString() shouldBeEqualTo "2016-12-09"
    }
}
