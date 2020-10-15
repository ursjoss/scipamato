package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.publ.ScipamatoPublicSession
import org.springframework.stereotype.Component

@Component
class PublicWebSessionFacade : ScipamatoWebSessionFacade {

    override val languageCode: String
        get() = ScipamatoPublicSession.get().locale.language

    override val paperIdManager: ItemNavigator<Long>
        get() = ScipamatoPublicSession.get().paperIdManager

    override fun hasAtLeastOneRoleOutOf(vararg roles: String): Boolean = false

    companion object {
        private const val serialVersionUID = 1L
    }
}
