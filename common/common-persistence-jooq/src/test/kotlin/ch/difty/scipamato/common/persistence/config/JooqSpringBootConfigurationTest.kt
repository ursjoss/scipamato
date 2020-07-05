package ch.difty.scipamato.common.persistence.config

import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

internal class JooqSpringBootConfigurationTest {

    private val conf = JooqSpringBootConfiguration()

    @Test
    fun settings_haveOptimisticLockingConfigured() {
        val settings = conf.settings()
        settings.isExecuteWithOptimisticLocking.shouldBeTrue()
    }
}
