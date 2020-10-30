package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class CoreWebUtilTestConfigurationTest {

    @Test
    fun dateTimeService() {
        val uc = CoreWebUtilTestConfiguration()
        uc.dateTimeService() shouldBeInstanceOf FrozenDateTimeService::class
    }
}
