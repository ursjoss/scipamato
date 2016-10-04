package ch.difty.sipamato.web.pages.login;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.sipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;

public class LoginPageTest extends BasePageTest<LoginPage> {

    @Override
    protected LoginPage makePage() {
        return new LoginPage(null);
    }

    @Override
    protected Class<LoginPage> getPageClass() {
        return LoginPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "username");
        assertLabeledTextField(b, "password");
        getTester().assertComponent(b + ":signin", BootstrapButton.class);
    }

    @Test
    public void whenSigningIn_withWrongCredentials_providesErrorMessage() {
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
}
