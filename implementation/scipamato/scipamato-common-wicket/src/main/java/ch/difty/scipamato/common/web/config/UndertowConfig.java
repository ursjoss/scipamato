package ch.difty.scipamato.common.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.difty.scipamato.common.config.ApplicationProperties;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;

@Configuration
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
     * the port used for https {@literal server.port}. The bean is only instantiated
     * if the property {@literal scipamato.redirect-from-port} is defined.
     */
    @Bean
    @ConditionalOnProperty(name = "scipamato.redirect-from-port", relaxedNames = true)
    public EmbeddedServletContainerFactory undertow() {
        final int portUnsecured = scipamatoProperties.getRedirectFromPort();
        final int portSecured = serverProperties.getPort();
        final UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(builder -> builder.addHttpListener(portUnsecured, "0.0.0.0"));
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo
                .addSecurityConstraint(
                    new SecurityConstraint().addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                        .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                        .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                .setConfidentialPortManager(exchange -> portSecured);
        });
        return factory;
    }

}
