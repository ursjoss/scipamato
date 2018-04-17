package ch.difty.scipamato.publ.web.authentication;

import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.springframework.stereotype.Service;

@Service
public class ScipamatoAuthenticatedWebSessionConfig implements AuthenticatedWebSessionConfig {

    @Override
    public Class<? extends AbstractAuthenticatedWebSession> getAuthenticatedWebSessionClass() {
        return SecureWebSession.class;
    }

}
