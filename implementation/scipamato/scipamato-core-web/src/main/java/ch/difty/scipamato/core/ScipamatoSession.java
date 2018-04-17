package ch.difty.scipamato.core;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.navigator.LongNavigator;

/**
 * Scipamato specific Session
 *
 * Holds an instance of {@link ItemNavigator} to manage the paper ids of the
 * latest search result. Both keeps track of the id of the currently
 * viewed/edited paper ('focus') and/or move the focus to the previous/next
 * paper in the list of managed ids.
 *
 * @author u.joss
 */
public final class ScipamatoSession extends SecureWebSession {

    private static final long serialVersionUID = 1L;

    private final ItemNavigator<Long> paperIdManager = new LongNavigator();

    ScipamatoSession(final Request request) {
        super(request);
    }

    public static ScipamatoSession get() {
        return (ScipamatoSession) Session.get();
    }

    public ItemNavigator<Long> getPaperIdManager() {
        return paperIdManager;
    }

}
