package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.ScipamatoSession
import ch.difty.scipamato.core.web.common.PanelTest
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.wicket.markup.html.panel.Panel
import org.junit.jupiter.api.AfterEach

class SyncResultListPanelTest : PanelTest<SyncResultListPanel>() {

    override fun setUpHook() {
        ScipamatoSession.get().syncJobResult.setWarning("unsynced foo")
        ScipamatoSession.get().syncJobResult.setSuccess("successful bar")
        ScipamatoSession.get().syncJobResult.setFailure("failed baz")
    }

    @AfterEach
    fun tearDown() {
        ScipamatoSession.get().clear()
    }

    override fun makePanel(): SyncResultListPanel = SyncResultListPanel(PANEL_ID)

    override fun assertSpecificComponents() {
        val b = PANEL_ID
        tester.assertComponent(b, Panel::class.java)
        val bb = "$b:taskMessages"
        tester.assertComponent(bb, BootstrapDefaultDataTable::class.java)
        assertTableRow("$bb:body:rows:1:cells", "ERROR", "failed baz")
        assertTableRow("$bb:body:rows:2:cells", "INFO", "successful bar")
        assertTableRow("$bb:body:rows:3:cells", "WARNING", "unsynced foo")
    }

    private fun assertTableRow(bb: String, level: String, msg: String) {
        tester.assertLabel("$bb:1:cell", level)
        tester.assertLabel("$bb:2:cell", msg)
    }
}
