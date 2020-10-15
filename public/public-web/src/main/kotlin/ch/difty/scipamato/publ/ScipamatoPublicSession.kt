package ch.difty.scipamato.publ

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.navigator.LongNavigator
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.apache.wicket.Session
import org.apache.wicket.request.Request

/**
 * Scipamato Public specific Session
 */
class ScipamatoPublicSession(request: Request) : SecureWebSession(request) {

    val paperIdManager: ItemNavigator<Long> = LongNavigator()

    companion object {
        private const val serialVersionUID = 1L
        fun get(): ScipamatoPublicSession = Session.get() as ScipamatoPublicSession
    }
}
