package ch.difty.scipamato.common.config

import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class MavenPropertiesTest {

    private val mp = MavenProperties()

    @Test
    fun noDefaultValues() {
        mp.version.shouldBeNull()
    }
}
