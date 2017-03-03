package ch.difty.sipamato.web.pages.login;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.markup.html.form.Form;
import org.junit.Test;

import ch.difty.sipamato.web.pages.BasePageTest;

public class LogoutPageTest extends BasePageTest<LogoutPage> {

    private static final String FORM = "form";

    @Override
    protected LogoutPage makePage() {
        return new LogoutPage(null);
    }

    @Override
    protected Class<LogoutPage> getPageClass() {
        return LogoutPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertComponent(FORM, Form.class);
    }

    @Test
    public void submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        getTester().startPage(makePage());
        assertThat(getTester().getSession().isSessionInvalidated()).isFalse();
        getTester().submitForm(FORM);
        getTester().assertRenderedPage(LoginPage.class);
        assertThat(getTester().getSession().isSessionInvalidated()).isTrue();
    }

}
