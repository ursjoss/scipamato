package ch.difty.scipamato.common.web.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.Undertow.ListenerInfo;
import io.undertow.servlet.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.boot.web.servlet.server.Session;
import org.xnio.XnioWorker;

import ch.difty.scipamato.common.config.ApplicationProperties;

/**
 * Some exploratory testing next to the actual configuration in UndertowConfig
 *
 * @author u.joss
 */
@ExtendWith(MockitoExtension.class)
class UndertowConfigTest {

    private UndertowServletWebServerFactory factory;

    @Mock
    private ServerProperties      serverPropsMock;
    @Mock
    private ApplicationProperties scipamatoPropertiesMock;

    private final Builder        undertowBuilder = Undertow.builder();
    private final DeploymentInfo deploymentInfo  = new DeploymentInfo();

    @BeforeEach
    void setUp() {
        UndertowConfig config = new UndertowConfig(serverPropsMock, scipamatoPropertiesMock);
        factory = (UndertowServletWebServerFactory) config.undertow();
    }

    @AfterEach
    void tearDown() {
        verify(scipamatoPropertiesMock).getRedirectFromPort();
        verify(serverPropsMock).getPort();

        verifyNoMoreInteractions(scipamatoPropertiesMock, serverPropsMock);
    }

    @Test
    void assertBasicFactoryAttributes() {
        assertThat(factory.getAccessLogPrefix()).isNull();
        assertThat(factory.getAddress()).isNull();
        assertThat(factory.getCompression()).isNull();
        assertThat(factory.getContextPath()).isEmpty();
        assertThat(factory.getDisplayName()).isNull();
        assertThat(factory.getDocumentRoot()).isNull();
        assertThat(factory.getErrorPages()).isEmpty();
        assertThat(factory.getLocaleCharsetMappings()).isEmpty();
        assertThat(factory.getPort()).isEqualTo(8080);
        assertThat(factory.getServerHeader()).isNull();
        assertThat(factory.getSession())
            .isNotNull()
            .isInstanceOf(Session.class);
        assertThat(factory
            .getSession()
            .getStoreDir()).isNull();
        assertThat(factory.getSsl()).isNull();
        assertThat(factory.getSslStoreProvider()).isNull();

    }

    @Test
    void canStartAndStopUndertowServletContainer() {
        WebServer server = factory.getWebServer();
        assertThat(server.getPort()).isEqualTo(0);
        try {
            server.start();
            assertThat(server.getPort()).isEqualTo(8080);
        } catch (Exception ex) {
            fail("Did not start successfully: ", ex);
        } finally {
            server.stop();
        }
    }

    @Test
    void gettingListenerInfo_beforeStartingServer_fails() {
        final Undertow undertow = undertowBuilder.build();
        try {
            undertow.getListenerInfo();
            fail("server is not started, should not succeed");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("UT000138: Server not started");
        }
    }

    @Test
    void gettingListenerInfo_afterStartingServer_withNoBuilderCustomizers_hasNoListenerInfo() {
        final Undertow undertow = undertowBuilder.build();
        undertow.start();
        assertThat(undertow.getListenerInfo()).isEmpty();
        undertow.stop();
    }

    @Test
    void assertCustomizedUndertowBuilder() {
        assertThat(factory.getBuilderCustomizers()).hasSize(1);
        final UndertowBuilderCustomizer bc = factory
            .getBuilderCustomizers()
            .iterator()
            .next();
        bc.customize(undertowBuilder);
        Undertow undertow = undertowBuilder.build();

        undertow.start();
        {
            final List<ListenerInfo> listenerInfos = undertow.getListenerInfo();
            assertThat(listenerInfos).hasSize(1);

            final ListenerInfo li = listenerInfos.get(0);
            assertThat(li
                .getAddress()
                .toString()).startsWith("/0:0:0:0:0:0:0:0:");
            assertThat(li.getProtcol()).isEqualTo("http");
        }
        undertow.stop();
    }

    @Test
    void assertingWorkers() {
        assertThat(factory.getBuilderCustomizers()).hasSize(1);
        final UndertowBuilderCustomizer bc = factory
            .getBuilderCustomizers()
            .iterator()
            .next();
        bc.customize(undertowBuilder);
        Undertow undertow = undertowBuilder.build();

        undertow.start();
        {
            XnioWorker worker = undertow.getWorker();
            assertThat(worker).isNotNull();
            assertThat(worker.getName()).startsWith("XNIO-");
            assertThat(worker
                .getXnio()
                .getName()).isEqualTo("nio");
        }
        undertow.stop();
    }

    @Test
    void uncustomizedDeploymentInfo_hasNoSecurityConstraints() {
        assertThat(deploymentInfo.getSecurityConstraints()).isEmpty();
    }

    @Test
    void assertSecurityConstraints() {
        assertThat(factory.getDeploymentInfoCustomizers()).hasSize(1);
        final UndertowDeploymentInfoCustomizer dic = factory
            .getDeploymentInfoCustomizers()
            .iterator()
            .next();

        dic.customize(deploymentInfo);

        assertThat(deploymentInfo.getSecurityConstraints()).hasSize(1);
        final SecurityConstraint sc = deploymentInfo
            .getSecurityConstraints()
            .iterator()
            .next();

        assertThat(sc.getWebResourceCollections()).hasSize(1);

        final WebResourceCollection wrc = sc
            .getWebResourceCollections()
            .iterator()
            .next();
        assertThat(wrc.getUrlPatterns()).hasSize(1);
        assertThat(wrc
            .getUrlPatterns()
            .iterator()
            .next()).isEqualTo("/*");
        assertThat(wrc.getHttpMethods()).isEmpty();
        assertThat(wrc.getHttpMethodOmissions()).isEmpty();

        assertThat(sc.getTransportGuaranteeType()).isEqualTo(TransportGuaranteeType.CONFIDENTIAL);
        assertThat(sc.getEmptyRoleSemantic()).isEqualTo(SecurityInfo.EmptyRoleSemantic.PERMIT);

        assertThat(sc.getRolesAllowed()).isEmpty();
    }

    @Test
    void hasMimeMappings() {
        assertThat(factory.getMimeMappings()).isNotEmpty();
    }

    @Test
    void assertJspServlet() {
        final Jsp jspServlet = factory.getJsp();
        assertThat(jspServlet).isNotNull();
        assertThat(jspServlet.getClassName()).isEqualTo("org.apache.jasper.servlet.JspServlet");
        assertThat(jspServlet
            .getInitParameters()
            .keySet()).containsExactly("development");
        assertThat(jspServlet
            .getInitParameters()
            .get("development")).isEqualTo("false");
        assertThat(jspServlet.getRegistered()).isFalse();
    }

}
