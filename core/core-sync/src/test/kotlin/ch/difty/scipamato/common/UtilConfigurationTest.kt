package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class UtilConfigurationTest {

    @Test
    fun dateTimeService() {
        UtilConfiguration().dateTimeService() shouldBeInstanceOf CurrentDateTimeService::class
    }
}
