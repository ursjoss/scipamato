package ch.difty.scipamato.common.config

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class MavenPropertiesTest {

    private val mp = MavenProperties()

    @Test
    fun noDefaultValues() {
        assertThat(mp.version).isNull()
        assertThat(mp.timestamp).isNull()
    }
}
