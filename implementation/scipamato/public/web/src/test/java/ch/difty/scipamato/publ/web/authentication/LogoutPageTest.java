package ch.difty.scipamato.publ.web.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.publ.web.WicketTest;
import ch.difty.scipamato.publ.web.paper.browse.PublicPage;

@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
class LogoutPageTest extends WicketTest {

    private static final String FORM = "form";

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    @Override
    protected void setUpHook() {
        super.setUpHook();
        login(USERNAME, PASSWORD);
    }

    protected void login(String username, String password) {
        SecureWebSession session = (SecureWebSession) getTester().getSession();
        session.signOut();
        getTester().startPage(LoginPage.class);
        FormTester formTester = getTester().newFormTester(FORM);
        formTester.setValue("username", username);
        formTester.setValue("password", password);
        formTester.submit();
        getTester().assertNoErrorMessage();
        getTester().assertRenderedPage(PublicPage.class);
    }

    @Test
    void assertPage() {
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
    void submitting_invalidatesSessionAndSendsToHomePage_whichForwardsToLoginPage() {
        getTester().startPage(makePage());
        assertThat(getTester()
            .getSession()
            .isSessionInvalidated()).isFalse();
        getTester().submitForm(FORM);
        getTester().assertRenderedPage(LoginPage.class);
    }

}
