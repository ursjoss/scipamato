package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import ch.difty.scipamato.core.web.common.BasePageTest
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class RefDataSyncPageTest : BasePageTest<RefDataSyncPage>() {

    private val result = SyncJobResult()

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
        tester.assertComponent("synchForm:synchronize", LaddaAjaxButton::class.java)
    }

    @Test
    fun submitting_triggersSynchronize_withSuccess() {
        result.setSuccess("yep")
        assertAjaxEvent("yep", "Data was successfully exported to the public database.", result)
        tester.assertInfoMessages("Data was successfully exported to the public database.", "yep")
        tester.assertNoErrorMessage()
    }

    @Test
    fun submitting_triggersSynchronize_withFailure() {
        result.setFailure("nope")
        assertAjaxEvent("nope", "Unexpected error occurred while exporting the data to the public database.", result)
    }

    @Test
    fun submitting_triggersSynchronize_withWarn() {
        result.setSuccess("yep")
        result.setWarning("hmmm")
        assertAjaxEvent("yep", "Data was successfully exported to the public database.", result)
        tester.assertInfoMessages("Data was successfully exported to the public database.", "yep")
        tester.assertNoErrorMessage()
    }

    private fun assertAjaxEvent(msg: String, expectedLabelText: String, result: SyncJobResult) {
        every { jobLauncherMock.launch() } returns result
        tester.startPage(makePage())
        tester.executeAjaxEvent("synchForm:synchronize", "click")
        tester.assertComponentOnAjaxResponse("feedback")
        tester.assertLabel("feedback:feedbackul:messages:0:message:message", expectedLabelText)
        tester.assertLabel("feedback:feedbackul:messages:1:message:message", msg)
        verify { jobLauncherMock.launch() }
    }
}
