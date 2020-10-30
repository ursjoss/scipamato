package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class PublicWebUtilTestConfigurationTest {

    @Test
    fun dateTimeService() {
        val uc = PublicWebUtilTestConfiguration()
        uc.dateTimeService() shouldBeInstanceOf FrozenDateTimeService::class
    }
}
