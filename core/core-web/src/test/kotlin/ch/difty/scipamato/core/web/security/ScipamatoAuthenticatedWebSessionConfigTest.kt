package ch.difty.scipamato.core.web.security

import ch.difty.scipamato.core.web.WicketTest
import ch.difty.scipamato.core.web.authentication.ScipamatoAuthenticatedWebSessionConfig
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ScipamatoAuthenticatedWebSessionConfigTest : WicketTest() {

    @Autowired
    private lateinit var config: ScipamatoAuthenticatedWebSessionConfig

    @Test
    fun canWire() {
        config.shouldNotBeNull()
    }

    @Test
    fun providesSecureWebSession() {
        config.authenticatedWebSessionClass.shouldNotBeNull()
        config.authenticatedWebSessionClass.name shouldBeEqualTo SecureWebSession::class.java.name
    }
}
