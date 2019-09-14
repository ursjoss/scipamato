package ch.difty.scipamato.common.persistence.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class JooqSpringBootConfigurationTest {

    private val conf = JooqSpringBootConfiguration()

    @Test
    fun settings_haveOptimisticLockingConfigured() {
        val settings = conf.settings()
        assertThat(settings.isExecuteWithOptimisticLocking).isTrue()
    }
}
