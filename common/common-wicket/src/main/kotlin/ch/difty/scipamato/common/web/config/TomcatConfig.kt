package ch.difty.scipamato.common.web.config

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.logger
import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory
import org.springframework.boot.web.server.autoconfigure.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val FALLBACK_SSL_PORT = 443

private val log = logger()

@Configuration
open class TomcatConfig(
    private val serverProperties: ServerProperties,
    private val scipamatoProperties: ApplicationProperties,
) {

    /**
     * Bean configuring the embedded Tomcat server to forward an unencrypted connection
     * from a specific port scipamato.redirect-from-port to the confidential port used for
     * https server.port. The bean is only instantiated if the property
     * scipamato.redirect-from-port is defined.
     *
     * @return the configured TomcatServletWebServerFactory
     */
    @Bean
    @ConditionalOnProperty(name = ["scipamato.redirect-from-port"])
    open fun getTomcat(): TomcatServletWebServerFactory {
        val redirectFromPort = scipamatoProperties.redirectFromPort
        val confidentialPort = serverProperties.port
        log.info {
            "Adding http listener on port $redirectFromPort redirecting " +
                "to confidential port $confidentialPort for https."
        }
        val factory = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context) {
                val securityConstraint = SecurityConstraint().apply {
                    userConstraint = "CONFIDENTIAL"
                    addCollection(
                        SecurityCollection().apply { addPattern("/*") }
                    )
                }
                context.addConstraint(securityConstraint)
            }
        }
        redirectFromPort?.let { port ->
            factory.addAdditionalConnectors(
                Connector("org.apache.coyote.http11.Http11NioProtocol").apply {
                    scheme = "http"
                    setPort(port)
                    secure = false
                    redirectPort = confidentialPort ?: FALLBACK_SSL_PORT
                }
            )
        }
        log.debug { "TomcatServletWebServerFactory configured." }
        return factory
    }
}
