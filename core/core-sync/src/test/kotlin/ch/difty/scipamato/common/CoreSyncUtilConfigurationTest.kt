package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class CoreSyncUtilConfigurationTest {

    @Test
    fun dateTimeService() {
        CoreSyncUtilConfiguration().dateTimeService() shouldBeInstanceOf CurrentDateTimeService::class
    }
}
