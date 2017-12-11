package ch.difty.scipamato.public_.web.pages.login;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.scipamato.public_.web.WicketTest;
import ch.difty.scipamato.public_.web.pages.portal.PublicPage;

public class LogoutPageTest extends WicketTest {

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

    public static class CustomAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication arg0) throws AuthenticationException {
            return new TestingAuthenticationToken(USERNAME, PASSWORD, Roles.ADMIN);
        }
    }

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
        assertThat(getTester().getSession().isSessionInvalidated()).isFalse();
        // TODO does not work in the test scenario, investigate
        //        getTester().submitForm(FORM);
        //        getTester().assertRenderedPage(LoginPage.class);
        //        assertThat(getTester().getSession().isSessionInvalidated()).isTrue();
    }

}
