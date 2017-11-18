package ch.difty.scipamato;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

/**
 * Scipamato Public specific Session
 *
 * @author u.joss
 */
public final class ScipamatoPublicSession extends SecureWebSession {

    private static final long serialVersionUID = 1L;

    public ScipamatoPublicSession(final Request request) {
        super(request);
    }

    public static ScipamatoPublicSession get() {
        return (ScipamatoPublicSession) Session.get();
    }

}
