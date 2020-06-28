package ch.difty.scipamato.publ.web.authentication

import ch.difty.scipamato.publ.web.WicketTest
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.jvm.jvmName

internal class ScipamatoAuthenticatedWebSessionConfigTest : WicketTest() {

    @Autowired
    private lateinit var config: ScipamatoAuthenticatedWebSessionConfig

    @Test
    fun canWire() {
        config.shouldNotBeNull()
    }

    @Test
    fun providesSecureWebSession() {
        config.authenticatedWebSessionClass.name shouldBeEqualTo SecureWebSession::class.jvmName
    }
}
