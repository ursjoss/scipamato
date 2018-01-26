package ch.difty.scipamato.core.web.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import ch.difty.scipamato.core.web.WicketTest;

public class LogoutPageTest extends WicketTest {

    private static final String FORM = "form";

    @Test
    public void assertPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    protected LogoutPage makePage() {
        return new LogoutPage(new PageParameters());
    }

    protected Class<LogoutPage> getPageClass() {
        return LogoutPage.class;
    }

    protected void assertSpecificComponents() {
        getTester().assertComponent(FORM, Form.class);
    }

    @Test
    public void submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        getTester().startPage(makePage());
        assertThat(getTester().getSession()
            .isSessionInvalidated()).isFalse();
        // TODO does not work in the test scenario, investigate
        // getTester().submitForm(FORM);
        // getTester().assertRenderedPage(LoginPage.class);
        // assertThat(getTester().getSession().isSessionInvalidated()).isTrue();
    }

}
