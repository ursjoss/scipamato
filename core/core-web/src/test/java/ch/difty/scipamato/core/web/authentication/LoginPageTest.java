package ch.difty.scipamato.core.web.authentication;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.WicketTest;

@SuppressWarnings("SpellCheckingInspection")
class LoginPageTest extends WicketTest {

    @Test
    void assertPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    private LoginPage makePage() {
        return new LoginPage(new PageParameters());
    }

    private Class<LoginPage> getPageClass() {
        return LoginPage.class;
    }

    private void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "username");
        assertLabeledTextField(b, "password");
        getTester().assertComponent(b + ":signin", BootstrapButton.class);

        assertMenuEntries();
    }

    @Test
    void whenSigningIn_withWrongCredentials_providesErrorMessage() {
        SecureWebSession session = (SecureWebSession) getTester().getSession();
        session.signOut();
        getTester().startPage(LoginPage.class);
        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("username", "testuser");
        formTester.setValue("password", "wrongpw");
        formTester.submit();
        getTester().assertErrorMessages("The username and/or password were invalid.");
        getTester().assertRenderedPage(getPageClass());
    }

    private void assertMenuEntries() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertInvisible("navbar");
    }
}
