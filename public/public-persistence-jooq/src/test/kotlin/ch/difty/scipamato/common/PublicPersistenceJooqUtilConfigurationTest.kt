package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class PublicPersistenceJooqUtilConfigurationTest {

    @Test
    fun dateTimeService() {
        PublicPersistenceJooqUtilConfiguration().dateTimeService() shouldBeInstanceOf CurrentDateTimeService::class
    }
}
