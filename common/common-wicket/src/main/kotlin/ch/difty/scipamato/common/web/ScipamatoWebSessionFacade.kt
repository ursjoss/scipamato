package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.navigator.ItemNavigator
import java.io.Serializable

/**
 * Facade encapsulating the singleton access to the session.
 *
 * @author Urs Joss
 */
interface ScipamatoWebSessionFacade : Serializable {

    val languageCode: String
    val paperIdManager: ItemNavigator<Long>

    /**
     * Determines if the authenticated users is member of at least one of the specified roles
     *
     * @param roles variable number of role names (as string)
     * @return `true` if the current user has at least one of the specified roles. `false` otherwise.
     */
    fun hasAtLeastOneRoleOutOf(vararg roles: String): Boolean
}
