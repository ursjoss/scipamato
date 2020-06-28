package ch.difty.scipamato.publ.config

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class DbPropertiesTest {
    private val dbProperties = DbProperties()

    @Test
    fun schema_hasDefaultValuePublic() {
        dbProperties.schema shouldBeEqualTo "public"
    }
}
