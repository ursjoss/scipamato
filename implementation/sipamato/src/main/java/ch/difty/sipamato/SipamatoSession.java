package ch.difty.sipamato;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.sipamato.navigator.ItemNavigator;
import ch.difty.sipamato.navigator.LongNavigator;

/**
 * Sipamato specific Session
 *
 * Holds an instance of {@link ItemNavigator} to manage the paper ids of the lastest search result.
 * Both keeps track of the id of the currently viewed/edited paper ('focus') and/or move the focus
 * to the previous/next paper in the list of managed ids.
 *
 * @author u.joss
 */
public final class SipamatoSession extends SecureWebSession {

    private static final long serialVersionUID = 1L;

    private final ItemNavigator<Long> paperIdManager = new LongNavigator();

    public SipamatoSession(final Request request) {
        super(request);
    }

    public static SipamatoSession get() {
        return (SipamatoSession) Session.get();
    }

    public ItemNavigator<Long> getPaperIdManager() {
        return paperIdManager;
    }

}
