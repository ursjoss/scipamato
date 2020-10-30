package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class CorePersistenceJooqUtilConfigurationTest {

    @Test
    fun dateTimeService() {
        CorePersistenceJooqUtilConfiguration().dateTimeService() shouldBeInstanceOf CurrentDateTimeService::class
    }
}
