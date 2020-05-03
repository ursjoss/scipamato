package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class UtilTestConfigurationTest {

    @Test
    fun dateTimeService() {
        val uc = UtilTestConfiguration()
        uc.dateTimeService() shouldBeInstanceOf FrozenDateTimeService::class
    }
}