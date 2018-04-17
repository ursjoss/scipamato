package ch.difty.scipamato.core.web.sync;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher;
import ch.difty.scipamato.core.sync.launcher.SyncJobResult;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class RefDataSyncPageTest extends BasePageTest<RefDataSyncPage> {

    private final SyncJobResult result = new SyncJobResult();

    @MockBean
    private SyncJobLauncher jobLauncherMock;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jobLauncherMock);
    }

    @Override
    protected RefDataSyncPage makePage() {
        return new RefDataSyncPage(new PageParameters());
    }

    @Override
    protected Class<RefDataSyncPage> getPageClass() {
        return RefDataSyncPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertComponent("synchForm", Form.class);
        getTester().assertComponent("synchForm:synchronize", LaddaAjaxButton.class);
    }

    @Test
    public void submitting_triggersSynchronize_withSuccess() {
        result.setSuccess("yep");
        assertAjaxEvent("yep", "Data was successfully exported to the public database.", result);
    }

    @Test
    public void submitting_triggersSynchronize_withFailure() {
        result.setFailure("nope");
        assertAjaxEvent("nope", "Unexpected error occurred while exporting the data to the public database.", result);
    }

    private void assertAjaxEvent(String msg, String expectedLabelText, SyncJobResult result) {
        when(jobLauncherMock.launch()).thenReturn(result);
        getTester().startPage(makePage());
        getTester().executeAjaxEvent("synchForm:synchronize", "click");
        getTester().assertComponentOnAjaxResponse("feedback");
        getTester().assertLabel("feedback:feedbackul:messages:0:message:message", expectedLabelText);
        getTester().assertLabel("feedback:feedbackul:messages:1:message:message", msg);
        verify(jobLauncherMock).launch();
    }
}
