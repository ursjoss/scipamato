package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.core.ScipamatoSession.Companion.get
import org.springframework.stereotype.Component

@Component
class CoreWebSessionFacade : ScipamatoWebSessionFacade {

    override val languageCode: String get() = get().locale.language
    override val paperIdManager: ItemNavigator<Long> get() = get().paperIdManager

    override fun hasAtLeastOneRoleOutOf(vararg roles: String): Boolean = roles.any { get().roles.hasRole(it) }

    companion object {
        private const val serialVersionUID = 1L
    }
}
