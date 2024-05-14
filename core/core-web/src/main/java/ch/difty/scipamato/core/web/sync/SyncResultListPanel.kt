package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior
import org.apache.wicket.event.IEvent
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.StringResourceModel
import java.time.Duration

@Suppress("MagicNumber")
class SyncResultListPanel(id: String) : Panel(id) {

    override fun onInitialize() {
        super.onInitialize()
        outputMarkupId = true
        add(AjaxSelfUpdatingTimerBehavior(Duration.ofSeconds(5)))
        makeAndQueueTable("taskMessages")
    }

    @Suppress("SameParameterValue")
    private fun makeAndQueueTable(id: String) {
        buildList {
            add(makePropertyColumn("messageLevel"))
            add(makePropertyColumn("message"))
        }.run {
            queue(BootstrapDefaultDataTable(id, this, SyncResultDataProvider(), 20)).apply { outputMarkupId = true }
        }
    }

    override fun onEvent(event: IEvent<*>) {
        (event.payload as? BatchJobLaunchedEvent)?.let { jobLaunchedEvent ->
            this@SyncResultListPanel.add(AjaxSelfUpdatingTimerBehavior(Duration.ofSeconds(5)))
            jobLaunchedEvent.target.add(this@SyncResultListPanel)
        }
    }

    private fun makePropertyColumn(propExpression: String) = PropertyColumn<SyncJobResult.LogMessage, String>(
        StringResourceModel("${COLUMN_HEADER}$propExpression", this@SyncResultListPanel, null),
        propExpression,
        propExpression
    )


    companion object {
        private const val serialVersionUID = 1L
        private const val COLUMN_HEADER = "column.header."
    }
}
