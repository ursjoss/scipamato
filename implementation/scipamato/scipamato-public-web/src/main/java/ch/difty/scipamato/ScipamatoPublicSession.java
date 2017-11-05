package ch.difty.scipamato;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * Scipamato Public specific Session
 *
 * @author u.joss
 */
public final class ScipamatoPublicSession extends WebSession {

    private static final long serialVersionUID = 1L;

    public ScipamatoPublicSession(final Request request) {
        super(request);
    }

    public static ScipamatoPublicSession get() {
        return (ScipamatoPublicSession) Session.get();
    }

}
