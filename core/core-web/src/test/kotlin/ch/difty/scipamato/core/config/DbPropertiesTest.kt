package ch.difty.scipamato.core.config

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class DbPropertiesTest {

    private val dbProperties = DbProperties()

    @Test
    fun schema_hasDefaultValuePublic() {
        dbProperties.schema shouldBeEqualTo "public"
    }
}
