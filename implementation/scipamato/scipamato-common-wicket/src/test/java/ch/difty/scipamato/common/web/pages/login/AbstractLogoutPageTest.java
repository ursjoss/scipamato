package ch.difty.scipamato.common.web.pages.login;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.WicketBaseTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

public class AbstractLogoutPageTest extends WicketBaseTest {

    private AbstractLogoutPage page;

    @MockBean
    private ApplicationProperties applicationProperties;
    @MockBean
    private DateTimeService       dateTimeService;

    @Override
    protected void setUpHook() {
        page = new TestLogoutPage(new PageParameters());
    }

    @Test
    public void assertPage() {
        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractLogoutPage.class);

        getTester().assertInvisible("navbar");
        getTester().assertComponent("feedback", NotificationPanel.class);
        getTester().assertComponent("form", StatelessForm.class);
    }

    @Test
    public void submitting_withoutLogoutData_rendersErrorMessages() {
        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractLogoutPage.class);
        getTester().submitForm("form");

        getTester().assertRenderedPage(AbstractLogoutPage.class);

        getTester().assertNoErrorMessage();
    }

}
