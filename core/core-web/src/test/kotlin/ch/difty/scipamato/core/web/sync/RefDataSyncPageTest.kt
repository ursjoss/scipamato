package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import ch.difty.scipamato.core.web.common.BasePageTest
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import io.mockk.confirmVerified
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach

internal class RefDataSyncPageTest : BasePageTest<RefDataSyncPage>() {

    @MockkBean
    private lateinit var jobLauncherMock: SyncJobLauncher

    @AfterEach
    fun tearDown() {
        confirmVerified(jobLauncherMock)
    }

    override fun makePage(): RefDataSyncPage = RefDataSyncPage(PageParameters())

    override val pageClass: Class<RefDataSyncPage>
        get() = RefDataSyncPage::class.java

    override fun assertSpecificComponents() {
        tester.assertComponent("synchForm", Form::class.java)
        tester.assertComponent("synchForm:synchronize", BootstrapAjaxButton::class.java)
        tester.assertComponent("synchForm:syncResults", SyncResultListPanel::class.java)
    }
}
