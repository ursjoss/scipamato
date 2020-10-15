package ch.difty.scipamato.publ.web.authentication

import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.springframework.stereotype.Service

@Service
class ScipamatoAuthenticatedWebSessionConfig : AuthenticatedWebSessionConfig {
    override fun getAuthenticatedWebSessionClass() = SecureWebSession::class.java
}
