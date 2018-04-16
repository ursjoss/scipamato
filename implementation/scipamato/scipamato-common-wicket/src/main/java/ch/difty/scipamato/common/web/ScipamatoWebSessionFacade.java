package ch.difty.scipamato.common.web;

import ch.difty.scipamato.common.navigator.ItemNavigator;

/**
 * Facade encapsulating the singleton access to the session.
 *
 * @author Urs Joss
 */
public interface ScipamatoWebSessionFacade {

    /**
     * @return the language Code of the session's locale
     */
    String getLanguageCode();

    /**
     * @return the {@link ItemNavigator} for paper ids
     */
    ItemNavigator<Long> getPaperIdManager();

}
