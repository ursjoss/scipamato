@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.common.web.config

import ch.difty.scipamato.common.config.ApplicationProperties
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.undertow.Undertow
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.servlet.api.SecurityInfo
import io.undertow.servlet.api.TransportGuaranteeType
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldStartWith
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.Session

/**
 * Some exploratory testing next to the actual configuration in UndertowConfig
 *
 * @author u.joss
 */
internal class UndertowConfigTest {

    private val serverPropsMock = mockk<ServerProperties> {
        every { port } returns 8080
    }
    private val scipamatoPropertiesMock = mockk<ApplicationProperties> {
        every { redirectFromPort } returns 8081
    }
    private val config = UndertowConfig(serverPropsMock, scipamatoPropertiesMock)

    private val factory: UndertowServletWebServerFactory = config.getUndertow() as UndertowServletWebServerFactory

    private val undertowBuilder = Undertow.builder()
    private val deploymentInfo = DeploymentInfo()

    @AfterEach
    fun tearDown() {
        verify { scipamatoPropertiesMock.redirectFromPort }
        verify { serverPropsMock.port }

        confirmVerified(scipamatoPropertiesMock, serverPropsMock)
    }

    @Test
    fun assertBasicFactoryAttributes() {
        factory.accessLogPrefix.shouldBeNull()
        factory.address.shouldBeNull()
        factory.compression.shouldBeNull()
        factory.contextPath.shouldBeEmpty()
        factory.displayName.shouldBeNull()
        factory.documentRoot.shouldBeNull()
        factory.errorPages.shouldBeEmpty()
        factory.localeCharsetMappings.shouldBeEmpty()
        factory.port shouldBeEqualTo 8080
        factory.serverHeader.shouldBeNull()
        factory.session shouldBeInstanceOf Session::class
        factory.session.storeDir.shouldBeNull()
        factory.ssl.shouldBeNull()
        factory.sslStoreProvider.shouldBeNull()
    }

    @Test
    fun canStartAndStopUndertowServletContainer() {
        val server = factory.getWebServer()
        server.port shouldBeEqualTo 0
        try {
            server.start()
            server.port shouldBeEqualTo 8080
        } catch (ex: Exception) {
            fail { "Did not start successfully: " }
        } finally {
            server.stop()
        }
    }

    @Test
    fun gettingListenerInfo_beforeStartingServer_fails() {
        val undertow = undertowBuilder.build()
        invoking { undertow.listenerInfo } shouldThrow IllegalStateException::class withMessage "UT000138: Server not started"
    }

    @Test
    fun gettingListenerInfo_afterStartingServer_withNoBuilderCustomizers_hasNoListenerInfo() {
        val undertow = undertowBuilder.build()
        undertow.start()
        undertow.listenerInfo.shouldBeEmpty()
        undertow.stop()
    }

    @Test
    fun assertCustomizedUndertowBuilder() {
        factory.builderCustomizers shouldHaveSize 1
        val bc = factory.builderCustomizers.iterator().next()
        bc.customize(undertowBuilder)
        val undertow = undertowBuilder.build()

        undertow.start()
        run {
            val listenerInfos = undertow.listenerInfo
            listenerInfos shouldHaveSize 1

            val li = listenerInfos[0]
            val address = li.address.toString()
            if (address.contains("."))
                address.shouldStartWith("/0.0.0.0:")
            else
                address.shouldStartWith("/0:0:0:0:0:0:0:0:")
            li.protcol shouldBeEqualTo "http"
        }
        undertow.stop()
    }

    @Test
    fun assertingWorkers() {
        factory.builderCustomizers shouldHaveSize 1
        val bc = factory.builderCustomizers.iterator().next()
        bc.customize(undertowBuilder)
        val undertow = undertowBuilder.build()

        undertow.start()
        run {
            val worker = undertow.worker
            worker.name.shouldStartWith("XNIO-")
            worker.xnio.name shouldBeEqualTo "nio"
        }
        undertow.stop()
    }

    @Test
    fun uncustomizedDeploymentInfo_hasNoSecurityConstraints() {
        deploymentInfo.securityConstraints.shouldBeEmpty()
    }

    @Test
    fun assertSecurityConstraints() {
        factory.deploymentInfoCustomizers shouldHaveSize 1
        val dic = factory.deploymentInfoCustomizers.iterator().next()
        dic.customize(deploymentInfo)

        deploymentInfo.securityConstraints shouldHaveSize 1
        val sc = deploymentInfo.securityConstraints.iterator().next()

        sc.webResourceCollections shouldHaveSize 1

        val wrc = sc.webResourceCollections.iterator().next()
        wrc.urlPatterns shouldHaveSize 1
        wrc.urlPatterns.iterator().next() shouldBeEqualTo "/*"
        wrc.httpMethods.shouldBeEmpty()
        wrc.httpMethodOmissions.shouldBeEmpty()

        sc.transportGuaranteeType shouldBeEqualTo TransportGuaranteeType.CONFIDENTIAL
        sc.emptyRoleSemantic shouldBeEqualTo SecurityInfo.EmptyRoleSemantic.PERMIT

        sc.rolesAllowed.shouldBeEmpty()
    }

    @Test
    fun hasMimeMappings() {
        factory.mimeMappings.shouldNotBeEmpty()
    }

    @Test
    fun assertJspServlet() {
        val jspServlet = factory.jsp
        jspServlet.className shouldBeEqualTo "org.apache.jasper.servlet.JspServlet"
        jspServlet.initParameters.keys shouldContainAll listOf("development")
        jspServlet.initParameters["development"] shouldBeEqualTo "false"
        jspServlet.registered.shouldBeFalse()
    }
}
