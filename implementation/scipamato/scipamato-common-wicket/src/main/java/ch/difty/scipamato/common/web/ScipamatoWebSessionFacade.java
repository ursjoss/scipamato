package ch.difty.scipamato.common.web;

import java.io.Serializable;

import ch.difty.scipamato.common.navigator.ItemNavigator;

/**
 * Facade encapsulating the singleton access to the session.
 *
 * @author Urs Joss
 */
public interface ScipamatoWebSessionFacade extends Serializable {

    /**
     * @return the language Code of the session's locale
     */
    String getLanguageCode();

    /**
     * @return the {@link ItemNavigator} for paper ids
     */
    ItemNavigator<Long> getPaperIdManager();

    /**
     * Determines if the authenticated users is member of at least one of the specified roles
     *
     * @param roles
     *     list of role names (as string)
     * @return true if the current user has at least one of the specified roles. False otherwise.
     */
    boolean hasAtLeastOneRoleOutOf(String... roles);

}
