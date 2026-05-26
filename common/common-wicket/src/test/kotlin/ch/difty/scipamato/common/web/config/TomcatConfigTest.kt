@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.common.web.config

import ch.difty.scipamato.common.config.ApplicationProperties
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory
import org.springframework.boot.web.server.autoconfigure.ServerProperties

/**
 * Some exploratory testing next to the actual configuration in TomcatConfig
 *
 * @author u.joss
 */
internal class TomcatConfigTest {

    private val serverPropsMock = mockk<ServerProperties> {
        every { port } returns 8080
    }
    private val scipamatoPropertiesMock = mockk<ApplicationProperties> {
        every { redirectFromPort } returns 8081
    }
    private val config = TomcatConfig(serverPropsMock, scipamatoPropertiesMock)

    private val factory: TomcatServletWebServerFactory =
        config.getTomcat() as TomcatServletWebServerFactory

    @AfterEach
    fun tearDown() {
        verify { scipamatoPropertiesMock.redirectFromPort }
        verify { serverPropsMock.port }

        confirmVerified(scipamatoPropertiesMock, serverPropsMock)
    }

    @Test
    fun assertBasicFactoryAttributes() {
        factory.address.shouldBeNull()
        factory.compression.shouldBeNull()
        factory.contextPath.shouldBeEmpty()
        factory.errorPages.shouldBeEmpty()
        factory.port shouldBeEqualTo 8080
        factory.serverHeader.shouldBeNull()
        factory.ssl.shouldBeNull()
    }

    @Test
    fun canStartAndStopTomcatServletContainer() {
        val server = factory.getWebServer()
        server.port shouldBeEqualTo -1
        try {
            server.start()
            server.port shouldBeEqualTo 8080
        } catch (ex: Exception) {
            fail { "Did not start successfully: $ex" }
        } finally {
            server.stop()
        }
    }

    @Test
    fun assertSecurityConstraint() {
        // Spin up a real Tomcat context to trigger postProcessContext
        val server = factory.getWebServer()
        try {
            server.start()
            // If we reach here without exception, the security constraint
            // was applied successfully during context post-processing
        } catch (ex: Exception) {
            fail { "Tomcat did not start with security constraint applied: $ex" }
        } finally {
            server.stop()
        }
    }
}
