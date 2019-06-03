package ch.difty.scipamato.common.web.config;

import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.common.config.ApplicationProperties;

@Configuration
@Slf4j
public class UndertowConfig {

    private final ServerProperties      serverProperties;
    private final ApplicationProperties scipamatoProperties;

    public UndertowConfig(final ServerProperties serverProps, final ApplicationProperties scipamatoProperties) {
        this.serverProperties = serverProps;
        this.scipamatoProperties = scipamatoProperties;
    }

    /**
     * Bean configuring the embedded undertow server to forward an unencrypted
     * connection from a specific port {@literal scipamato.redirect-from-port} to
     * the confidential port used for https {@literal server.port}. The bean is only
     * instantiated if the property {@literal scipamato.redirect-from-port} is
     * defined.
     *
     * @return the configured EmbeddedServletContainerFactory
     */
    @Bean
    @ConditionalOnProperty(name = "scipamato.redirect-from-port")
    public AbstractServletWebServerFactory undertow() {
        final Integer redirectFromPort = scipamatoProperties.getRedirectFromPort();
        final Integer confidentialPort = serverProperties.getPort();
        log.info("Adding http listener on port {} redirecting to confidential port {} for https.", redirectFromPort,
            confidentialPort);
        final UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(builder -> builder.addHttpListener(redirectFromPort, "0.0.0.0"));
        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo
            .addSecurityConstraint(new SecurityConstraint()
                .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
            .setConfidentialPortManager(httpExchange -> confidentialPort));
        log.debug("UndertowEmbeddedServletContainerFactory configured.");
        return factory;
    }

}
