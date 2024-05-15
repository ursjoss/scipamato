package ch.difty.scipamato.common.web.config

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.logger
import io.undertow.Undertow
import io.undertow.servlet.api.ConfidentialPortManager
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.servlet.api.SecurityConstraint
import io.undertow.servlet.api.SecurityInfo
import io.undertow.servlet.api.TransportGuaranteeType
import io.undertow.servlet.api.WebResourceCollection
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val log = logger()

@Configuration
open class UndertowConfig(
    private val serverProperties: ServerProperties,
    private val scipamatoProperties: ApplicationProperties,
) {

    /**
     * Bean configuring the embedded undertow server to forward an unencrypted connection
     * from a specific port scipamato.redirect-from-port to the confidential port used for
     * https server.port. The bean is only instantiated if the property
     * scipamato.redirect-from-port is defined.
     *
     * @return the configured EmbeddedServletContainerFactory
     */
    @Bean
    @ConditionalOnProperty(name = ["scipamato.redirect-from-port"])
    open fun getUndertow(): AbstractServletWebServerFactory {
        val redirectFromPort = scipamatoProperties.redirectFromPort
        val confidentialPort = serverProperties.port
        log.info(
            "Adding http listener on port $redirectFromPort redirecting " +
                "to confidential port $confidentialPort for https."
        )
        val factory = UndertowServletWebServerFactory()
        redirectFromPort?.let { port ->
            factory.addBuilderCustomizers(
                UndertowBuilderCustomizer { builder: Undertow.Builder ->
                    builder.addHttpListener(port, "0.0.0.0")
                }
            )
        }
        factory.addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer { deploymentInfo: DeploymentInfo ->
            deploymentInfo.addSecurityConstraint(
                SecurityConstraint()
                    .addWebResourceCollection(WebResourceCollection()
                        .addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT)
            ).confidentialPortManager = ConfidentialPortManager { confidentialPort }
        })
        log.debug("UndertowEmbeddedServletContainerFactory configured.")
        return factory
    }
}
