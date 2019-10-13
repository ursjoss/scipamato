package ch.difty.scipamato.core.web.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.WicketTest;

class LogoutPageTest extends WicketTest {

    private static final String FORM = "form";

    @Test
    void assertPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    private LogoutPage makePage() {
        return new LogoutPage(new PageParameters());
    }

    private Class<LogoutPage> getPageClass() {
        return LogoutPage.class;
    }

    private void assertSpecificComponents() {
        getTester().assertComponent(FORM, Form.class);
    }

    @Test
    @Disabled("TODO restore")
    void submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        getTester().startPage(makePage());
        assertThat(getTester()
            .getSession()
            .isSessionInvalidated()).isFalse();
        getTester().submitForm(FORM);
        getTester().assertRenderedPage(LoginPage.class);
    }
}
