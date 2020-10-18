package ch.difty.scipamato.publ.config

import ch.difty.scipamato.publ.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class DbPropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var dbProperties: DbProperties

    @Test
    fun schema_hasValuePublic() {
        dbProperties.schema shouldBeEqualTo "public"
    }
}
