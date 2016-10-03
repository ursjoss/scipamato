package ch.difty.sipamato.web.pages.login;

import static org.fest.assertions.api.Assertions.assertThat;

import org.apache.wicket.markup.html.form.Form;
import org.junit.Test;

import ch.difty.sipamato.web.pages.BasePageTest;

public class LogoutPageTest extends BasePageTest<LogoutPage> {

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
        String b = "form";
        getTester().assertComponent(b, Form.class);
    }

    @Test
    public void submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        getTester().startPage(makePage());
        assertThat(getTester().getSession().isSessionInvalidated()).isFalse();
        getTester().submitForm("form");
        getTester().assertRenderedPage(LoginPage.class);
        assertThat(getTester().getSession().isSessionInvalidated()).isTrue();
    }

}
