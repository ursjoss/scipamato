package ch.difty.sipamato;

import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

/**
 * Sipamato specific Session, which manages an instance of {@link Navigateable} to track the id of
 * the current paper within a collection of ids of papers from a particular search.
 *
 * @author u.joss
 *
 */
public final class SipamatoSession extends SecureWebSession {

    private static final long serialVersionUID = 1L;

    private Navigateable<Long> navigateable;

    public SipamatoSession(Request request) {
        super(request);
    }

    public final void setPaperIdsToNavigate(final List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            final Navigateable<Long> navigator = new Navigator<>(ids);
            this.navigateable = navigator;
        }
    }

    public final void setCurrentIdToNavigateable(final Long id) {
        if (navigateable != null)
            navigateable.setCurrentItem(id);
    }

    public final Navigateable<Long> getNavigateable() {
        return navigateable;
    }

    public static SipamatoSession get() {
        return (SipamatoSession) Session.get();
    }

}
