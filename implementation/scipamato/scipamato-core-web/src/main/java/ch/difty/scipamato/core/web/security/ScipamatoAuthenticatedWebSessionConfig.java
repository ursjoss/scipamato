package ch.difty.scipamato.core.web.security;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.springframework.stereotype.Service;

import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

@Service
public class ScipamatoAuthenticatedWebSessionConfig implements AuthenticatedWebSessionConfig {

    @Override
    public Class<? extends AbstractAuthenticatedWebSession> getAuthenticatedWebSessionClass() {
        return SecureWebSession.class;
    }

}
