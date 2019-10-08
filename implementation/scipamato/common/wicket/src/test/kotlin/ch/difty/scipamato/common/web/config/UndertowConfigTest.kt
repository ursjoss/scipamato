package ch.difty.scipamato.common.web.config

import ch.difty.scipamato.common.config.ApplicationProperties
import com.nhaarman.mockitokotlin2.mock
import io.undertow.Undertow
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.servlet.api.SecurityInfo
import io.undertow.servlet.api.TransportGuaranteeType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.Session

/**
 * Some exploratory testing next to the actual configuration in UndertowConfig
 *
 * @author u.joss
 */
internal class UndertowConfigTest {

    private val serverPropsMock = mock<ServerProperties>()
    private val scipamatoPropertiesMock = mock<ApplicationProperties>()
    private val config = UndertowConfig(serverPropsMock, scipamatoPropertiesMock)

    private val factory: UndertowServletWebServerFactory = config.undertow() as UndertowServletWebServerFactory

    private val undertowBuilder = Undertow.builder()
    private val deploymentInfo = DeploymentInfo()

    @AfterEach
    fun tearDown() {
        verify<ApplicationProperties>(scipamatoPropertiesMock).redirectFromPort
        verify<ServerProperties>(serverPropsMock).port

        verifyNoMoreInteractions(scipamatoPropertiesMock, serverPropsMock)
    }

    @Test
    fun assertBasicFactoryAttributes() {
        assertThat(factory.accessLogPrefix).isNull()
        assertThat(factory.address == null).isTrue()
        assertThat(factory.compression == null).isTrue()
        assertThat(factory.contextPath).isEmpty()
        assertThat(factory.displayName).isNull()
        assertThat(factory.documentRoot).isNull()
        assertThat(factory.errorPages).isEmpty()
        assertThat(factory.localeCharsetMappings).isEmpty()
        assertThat(factory.port).isEqualTo(8080)
        assertThat(factory.serverHeader).isNull()
        assertThat(factory.session).isInstanceOf(Session::class.java)
        assertThat(factory.session.storeDir == null).isTrue()
        assertThat(factory.ssl == null).isTrue()
        assertThat(factory.sslStoreProvider == null).isTrue()

    }

    @Test
    fun canStartAndStopUndertowServletContainer() {
        val server = factory.getWebServer()
        assertThat(server.port).isEqualTo(0)
        try {
            server.start()
            assertThat(server.port).isEqualTo(8080)
        } catch (ex: Exception) {
            fail<Any>("Did not start successfully: ", ex)
        } finally {
            server.stop()
        }
    }

    @Test
    fun gettingListenerInfo_beforeStartingServer_fails() {
        val undertow = undertowBuilder.build()
        try {
            undertow.listenerInfo
            fail<Any>("server is not started, should not succeed")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(IllegalStateException::class.java).hasMessage("UT000138: Server not started")
        }

    }

    @Test
    fun gettingListenerInfo_afterStartingServer_withNoBuilderCustomizers_hasNoListenerInfo() {
        val undertow = undertowBuilder.build()
        undertow.start()
        assertThat(undertow.listenerInfo).isEmpty()
        undertow.stop()
    }

    @Test
    fun assertCustomizedUndertowBuilder() {
        assertThat(factory.builderCustomizers).hasSize(1)
        val bc = factory.builderCustomizers.iterator().next()
        bc.customize(undertowBuilder)
        val undertow = undertowBuilder.build()

        undertow.start()
        run {
            val listenerInfos = undertow.listenerInfo
            assertThat(listenerInfos).hasSize(1)

            val li = listenerInfos[0]
            val address = li.address.toString()
            if (address.contains("."))
                assertThat(address).startsWith("/0.0.0.0:")
            else
                assertThat(address).startsWith("/0:0:0:0:0:0:0:0:")
            assertThat(li.protcol).isEqualTo("http")
        }
        undertow.stop()
    }

    @Test
    fun assertingWorkers() {
        assertThat(factory.builderCustomizers).hasSize(1)
        val bc = factory.builderCustomizers.iterator().next()
        bc.customize(undertowBuilder)
        val undertow = undertowBuilder.build()

        undertow.start()
        run {
            val worker = undertow.worker
            assertThat(worker.name).startsWith("XNIO-")
            assertThat(worker.xnio.name).isEqualTo("nio")
        }
        undertow.stop()
    }

    @Test
    fun uncustomizedDeploymentInfo_hasNoSecurityConstraints() {
        assertThat(deploymentInfo.securityConstraints).isEmpty()
    }

    @Test
    fun assertSecurityConstraints() {
        assertThat(factory.deploymentInfoCustomizers).hasSize(1)
        val dic = factory.deploymentInfoCustomizers.iterator().next()
        dic.customize(deploymentInfo)

        assertThat(deploymentInfo.securityConstraints).hasSize(1)
        val sc = deploymentInfo.securityConstraints.iterator().next()

        assertThat(sc.webResourceCollections).hasSize(1)

        val wrc = sc.webResourceCollections.iterator().next()
        assertThat(wrc.urlPatterns).hasSize(1)
        assertThat(wrc.urlPatterns.iterator().next()).isEqualTo("/*")
        assertThat(wrc.httpMethods).isEmpty()
        assertThat(wrc.httpMethodOmissions).isEmpty()

        assertThat(sc.transportGuaranteeType).isEqualTo(TransportGuaranteeType.CONFIDENTIAL)
        assertThat(sc.emptyRoleSemantic).isEqualTo(SecurityInfo.EmptyRoleSemantic.PERMIT)

        assertThat(sc.rolesAllowed).isEmpty()
    }

    @Test
    fun hasMimeMappings() {
        assertThat(factory.mimeMappings).isNotEmpty
    }

    @Test
    fun assertJspServlet() {
        val jspServlet = factory.jsp
        assertThat(jspServlet.className).isEqualTo("org.apache.jasper.servlet.JspServlet")
        assertThat(jspServlet.initParameters.keys).containsExactly("development")
        assertThat(jspServlet.initParameters["development"]).isEqualTo("false")
        assertThat(jspServlet.registered).isFalse()
    }

}
