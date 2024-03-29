package ch.difty.scipamato.core

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.navigator.LongNavigator
import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import org.apache.wicket.Session
import org.apache.wicket.request.Request

/**
 * Scipamato specific Session
 *
 * Holds an instance of [ItemNavigator] to manage the paper ids of the
 * latest search result. Both keeps track of the id of the currently
 * viewed/edited paper ('focus') and/or move the focus to the previous/next
 * paper in the list of managed ids.
 *
 * Holds an instance of the [SyncJobResult] capturing the state of the
 * Spring batch job that synchronizes Core data to the public database.
 */
class ScipamatoSession(request: Request) : SecureWebSession(request) {
    val paperIdManager: ItemNavigator<Long> = LongNavigator()

    val syncJobResult: SyncJobResult = SyncJobResult()

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic
        fun get(): ScipamatoSession = Session.get() as ScipamatoSession
    }
}
