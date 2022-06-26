package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.ScipamatoSession
import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

class SyncResultDataProvider : SortableDataProvider<SyncJobResult.LogMessage, String>() {

    private val syncJobResult get() = ScipamatoSession.get().syncJobResult

    override fun iterator(first: Long, count: Long): Iterator<SyncJobResult.LogMessage> {
        val s : SortParam<String>? = sort
        val dir = if (s == null || s.isAscending) Sort.Direction.ASC else Sort.Direction.DESC
        val sortProp = s?.property ?: "message"
        return syncJobResult.getPagedResultMessages(first.toInt(), count.toInt(), sortProp, dir)
    }

    override fun size() = syncJobResult.messageCount()

    override fun model(logMessage: SyncJobResult.LogMessage): IModel<SyncJobResult.LogMessage> = Model.of(logMessage)

    companion object {
        private const val serialVersionUID = 1L
    }
}
